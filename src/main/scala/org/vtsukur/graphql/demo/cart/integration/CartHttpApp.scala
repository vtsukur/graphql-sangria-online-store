package org.vtsukur.graphql.demo.cart.integration

import akka.http.scaladsl.server.{HttpApp, Route}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import org.vtsukur.graphql.demo.cart.client.ProductServiceClient
import org.vtsukur.graphql.demo.cart.integration.domain.CartService

class CartHttpApp(cartService: CartService,
                  productServiceClient: ProductServiceClient) extends HttpApp with FailFastCirceSupport {
  override protected def routes: Route = ???
}
