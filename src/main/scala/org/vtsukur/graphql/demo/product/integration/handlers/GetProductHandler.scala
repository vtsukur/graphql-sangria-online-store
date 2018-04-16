package org.vtsukur.graphql.demo.product.integration.handlers

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.StandardRoute
import org.vtsukur.graphql.demo.product.integration.domain.ProductService
import org.vtsukur.graphql.demo.product.integration.json.ProductDtoJsonEncoder

class GetProductHandler(service: ProductService) extends BaseHandler {
  def apply(id: String): StandardRoute = {
    service.findById(id) match {
      case None => complete(HttpResponse(404))
      case Some(product) => complete(ProductDtoJsonEncoder.Default.apply(product))
    }
  }
}
