package org.vtsukur.graphql.demo.product

import org.vtsukur.graphql.demo.product.domain.ProductRepository
import org.vtsukur.graphql.demo.product.integration.ProductHttpApp
import org.vtsukur.graphql.demo.product.integration.domain.ProductService

object ProductServer extends App {
  new ProductHttpApp(
    new ProductService(
      new ProductRepository
    )
  ).startServer("0.0.0.0", 9090)
}
