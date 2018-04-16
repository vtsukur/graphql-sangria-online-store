package org.vtsukur.graphql.demo.cart.client

import com.softwaremill.sttp.Uri.QueryFragment
import com.softwaremill.sttp.circe.asJson
import com.softwaremill.sttp._
import com.typesafe.scalalogging.StrictLogging
import io.circe.generic.auto._
import org.vtsukur.graphql.demo.product.integration.domain.{ProductDto, ProductsDto}

class ProductServiceClient extends StrictLogging {
  private implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()

  def fetchProductById(id: String): ProductDto = {
    sttp.get(uri"http://localhost:9090/api/products/$id")
      .response(asJson[ProductDto])
      .send()
      .unsafeBody.right.get
  }

  def fetchProductsByIdsSync(ids: Seq[String], fields: Seq[String] = Seq.empty): ProductsDto = {
    sttp.get(uri"http://localhost:9090/api/products"
      .queryFragment(QueryFragment.KeyValue("ids", ids.mkString(",")))
      .queryFragment(QueryFragment.KeyValue("include", fields.mkString(",")))
    )
      .response(asJson[ProductsDto])
      .send()
      .unsafeBody.right.get
  }
}
