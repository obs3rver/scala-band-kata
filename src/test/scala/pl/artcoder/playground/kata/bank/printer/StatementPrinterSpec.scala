package pl.artcoder.playground.kata.bank.printer

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.transaction.{Deposit, Transaction, Withdrawal}
import pl.artcoder.playground.kata.bank.util.DateTimeCustomFormatter.parseDateTimeStr

class StatementPrinterSpec extends FlatSpec with MockFactory with Matchers {
  val consolePrinter = mock[Printer]
  val statementPrinter = StatementPrinterImpl(consolePrinter)

  behavior of "StatementPrinter"

  it should "print only a header if given no transactions" in {
    //expect
    (consolePrinter.printLine _) expects Line("date || transaction || balance")

    //when
    statementPrinter.printStatement(List.empty[Transaction])
  }

  it should "print a header and transactions in order sorted by timestamp desc" in {
    //given
    val depositAmount = Money(500)
    val withdrawalAmount = Money(100)
    val depositDate = parseDateTimeStr("10/10/2017")
    val withdrawalDate = parseDateTimeStr("14/10/2017")
    val deposit = Deposit(depositDate, depositAmount)
    val withdrawal = Withdrawal(withdrawalDate, withdrawalAmount)
    val transactions = List(deposit, withdrawal)

    //expect
    inSequence {
      (consolePrinter.printLine _) expects Line("date || transaction || balance")
      (consolePrinter.printLine _) expects Line("14/10/2017 || -100.00 || 400.00")
      (consolePrinter.printLine _) expects Line("10/10/2017 || 500.00 || 500.00")
    }

    //when
    statementPrinter.printStatement(transactions)
  }

}