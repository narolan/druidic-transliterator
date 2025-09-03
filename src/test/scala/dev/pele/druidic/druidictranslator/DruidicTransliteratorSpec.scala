package dev.pele.druidic.druidictranslator

import cats.effect.IO
import munit.CatsEffectSuite
import org.http4s._
import org.http4s.implicits._

class DruidicTransliteratorSpec extends CatsEffectSuite {

  test("Transliterate returns status code 200") {
    assertIO(retTransliterator.map(_.status), Status.Ok)
  }

  test("Transliterate returns message with transliteration and keeps special characters") {
    assertIO(retTransliterator.flatMap(_.as[String]), "{\"message\":\"ᚺᛖᛚᛚᛟ,!=., \"}")
  }

  private[this] val retTransliterator: IO[Response[IO]] = {
    val getT = Request[IO](Method.GET, uri"/translate?text=Hello,!=.,+#?")
    val transliterators = Transliterators.impl[IO]
    DruidictranslatorRoutes.transliteratorRoutes(transliterators).orNotFound(getT)
  }

}
