package dev.pele.druidic.druidictranslator

import cats.Applicative
import cats.implicits.catsSyntaxApplicativeId
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import scala.annotation.unused

trait Transliterators[F[_]] {

  def transliterate(text: String): F[Transliterators.Transliteration]

}

object Transliterators {

  def apply[F[_]](implicit ev: Transliterators[F]): Transliterators[F] = ev

  final case class Transliteration(text: String) extends AnyVal

  object Transliteration {
    implicit val transliterationEncoder: Encoder[Transliteration] =
      (a: Transliteration) => Json.obj(("message", Json.fromString(a.text)))

    @unused
    implicit def transliterationEntityEncoder[F[_]]: EntityEncoder[F, Transliteration] =
      jsonEncoderOf[F, Transliteration]
  }

  implicit val mapping: Map[Char, Char] = Map(
    'a' -> 'ᚨ', 'b' -> 'ᛒ', 'c' -> 'ᚲ', 'd' -> 'ᛞ',
    'e' -> 'ᛖ', 'f' -> 'ᚠ', 'g' -> 'ᚷ', 'h' -> 'ᚺ',
    'i' -> 'ᛁ', 'j' -> 'ᛃ', 'k' -> 'ᚲ', 'l' -> 'ᛚ',
    'm' -> 'ᛗ', 'n' -> 'ᚾ', 'o' -> 'ᛟ', 'p' -> 'ᛈ',
    'q' -> 'ᛩ', 'r' -> 'ᚱ', 's' -> 'ᛊ', 't' -> 'ᛏ',
    'u' -> 'ᚢ', 'v' -> 'ᚡ', 'w' -> 'ᚹ', 'x' -> 'ᛪ',
    'y' -> 'ᛇ', 'z' -> 'ᛉ'
  )

  def impl[F[_] : Applicative]: Transliterators[F] =
    (text: String) => Transliteration(text.toLowerCase.map(c => mapping.getOrElse(c, c))).pure[F]

}
