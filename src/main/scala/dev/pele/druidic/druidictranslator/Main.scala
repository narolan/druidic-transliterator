package dev.pele.druidic.druidictranslator

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run: IO[Nothing] = DruidictranslatorServer.run[IO]
}
