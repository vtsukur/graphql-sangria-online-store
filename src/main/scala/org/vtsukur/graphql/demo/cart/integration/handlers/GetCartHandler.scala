package org.vtsukur.graphql.demo.cart.integration.handlers

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.{Directives, StandardRoute}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import org.vtsukur.graphql.demo.cart.integration.domain.CartService
import org.vtsukur.graphql.demo.cart.integration.json.CartJson.encoder

class GetCartHandler(service: CartService) extends Directives with FailFastCirceSupport {
  def apply(id: Long): StandardRoute = {
    service.findById(id) match {
      case None => complete(HttpResponse(404))
      case Some(cart) => complete(cart)
    }
  }
}
