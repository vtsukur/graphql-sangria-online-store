package org.vtsukur.graphql.demo.cart.client

import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp.Uri.QueryFragment
import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.softwaremill.sttp.circe.asJson
import com.typesafe.scalalogging.StrictLogging
import io.circe
import io.circe.generic.auto._
import org.vtsukur.graphql.demo.product.integration.domain.{ProductDto, ProductsDto}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ProductServiceClient extends StrictLogging {
  import ProductServiceClient._

  private val baseUrl = "http://localhost:9090"

  def fetchProductById(id: String): ProductDto = {
    sendAndReceiveSync(
      sttp.get(uri"$baseUrl/api/products/$id")
        .response(asJson[ProductDto])
    )
  }

  def fetchProductsByIds(ids: Seq[String], fields: Seq[String] = Seq.empty): ProductsDto = {
    sendAndReceiveSync(
      sttp.get(uri"$baseUrl/api/products"
        .queryFragment(QueryFragment.KeyValue("ids", ids.mkString(",")))
        .queryFragment(QueryFragment.KeyValue("include", fields.mkString(",")))
      ).response(asJson[ProductsDto])
    )
  }
}

object ProductServiceClient {
  private implicit val backend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend()

  private def sendAndReceiveSync[T](request: RequestT[Id, Either[circe.Error, T], Nothing]): T = {
    Await.result(request.send(), 10.second).unsafeBody.right.get
  }
}
