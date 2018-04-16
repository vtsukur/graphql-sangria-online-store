package org.vtsukur.graphql.demo.cart.integration

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.{HttpApp, Route}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Json
import io.circe.generic.auto._
import org.vtsukur.graphql.demo.cart.client.ProductServiceClient
import org.vtsukur.graphql.demo.cart.domain.{Cart, Item}
import org.vtsukur.graphql.demo.cart.integration.domain.CartService
import org.vtsukur.graphql.demo.product.integration.domain.ProductDto
import sangria.ast.Document
import sangria.execution.deferred.{Deferred, DeferredResolver}
import sangria.execution.{Executor, QueryReducer}
import sangria.macros.derive._
import sangria.marshalling.circe._
import sangria.parser.QueryParser
import sangria.schema._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class CartHttpApp(cartService: CartService,
                  productServiceClient: ProductServiceClient) extends HttpApp with FailFastCirceSupport {
  override protected def routes: Route = {
    (post & path("graphql")) {
      object readAndParseQuery {

        case class Request(query: String)

        def apply(fn: Document => ToResponseMarshallable): Route = {
          entity(as[Request]) { body =>
            QueryParser.parse(body.query) match {
              case Success(queryAst) => complete(fn(queryAst))
              case Failure(error) => complete(400,
                Json.obj("errors" -> Json.arr(Json.obj("message" -> Json.fromString(error.getMessage)))))
            }
          }
        }
      }
      readAndParseQuery(ast => {
        Executor.execute(CartSchema.definition, ast, cartService,
          deferredResolver = new ProductDelayedResolver,
          maxQueryDepth = Some(10),
          queryReducers = List(
            QueryReducer.rejectComplexQueries[Any](200,
              (_, _) => throw new IllegalArgumentException("Too complex query"))
          )
        )
      })
    } ~
      (get & path("graphiql")) {
        getFromResource("graphiql.html")
      }
  }

  case class ProductDeferred(productId: String, fields: Seq[String]) extends Deferred[ProductDto]

  class ProductDelayedResolver extends DeferredResolver[Any] {
    override def resolve(deferred: Vector[Deferred[Any]], ctx: Any, queryState: Any)(implicit ec: ExecutionContext): Vector[Future[Any]] = {
      val productIds = deferred.map { case ProductDeferred(id, _) => id }
      val fields = deferred.head.asInstanceOf[ProductDeferred].fields
      productServiceClient.fetchProductsByIdsSync(productIds, fields)
        .products
        .map(Future(_)(ec))
        .toVector
    }
  }

  object CartSchema {
    val ProductType: ObjectType[Unit, ProductDto] = deriveObjectType[Unit, ProductDto](
      ReplaceField(
        "images",
        Field("images",
          ListType(StringType),
          arguments = List(Argument.createWithDefault("limit", OptionInputType(IntType), None, Some(0))),
          resolve = c => {
            val requestedLimit = c.arg[Int]("limit")
            val images = c.value.images.get
            images.take(if (requestedLimit <= 0) images.size else Math.min(images.size, requestedLimit))
          })
      )
    )

    val ItemType = ObjectType(
      "Item",
      fields[Unit, Item](
        Field("productId", StringType, resolve = _.value.productId,
          deprecationReason = Some("I need the whole Product, not just id!")),
        Field("product", ProductType, resolve =
          Projector.apply((c, fieldNames) => {
            ProductDeferred(c.value.productId, fieldNames.map(_.name))
          })
        ),
        Field("quantity", IntType, resolve = _.value.quantity),
        Field("total", BigDecimalType, resolve = _.value.total)
      )
    )

    val CartType = ObjectType(
      "Cart",
      fields[Unit, Cart](
        Field("id", LongType, resolve = _.value.id),
        Field("items", ListType(ItemType), resolve = _.value.items),
        Field("subTotal", BigDecimalType, resolve = _.value.subTotal)
      )
    )

    val query = ObjectType(
      "Query",
      fields[CartService, Unit](
        Field("cart", OptionType(CartType),
          arguments = List(Argument("id", LongType)),
          resolve = c => c.ctx.findById(c.arg[Long]("id")),
          complexity = Some((_, _, childScore) => childScore * 2)
        )
      )
    )

    val definition = Schema(query)
  }
}
