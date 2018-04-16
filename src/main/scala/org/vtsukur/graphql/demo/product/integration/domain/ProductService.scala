package org.vtsukur.graphql.demo.product.integration.domain

import org.vtsukur.graphql.demo.product.domain.{Product, ProductRepository}

class ProductService(repository: ProductRepository) {
  import ProductService._

  def findAll: ProductsDto = toProductsDto(repository.findAll)

  def findAllByIds(ids: Seq[String]): ProductsDto = toProductsDto(repository.findAllByIds(ids))

  def findById(id: String): Option[ProductDto] = repository.findById(id).map(toProductDto)
}

object ProductService {
  private def toProductsDto(products: Seq[Product]): ProductsDto = ProductsDto(products.map(toProductDto))

  private def toProductDto(product: Product): ProductDto = {
    ProductDto(
      id = Some(product.id),
      title = Some(product.title),
      price = Some(product.price),
      description = Some(product.description),
      sku = Some(product.sku),
      images = Some(product.images)
    )
  }
}
