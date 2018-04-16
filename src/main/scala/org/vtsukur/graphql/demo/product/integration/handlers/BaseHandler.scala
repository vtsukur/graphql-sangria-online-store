package org.vtsukur.graphql.demo.product.integration.handlers

import akka.http.scaladsl.server.Directives
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

trait BaseHandler extends Directives with FailFastCirceSupport {}
