package pl.artcoder.playground.kata.bank.account

import java.time.LocalDate

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import pl.artcoder.playground.kata.bank.account.TimestampFilter.TransactionTimestampFilter
import pl.artcoder.playground.kata.bank.account.TransactionTypeFilter.{AllTransactionsFilter, DepositFilter}
import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.printer.StatementPrinter
import pl.artcoder.playground.kata.bank.transaction._
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
    (transactionRepository.save _).expects(Deposit(amount = AMOUNT)).once()

    //when
    account.deposit(AMOUNT)
  }

  it should "record withdraw transaction of given amount" in {
    //expect
    (transactionRepository.save _).expects(Withdrawal(amount = AMOUNT)).once()

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
    val deposit = Deposit(amount = AMOUNT)

    //expect
    (transactionRepository.findByTransactionType _).expects(DepositFilter).returning(List(deposit)).once()
    (statementPrinter.printStatement _).expects(List(deposit)).once()

    //when
    account.printStatement(transactionTypeFilter = DepositFilter)
  }

  it should "print account statement summary with one withdrawal transaction filtered by timestamp" in {
    //given
    val withdrawalTimestamp = LocalDate.of(2017, 10, 10)
    val timestampFilter = TransactionTimestampFilter(LocalDate.of(2017, 10, 5))
    val withdrawal = Withdrawal(withdrawalTimestamp, AMOUNT)

    //expect
    (transactionRepository.findByTransactionTypeAndTimestampFilter _).expects(AllTransactionsFilter, timestampFilter).returning(List(withdrawal)).once()
    (statementPrinter.printStatement _).expects(List(withdrawal)).once()

    //when
    account.printStatement(transactionTimestampFilter = timestampFilter)
  }


}