package org.vtsukur.graphql.demo.cart.domain

import scala.collection.mutable.ListBuffer

case class Cart(id: Long, items: ListBuffer[Item]) {
  def subTotal: BigDecimal = items.map(_.total).sum

  def addProduct(productId: String, productPrice: BigDecimal, quantity: Int): Unit = {
    items.find(_.productId == productId) match {
      case None => items += Item(productId, productPrice, quantity)
      case Some(item) => item.repriceAndAdd(productPrice, quantity)
    }
  }
}
