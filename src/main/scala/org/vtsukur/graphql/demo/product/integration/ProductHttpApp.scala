package org.vtsukur.graphql.demo.product.integration

import akka.http.scaladsl.server.{HttpApp, Route}
import org.vtsukur.graphql.demo.product.integration.domain.ProductService
import org.vtsukur.graphql.demo.product.integration.handlers.{GetProductHandler, GetProductsHandler}

class ProductHttpApp(service: ProductService) extends HttpApp {
  val getProductsHandler = new GetProductsHandler(service)
  val getProductHandler = new GetProductHandler(service)

  override protected def routes: Route = {
    pathPrefix("api") {
      pathPrefix("products") {
        pathEnd {
          get {
            parameters('ids.?, 'include.?) { (ids, include) =>
              getProductsHandler(ids, include)
            }
          }
        } ~
          path(Segment) { id =>
            get {
              getProductHandler(id)
            }
          }
      }
    }
  }
}
