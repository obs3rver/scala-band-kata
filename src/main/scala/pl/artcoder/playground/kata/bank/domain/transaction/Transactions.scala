package pl.artcoder.playground.kata.bank.domain.transaction

import java.time.LocalDate

import pl.artcoder.playground.kata.bank.domain.account.TimestampFilter.TransactionTimestampFilter
import pl.artcoder.playground.kata.bank.domain.account.TransactionTypeFilter
import pl.artcoder.playground.kata.bank.domain.money.Money
import pl.artcoder.playground.kata.bank.infrastructure.Repository
import pl.artcoder.playground.kata.bank.util.CurrentClock

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
