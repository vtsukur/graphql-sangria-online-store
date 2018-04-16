package org.vtsukur.graphql.demo.cart.domain

import org.vtsukur.graphql.demo.cart.client.ProductServiceClient

import scala.collection.mutable.ListBuffer

class CartRepository(productServiceClient: ProductServiceClient) {
  val carts = Seq(mkTestCart)

  private def mkTestCart: Cart = {
    val product1 = productServiceClient.fetchProductById("59eb83c0040fa80b29938e3f")
    val product2 = productServiceClient.fetchProductById("59eb83c0040fa80b29938e40")
    val product3 = productServiceClient.fetchProductById("59eb88bd040fa8125aa9c400")

    val cart = Cart(1, ListBuffer())
    cart.addProduct(product1.id.get, product1.price.get, 1)
    cart.addProduct(product2.id.get, product2.price.get, 2)
    cart.addProduct(product3.id.get, product3.price.get, 1)
    cart
  }

  def findById(id: Long): Option[Cart] = carts find (_.id == id)
}
