package org.vtsukur.graphql.demo.product.domain

class ProductRepository {
  import ProductRepository._

  private val products = Seq(
    Product(
      id = "59eb83c0040fa80b29938e3f",
      title = "Combo Pack with Dreamy Eyes 12\" (Pink) Soft Toy",
      price = BigDecimal(26.99),
      description =
        """
          |Spread Unicorn love amongst your friends and family by
          |purchasing a Unicorn adoption combo pack today.
          |You'll receive your very own fabulous adoption pack and a
          |12" Dreamy Eyes (Pink) cuddly toy. It makes the perfect gift for loved ones.
          |Go on, you know you want to, adopt today!
        """.trimmedOneLiner,
      sku = "010",
      images = Seq(
        "http://localhost:8080/img/918d8d4cc83d4e5f8680ca4edfd5b6b2.jpg",
        "http://localhost:8080/img/f343889c0bb94965845e65d3f39f8798.jpg",
        "http://localhost:8080/img/dd55129473e04f489806db0dc6468dd9.jpg",
        "http://localhost:8080/img/64eba4524a1f4d5d9f1687a815795643.jpg",
        "http://localhost:8080/img/5727549e9131440dbb3cd707dce45d0f.jpg",
        "http://localhost:8080/img/28ae9369ec3c442dbfe6901434ad15af.jpg"
      )
    ),
    Product(
      id = "59eb83c0040fa80b29938e40",
      title = "Dreamy Eyes 12\" (Pink) Unicorn Soft Toy",
      price = BigDecimal(12.99),
      description =
        """
          |A fabulous 12" pink and white Unicorn soft toy for you to snuggle at leisure.
          |Also makes the perfect gift for loved ones.
          |Go on you know you want to, adopt today!
        """.trimmedOneLiner,
      sku = "006",
      images = Seq("http://localhost:8080/img/f343889c0bb94965845e65d3f39f8798.jpg")
    ),
    Product(
      id = "59eb88bd040fa8125aa9c400",
      title = "Combo Pack with Dreamy Eyes 12\" (White) Soft Toy",
      price = BigDecimal(26.99),
      description =
        """
          |Spread Unicorn love amongst your friends and family by
          |purchasing a Unicorn adoption combo pack today.
          |You'll receive your very own fabulous adoption pack and a
          |12" Dreamy Eyes (White) cuddly toy. It makes the perfect gift for loved ones.
          |Go on, you know you want to, adopt today!
        """.trimmedOneLiner,
      sku = "010",
      images = Seq(
        "http://localhost:8080/img/fb72e75c5f29488db41abf2f918769e4.jpg",
        "http://localhost:8080/img/cb8314b3b1e64e6998aad9ab426789be.jpg",
        "http://localhost:8080/img/dd55129473e04f489806db0dc6468dd9.jpg",
        "http://localhost:8080/img/64eba4524a1f4d5d9f1687a815795643.jpg",
        "http://localhost:8080/img/5727549e9131440dbb3cd707dce45d0f.jpg",
        "http://localhost:8080/img/28ae9369ec3c442dbfe6901434ad15af.jpg"
      )
    )
  )

  def findAll: Seq[Product] = products

  def findAllByIds(ids: Seq[String]): Seq[Product] = products filter (p => ids.contains(p.id))

  def findById(id: String): Option[Product] = products find (_.id == id)
}

object ProductRepository {
  private implicit class ToOneLiner(source: String) {
    def trimmedOneLiner: String = source.stripMargin.trim.replaceAll("\n", " ")
  }
}
