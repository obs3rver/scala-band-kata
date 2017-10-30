package pl.artcoder.playground.kata.bank.account

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.printer.StatementPrinter
import pl.artcoder.playground.kata.bank.transaction.TransactionType.{Deposit, Withdrawal}
import pl.artcoder.playground.kata.bank.transaction.{Transaction, TransactionRepository, TransactionType}
import pl.artcoder.playground.kata.bank.util.CurrentClock

class AccountSpec extends FlatSpec with MockFactory with Matchers {
  val statementPrinter = mock[StatementPrinter]
  val transactionRepository = mock[TransactionRepository]

  val clock = new CurrentClock()
  val account = Account(statementPrinter, transactionRepository, clock)

  val AMOUNT = Money(100)

  behavior of "Account"

  it should "record deposit transaction of given amount" in {
    //expect
    (transactionRepository.save _).expects(Transaction(amount = AMOUNT, transactionType = Deposit)).once()

    //when
    account.deposit(AMOUNT)
  }

  it should "record withdraw transaction of given amount" in {
    //expect
    (transactionRepository.save _).expects(Transaction(amount = AMOUNT, transactionType = Withdrawal)).once()

    //when
    account.withdraw(AMOUNT)
  }

  it should "print empty account statement summary" in {
    //expect
    (transactionRepository.findAll _).expects().returning(List.empty).once()
    (statementPrinter.printStatement _).expects(List.empty).once()

    //when
    account.printStatement()
  }

  it should "print account statement summary with one deposit transaction" in {
    //given
    val deposit = Transaction(amount = AMOUNT, transactionType = Deposit)

    //expect
    (transactionRepository.findByTransactionType _).expects(Deposit).returning(List(deposit)).once()
    (statementPrinter.printStatement _).expects(List(deposit)).once()

    //when
    account.printStatement(filter = TransactionType.Deposit)
  }


}