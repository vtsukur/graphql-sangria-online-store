package org.vtsukur.graphql.demo.product.integration.handlers

import akka.http.scaladsl.server.StandardRoute
import org.vtsukur.graphql.demo.product.integration.domain.ProductService
import org.vtsukur.graphql.demo.product.integration.json.{ProductDtoJsonEncoder, ProductsDtoJsonEncoder}

class GetProductsHandler(service: ProductService) extends BaseHandler {
  def apply(ids: Option[String], include: Option[String]): StandardRoute = {
    val products = findProducts(ids)
    val productEncoder = mkProductEncoder(include)
    complete(new ProductsDtoJsonEncoder(productEncoder)(products))
  }

  private def findProducts(ids: Option[String]) = ids match {
    case None => service.findAll
    case Some(commaSeparatedIds) => service.findAllByIds(commaSeparatedIds.split(","))
  }

  private def mkProductEncoder(include: Option[String]) = include match {
    case None => ProductDtoJsonEncoder.Default
    case Some(string) if string.isEmpty => ProductDtoJsonEncoder.Default
    case Some(commaSeparatedFields) => ProductDtoJsonEncoder.mk(commaSeparatedFields.split(","))
  }
}
