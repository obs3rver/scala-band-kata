package pl.artcoder.playground.kata.bank

import org.scalamock.scalatest.MockFactory
import org.scalatest._
import pl.artcoder.playground.kata.bank.domain.account.Account
import pl.artcoder.playground.kata.bank.domain.money.Money
import pl.artcoder.playground.kata.bank.domain.printer.{Line, Printer, TextTableStatementPrinter}
import pl.artcoder.playground.kata.bank.infrastructure.InMemoryTransactionRepository
import pl.artcoder.playground.kata.bank.util.Clock
import pl.artcoder.playground.kata.bank.util.DateTimeCustomFormatter.parseDateTimeStr

class DisplayAccountSummaryAcceptanceSpec extends FlatSpec with MockFactory with Matchers {
  val consolePrinter = mock[Printer]
  val clock = stub[Clock]

  val statementPrinter = TextTableStatementPrinter(consolePrinter)
  val transactionRepository = InMemoryTransactionRepository()
  val account = Account(statementPrinter, transactionRepository, clock)

  it should "allow to display summary of transaction history" in {
    //given
    (clock.now _).when().returns(parseDateTimeStr("10/10/2017")).noMoreThanOnce()
    (clock.now _).when().returns(parseDateTimeStr("12/10/2017")).noMoreThanOnce()
    (clock.now _).when().returns(parseDateTimeStr("14/10/2017")).noMoreThanOnce()

    //expect
    inSequence {
      (consolePrinter.printLine _) expects Line("date || transaction || balance")
      (consolePrinter.printLine _) expects Line("14/10/2017 || -500.00 || 2500.00")
      (consolePrinter.printLine _) expects Line("12/10/2017 || 2000.00 || 3000.00")
      (consolePrinter.printLine _) expects Line("10/10/2017 || 1000.00 || 1000.00")
    }

    //when
    account.deposit(Money(1000))
    account.deposit(Money(2000))
    account.withdraw(Money(500))
    account.printStatement()
  }
}
