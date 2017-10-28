package pl.artcoder.playground.kata.bank.util

import java.time.LocalDate

trait Clock {
  def now(): LocalDate = LocalDate.now()
}

class CurrentClock extends Clock
object CurrentClock extends Clock