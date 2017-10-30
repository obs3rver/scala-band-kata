package pl.artcoder.playground.kata.bank.transaction

import java.time.LocalDate

import pl.artcoder.playground.kata.bank.account.TransactionTimestampFilter
import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.transaction.TransactionType.{Deposit, Withdrawal}
import pl.artcoder.playground.kata.bank.util.LocalDateExtensions._
import pl.artcoder.playground.kata.bank.util.{CurrentClock, Repository}

import scala.collection.mutable

case class Transaction(timestamp: LocalDate = CurrentClock.now(),
                       amount: Money,
                       transactionType: TransactionType) {
  def signedAmount = transactionType match {
    case Deposit => amount
    case Withdrawal => -amount
  }
}

sealed trait TransactionType

object TransactionType {

  final object Deposit extends TransactionType

  final object Withdrawal extends TransactionType

  final object All extends TransactionType

}

trait TransactionRepository extends Repository[Transaction] {
  def findByTransactionTypeAndTimestampFilter(
                                               transactionTypeFilter: TransactionType,
                                               transactionTimestampFilter: TransactionTimestampFilter
                                             ): List[Transaction]

  def findByTransactionType(transactionType: TransactionType): List[Transaction]
}

class TransactionRepositoryImpl extends TransactionRepository {
  val transactions = mutable.ArrayBuffer.empty[Transaction]

  override def save(obj: Transaction): Unit = transactions += obj

  override def findAll(): List[Transaction] = transactions.toList

  override def findByTransactionType(transactionType: TransactionType): List[Transaction] =
    transactions
      .filter(_.transactionType == transactionType)
      .toList

  override def findByTransactionTypeAndTimestampFilter(
                                                        transactionTypeFilter: TransactionType,
                                                        transactionTimestampFilter: TransactionTimestampFilter
                                                      ): List[Transaction] =
    transactions
      .filter { t =>
        t.timestamp.isBetween(transactionTimestampFilter.from, transactionTimestampFilter.to) &&
          t.transactionType == transactionTypeFilter
      }
      .toList
}

object TransactionRepositoryImpl {
  def apply(): TransactionRepository = new TransactionRepositoryImpl
}
