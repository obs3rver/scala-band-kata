package pl.artcoder.playground.kata.bank.transaction

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import pl.artcoder.playground.kata.bank.money.Money

class TransactionRepositorySpec extends FlatSpec with MockFactory with Matchers {
  val repository = new TransactionRepository

  val AMOUNT = Money(-100)

  behavior of "TransactionRepository"

  it should "save and find given transaction from repository" in {
    //when
    val transactionToSave = Transaction(amount = AMOUNT)
    repository.save(transactionToSave)

    //expect
    val repoList = repository.findAll()
    repoList should have size (1)
    repoList should be(List(transactionToSave))
  }
}
