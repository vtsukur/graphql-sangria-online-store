package org.vtsukur.graphql.demo.cart.client

import com.softwaremill.sttp.Uri.QueryFragment
import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.softwaremill.sttp.circe.asJson
import com.typesafe.scalalogging.StrictLogging
import io.circe.generic.auto._
import org.vtsukur.graphql.demo.product.integration.domain.{ProductDto, ProductsDto}

import scala.concurrent.Await
import scala.concurrent.duration._

class ProductServiceClient extends StrictLogging {
  private implicit val backend = AkkaHttpBackend()

  private val baseUrl = "http://localhost:9090"

  def fetchProductById(id: String): ProductDto = {
    Await.result(sttp.get(uri"$baseUrl/api/products/$id")
      .response(asJson[ProductDto])
      .send(), 10.second)
      .unsafeBody.right.get
  }

  def fetchProductsByIds(ids: Seq[String], fields: Seq[String] = Seq.empty): ProductsDto = {
    Await.result(sttp.get(uri"$baseUrl/api/products"
      .queryFragment(QueryFragment.KeyValue("ids", ids.mkString(",")))
      .queryFragment(QueryFragment.KeyValue("include", fields.mkString(",")))
    )
      .response(asJson[ProductsDto])
      .send(), 10.second)
      .unsafeBody.right.get
  }
}
