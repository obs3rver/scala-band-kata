package pl.artcoder.playground.kata.bank.domain.money

import pl.artcoder.playground.kata.bank.util.MoneyFormating.formatMoneyToStr

case class Money(val value: BigDecimal) {
  def unary_- : Money = Money(-value)

  def +(other: Money): Money = Money(value + other.value)

  def -(other: Money): Money = Money(value - other.value)

  override def toString: String = formatMoneyToStr(this)
}

object Money {
  def zero: Money = Money(0)
}
