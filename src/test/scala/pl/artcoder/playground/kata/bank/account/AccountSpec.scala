package pl.artcoder.playground.kata.bank.account

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.printer.StatementPrinter
import pl.artcoder.playground.kata.bank.transaction.Transaction
import pl.artcoder.playground.kata.bank.util.{CurrentClock, Repository}

class AccountSpec extends FlatSpec with MockFactory with Matchers {
  val statementPrinter = mock[StatementPrinter]
  val transactionRepository = mock[Repository[Transaction]]

  val clock = new CurrentClock()
  val account = Account(statementPrinter, transactionRepository, clock)

  val AMOUNT = Money(100)

  behavior of "Account"

  it should "record deposit transaction of given amount" in {
    //expect
    (transactionRepository.save _).expects(Transaction(amount = AMOUNT)).once()

    //when
    account.deposit(AMOUNT)
  }

  it should "record withdraw transaction of given amount" in {
    //expect
    (transactionRepository.save _).expects(Transaction(amount = -AMOUNT)).once()

    //when
    account.withdraw(AMOUNT)
  }

  it should "print account statement summary" in {
    //expect
    (transactionRepository.findAll _).expects().returning(List.empty).once()
    (statementPrinter.printStatement _).expects(List.empty).once()

    //when
    account.printStatement
  }

}