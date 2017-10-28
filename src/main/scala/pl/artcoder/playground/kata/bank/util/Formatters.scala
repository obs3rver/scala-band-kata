package pl.artcoder.playground.kata.bank.util

import java.text.{DecimalFormat, DecimalFormatSymbols}
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

import pl.artcoder.playground.kata.bank.money.Money

object DateTimeCustomFormatter {
  private val DD_MM_YYYY = "dd/MM/yyyy"
  private val FORMATTER_DD_MM_YYYY = DateTimeFormatter
    .ofPattern(DD_MM_YYYY)
    .withLocale(Locale.forLanguageTag("PL"))

  def parseDateTimeStr(dateStr: String): LocalDate = LocalDate.parse(dateStr, FORMATTER_DD_MM_YYYY)

  def formatDateTimeToStr(date: LocalDate): String = date.format(FORMATTER_DD_MM_YYYY)
}

object MoneyFormating {
  private val FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.getDefault())
  FORMAT_SYMBOLS.setDecimalSeparator('.')
  private val amountFormat = new DecimalFormat("#0.00", FORMAT_SYMBOLS)

  def formatMoneyToStr(money: Money): String = amountFormat.format(money.value)
}