package pl.artcoder.playground.kata.bank.domain.printer

import pl.artcoder.playground.kata.bank.domain.money.Money
import pl.artcoder.playground.kata.bank.domain.transaction.Transaction
import pl.artcoder.playground.kata.bank.util.DateTimeCustomFormatter.formatDateTimeToStr

trait StatementPrinter {
  def printStatement(transactions: List[Transaction]): Unit
}

class TextTableStatementPrinter(val printer: Printer) extends StatementPrinter {
  private val STATEMENT_HEADER = "date || transaction || balance"
  private val SEPARATOR = " || "
  private var balance = zeroBalance

  private def zeroBalance: Money = Money.zero

  private def transactionToStatementLine(transaction: Transaction): Line = {
    balance = balance + transaction.signedAmount

    Line(
      StringBuilder.newBuilder
        .append(formatDateTimeToStr(transaction.timestamp))
        .append(SEPARATOR)
        .append(transaction.signedAmount)
        .append(SEPARATOR)
        .append(balance)
        .toString
    )
  }

  override def printStatement(transactions: List[Transaction]): Unit = {
    balance = zeroBalance
    printer.printLine(Line(STATEMENT_HEADER))
    transactions.map(transactionToStatementLine)
      .reverse
      .foreach {
        printer.printLine
      }
  }
}

object TextTableStatementPrinter {
  def apply(printer: Printer): StatementPrinter = new TextTableStatementPrinter(printer)
}