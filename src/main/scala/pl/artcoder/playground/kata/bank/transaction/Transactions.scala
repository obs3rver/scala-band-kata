package pl.artcoder.playground.kata.bank.transaction

import java.time.LocalDate

import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.util.{CurrentClock, Repository}

import scala.collection.mutable

case class Transaction(timestamp: LocalDate = CurrentClock.now(), amount: Money)

class TransactionRepository extends Repository[Transaction] {
  val transactions = mutable.ArrayBuffer.empty[Transaction]

  override def save(obj: Transaction): Unit = transactions += obj

  override def findAll(): List[Transaction] = transactions.toList
}

object TransactionRepository {
  def apply(): Repository[Transaction] = new TransactionRepository
}
