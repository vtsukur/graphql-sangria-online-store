package org.vtsukur.graphql.demo.product.integration.domain

case class ProductDto(id: Option[String],
                      title: Option[String],
                      price: Option[BigDecimal],
                      description: Option[String],
                      sku: Option[String],
                      images: Option[Seq[String]])
