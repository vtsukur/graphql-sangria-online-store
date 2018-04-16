package org.vtsukur.graphql.demo.product.domain

case class Product(id: String,
                   title: String,
                   price: BigDecimal,
                   description: String,
                   sku: String,
                   images: Seq[String])
