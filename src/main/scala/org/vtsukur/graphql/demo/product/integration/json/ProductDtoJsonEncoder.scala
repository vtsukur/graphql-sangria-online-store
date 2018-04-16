package org.vtsukur.graphql.demo.product.integration.json

import io.circe.{Encoder, Json}
import org.vtsukur.graphql.demo.product.integration.domain.ProductDto

import scala.collection.immutable.ListMap

private class ProductDtoJsonEncoder(fieldEncoders: Seq[(String, ProductDto => Json)]) extends Encoder[ProductDto] {
  def apply(product: ProductDto): Json = Json.obj(
    fieldEncoders.map(kv => (kv._1, kv._2.apply(product))): _*
  )
}

object ProductDtoJsonEncoder {
  private val fieldEncodingMap = ListMap[String, ProductDto => Json](
    "id" -> encoders.id,
    "title" -> encoders.title,
    "price" -> encoders.price,
    "description" -> encoders.description,
    "sku" -> encoders.sku,
    "images" -> encoders.images
  )

  private object encoders {
    def id(product: ProductDto): Json = Json.fromString(product.id.get)
    def title(product: ProductDto): Json = Json.fromString(product.title.get)
    def price(product: ProductDto): Json = Json.fromBigDecimal(product.price.get)
    def description(product: ProductDto): Json = Json.fromString(product.description.get)
    def sku(product: ProductDto): Json = Json.fromString(product.sku.get)
    def images(product: ProductDto): Json = Json.arr(product.images.get.map(Json.fromString): _*)
  }

  private val allFieldNames = fieldEncodingMap.keys.toSeq

  val Default: Encoder[ProductDto] = mk(allFieldNames)

  def mk(fieldNames: Seq[String]): Encoder[ProductDto] = {
    fieldNames match {
      case Nil => Default
      case _ => new ProductDtoJsonEncoder(fieldEncodingMap.filter(kv => fieldNames.contains(kv._1)).toList)
    }
  }
}
