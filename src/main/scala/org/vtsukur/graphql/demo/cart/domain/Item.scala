package org.vtsukur.graphql.demo.cart.domain

case class Item(productId: String, var productPrice: BigDecimal, var quantity: Int) {
  def total: BigDecimal = productPrice * quantity

  def repriceAndAdd(productPrice: BigDecimal, additionalQuantity: Int): Unit = {
    this.productPrice = productPrice
    quantity += additionalQuantity
  }
}
