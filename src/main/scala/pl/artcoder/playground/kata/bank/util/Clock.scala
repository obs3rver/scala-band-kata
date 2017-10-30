package pl.artcoder.playground.kata.bank.util

import java.time.LocalDate

trait Clock {
  def now(): LocalDate = LocalDate.now()
}

class CurrentClock extends Clock
object CurrentClock extends Clock

object LocalDateExtensions {

  implicit class CustomLocalDate(localDate: LocalDate) {
    def isBetween(from: LocalDate, to: LocalDate): Boolean = {
      localDate.isAfter(from) && localDate.isBefore(to)
    }
  }

}