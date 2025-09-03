package dev.pele.druidic.druidictranslator

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher

object TextQueryParamMatcher extends QueryParamDecoderMatcher[String]("text")

object DruidictranslatorRoutes {

  def transliteratorRoutes[F[_] : Sync](T: Transliterators[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "translate" :? TextQueryParamMatcher(text) =>
        for {
          transliteration <- T.transliterate(text)
          resp <- Ok(transliteration)
        } yield resp
    }
  }

}