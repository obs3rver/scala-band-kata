package pl.artcoder.playground.kata.bank.application

import pl.artcoder.playground.kata.bank.domain.account.Account
import pl.artcoder.playground.kata.bank.domain.money.Money
import pl.artcoder.playground.kata.bank.domain.printer.{ConsolePrinter, TextTableStatementPrinter}
import pl.artcoder.playground.kata.bank.infrastructure.InMemoryTransactionRepository
import pl.artcoder.playground.kata.bank.util.CurrentClock

object DisplayAccountSummaryApp {
  lazy val consolePrinter = ConsolePrinter
  lazy val clock = CurrentClock

  lazy val statementPrinter = TextTableStatementPrinter(consolePrinter)
  lazy val transactionRepository = InMemoryTransactionRepository()
  lazy val account = Account(statementPrinter, transactionRepository, clock)

  def main(args: Array[String]) {
    account.deposit(Money(1000))
    account.deposit(Money(2000))
    account.withdraw(Money(500))
    account.printStatement()
  }
}
