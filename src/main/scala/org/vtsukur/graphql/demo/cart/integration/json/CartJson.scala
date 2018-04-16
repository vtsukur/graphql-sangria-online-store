package org.vtsukur.graphql.demo.cart.integration.json

import io.circe.{Encoder, Json}
import org.vtsukur.graphql.demo.cart.domain.Cart

object CartJson {
  implicit val encoder: Encoder[Cart] = (cart: Cart) => Json.obj(
    ("id", Json.fromLong(cart.id)),
    ("items", Json.arr(cart.items.map(item => ItemJson.encoder(item)): _*)),
    ("subTotal", Json.fromBigDecimal(cart.subTotal))
  )
}
