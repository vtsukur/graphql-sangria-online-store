package org.vtsukur.graphql.demo.cart.integration

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.{HttpApp, Route}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Json
import io.circe.generic.auto._
import org.vtsukur.graphql.demo.cart.client.ProductServiceClient
import org.vtsukur.graphql.demo.cart.domain.{Cart, Item}
import org.vtsukur.graphql.demo.cart.integration.domain.CartService
import sangria.ast.Document
import sangria.execution.Executor
import sangria.marshalling.circe._
import sangria.parser.QueryParser
import sangria.schema._

import scala.concurrent.ExecutionContext.Implicits.global
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
        Executor.execute(CartSchema.definition, ast, cartService)
      })
    } ~
      (get & path("graphiql")) {
        getFromResource("graphiql.html")
      }
  }

  object CartSchema {
    val ItemType = ObjectType(
      "Item",
      fields[Unit, Item](
        Field("productId", StringType, resolve = _.value.productId),
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
          resolve = c => c.ctx.findById(c.arg[Long]("id"))
        )
      )
    )

    val definition = Schema(query)
  }
}
