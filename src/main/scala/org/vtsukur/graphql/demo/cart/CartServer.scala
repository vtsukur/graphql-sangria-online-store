package org.vtsukur.graphql.demo.cart

import org.vtsukur.graphql.demo.cart.client.ProductServiceClient
import org.vtsukur.graphql.demo.cart.domain.CartRepository
import org.vtsukur.graphql.demo.cart.integration.CartHttpApp
import org.vtsukur.graphql.demo.cart.integration.domain.CartService

object CartServer extends App {
  private val productServiceClient = new ProductServiceClient()

  new CartHttpApp(
    new CartService(
      new CartRepository(productServiceClient),
      productServiceClient
    ),
    productServiceClient
  ).startServer("0.0.0.0", 8080)
}
