package org.vtsukur.graphql.demo.cart.integration.json

import io.circe.{Encoder, Json}
import org.vtsukur.graphql.demo.cart.domain.Item

object ItemJson {
  implicit val encoder: Encoder[Item] = (item: Item) => Json.obj(
    ("product", Json.fromString(s"http://localhost:9090/api/products/${item.productId}")),
    ("quantity", Json.fromInt(item.quantity)),
    ("total", Json.fromBigDecimal(item.total))
  )
}
