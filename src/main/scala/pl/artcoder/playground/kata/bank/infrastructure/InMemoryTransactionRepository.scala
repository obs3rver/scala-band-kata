package pl.artcoder.playground.kata.bank.infrastructure

import pl.artcoder.playground.kata.bank.domain.account.TimestampFilter.TransactionTimestampFilter
import pl.artcoder.playground.kata.bank.domain.account.TransactionTypeFilter
import pl.artcoder.playground.kata.bank.domain.transaction.{Transaction, TransactionRepository}
import pl.artcoder.playground.kata.bank.util.LocalDateExtensions._

import scala.collection.mutable

class InMemoryTransactionRepository extends TransactionRepository {
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

object InMemoryTransactionRepository {
  def apply(): TransactionRepository = new InMemoryTransactionRepository
}
