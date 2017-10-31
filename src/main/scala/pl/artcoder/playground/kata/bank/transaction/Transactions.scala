package pl.artcoder.playground.kata.bank.transaction

import java.time.LocalDate

import pl.artcoder.playground.kata.bank.account.TimestampFilter.TransactionTimestampFilter
import pl.artcoder.playground.kata.bank.account.TransactionTypeFilter
import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.util.LocalDateExtensions._
import pl.artcoder.playground.kata.bank.util.{CurrentClock, Repository}

import scala.collection.mutable

sealed trait Transaction {
  def timestamp: LocalDate

  def amount: Money

  def signedAmount: Money
}

case class Deposit(timestamp: LocalDate = CurrentClock.now(), amount: Money) extends Transaction {
  override def signedAmount = amount
}

case class Withdrawal(timestamp: LocalDate = CurrentClock.now(), amount: Money) extends Transaction {
  override def signedAmount = -amount
}

trait TransactionRepository extends Repository[Transaction] {
  def findByTransactionTypeAndTimestampFilter(
                                               typeFilter: TransactionTypeFilter,
                                               timestampFilter: TransactionTimestampFilter
                                             ): List[Transaction]

  def findByTransactionType(typeFilter: TransactionTypeFilter): List[Transaction]
}

class TransactionRepositoryImpl extends TransactionRepository {
  val transactions = mutable.ArrayBuffer.empty[Transaction]

  override def save(obj: Transaction): Unit = transactions += obj

  override def findAll(): List[Transaction] = transactions.toList

  override def findByTransactionType(typeFilter: TransactionTypeFilter): List[Transaction] =
    transactions
      .filter(typeFilter.test(_))
      .toList

  override def findByTransactionTypeAndTimestampFilter(
                                                        typeFilter: TransactionTypeFilter,
                                                        timestampFilter: TransactionTimestampFilter
                                                      ): List[Transaction] =
    transactions
      .filter { t =>
        t.timestamp.isBetween(timestampFilter.from, timestampFilter.to) &&
          typeFilter.test(t)
      }
      .toList
}

object TransactionRepositoryImpl {
  def apply(): TransactionRepository = new TransactionRepositoryImpl
}
