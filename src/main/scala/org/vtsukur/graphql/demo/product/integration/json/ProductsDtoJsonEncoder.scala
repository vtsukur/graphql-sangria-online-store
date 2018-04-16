package org.vtsukur.graphql.demo.product.integration.json

import io.circe.{Encoder, Json}
import org.vtsukur.graphql.demo.product.integration.domain.{ProductDto, ProductsDto}

class ProductsDtoJsonEncoder(productEncoder: Encoder[ProductDto]) extends Encoder[ProductsDto] {
  def apply(products: ProductsDto): Json = Json.obj(
    ("products", Json.arr(products.products.map(productEncoder(_)): _*))
  )
}
