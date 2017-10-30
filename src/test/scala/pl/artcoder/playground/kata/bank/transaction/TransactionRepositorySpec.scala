package pl.artcoder.playground.kata.bank.transaction

import java.time.LocalDate

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import pl.artcoder.playground.kata.bank.account.TransactionTimestampFilter
import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.transaction.TransactionType.{Deposit, Withdrawal}

class TransactionRepositorySpec extends FlatSpec with MockFactory with Matchers with BeforeAndAfter {
  var repository: TransactionRepository = _

  val AMOUNT = Money(-100)

  val deposit = Transaction(amount = AMOUNT, transactionType = Deposit)
  val withdrawal = Transaction(amount = AMOUNT, transactionType = Withdrawal)

  before {
    repository = new TransactionRepositoryImpl
  }

  behavior of "TransactionRepository"

  it should "save and find given transaction from repository" in {
    //when
    repository.save(deposit)

    //expect
    val repoList = repository.findAll()
    repoList should have size (1)
    repoList should be(List(deposit))
  }

  it should "find transaction by operation type" in {
    //when
    repository.save(deposit)
    repository.save(withdrawal)

    //expect
    val repoList = repository.findByTransactionType(Deposit)
    repoList should have size (1)
    repoList should be(List(deposit))
  }

  it should "find transaction by operation type and timestamp" in {
    //given
    val transactionTimestampFilter = TransactionTimestampFilter(LocalDate.of(2017, 10, 5))
    val depositTimestamp = LocalDate.of(2017, 10, 1)
    val withdrawalTimestamp = LocalDate.of(2017, 10, 10)
    val deposit = Transaction(depositTimestamp, AMOUNT, Deposit)
    val withdrawal = Transaction(withdrawalTimestamp, AMOUNT, Withdrawal)

    //when
    repository.save(deposit)
    repository.save(withdrawal)

    //expect
    val repoList = repository.findByTransactionTypeAndTimestampFilter(Withdrawal, transactionTimestampFilter)
    repoList should have size (1)
    repoList should be(List(withdrawal))
  }
}
