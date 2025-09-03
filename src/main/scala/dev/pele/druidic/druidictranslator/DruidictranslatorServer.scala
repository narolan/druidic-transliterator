package dev.pele.druidic.druidictranslator

import cats.effect.Async
import com.comcast.ip4s._
import fs2.io.net.Network
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object DruidictranslatorServer {

  def run[F[_] : Async : Network]: F[Nothing] = {
    for {
      _ <- EmberClientBuilder.default[F].build
      transliterationAlg = Transliterators.impl[F]

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract segments not checked
      // in the underlying routes.
      httpApp = DruidictranslatorRoutes.transliteratorRoutes[F](transliterationAlg).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      host = ipv4"0.0.0.0"
      port = Port.fromInt(sys.env.get("PORT").flatMap(_.toIntOption).getOrElse(8080)).get

      _ <-
        EmberServerBuilder.default[F]
          .withHost(host)
          .withPort(port)
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
}
