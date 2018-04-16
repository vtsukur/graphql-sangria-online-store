package org.vtsukur.graphql.demo.cart.integration.domain

import org.vtsukur.graphql.demo.cart.client.ProductServiceClient
import org.vtsukur.graphql.demo.cart.domain.{Cart, CartRepository}

class CartService(repository: CartRepository, productServiceClient: ProductServiceClient) {
  def findById(id: Long): Option[Cart] = repository.findById(id)
  def addProductToCart(cartId: Long, productId: String, quantity: Int): Option[Cart] = {
    findById(cartId).map(cart => {
      val product = productServiceClient.fetchProductById(productId)
      cart.addProduct(productId, product.price.get, quantity)
      cart
    })
  }
}
