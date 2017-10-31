package pl.artcoder.playground.kata.bank.domain.account

import java.time.LocalDate

import pl.artcoder.playground.kata.bank.domain.account.TimestampFilter.{EmptyTimestampFilter, TransactionTimestampFilter}
import pl.artcoder.playground.kata.bank.domain.account.TransactionTypeFilter.AllTransactionsFilter
import pl.artcoder.playground.kata.bank.domain.money.Money
import pl.artcoder.playground.kata.bank.domain.printer.StatementPrinter
import pl.artcoder.playground.kata.bank.domain.transaction.{Deposit, Transaction, TransactionRepository, Withdrawal}
import pl.artcoder.playground.kata.bank.util.{Clock, CurrentClock}

sealed trait TimestampFilter

object TimestampFilter {

  case class TransactionTimestampFilter(from: LocalDate, to: LocalDate = CurrentClock.now()) extends TimestampFilter

  case object EmptyTimestampFilter extends TimestampFilter

}

sealed trait TransactionTypeFilter {
  def test(transaction: Transaction): Boolean
}

object TransactionTypeFilter {

  final object DepositFilter extends TransactionTypeFilter {
    override def test(transaction: Transaction): Boolean = transaction match {
      case Deposit(_, _) => true
      case _ => false
    }
  }

  final object WithdrawalFilter extends TransactionTypeFilter {
    override def test(transaction: Transaction): Boolean = transaction match {
      case Withdrawal(_, _) => true
      case _ => false
    }
  }

  final object AllTransactionsFilter extends TransactionTypeFilter {
    override def test(transaction: Transaction): Boolean = transaction match {
      case Deposit(_, _) => true
      case Withdrawal(_, _) => true
      case _ => false
    }
  }

}

class Account(statementPrinter: StatementPrinter,
              transactionRepository: TransactionRepository,
              clock: Clock) {
  def deposit(amount: Money): Unit =
    transactionRepository.save(Deposit(clock.now(), amount))

  def withdraw(amount: Money): Unit =
    transactionRepository.save(Withdrawal(clock.now(), amount))

  def printStatement(
                      transactionTypeFilter: TransactionTypeFilter = AllTransactionsFilter,
                      transactionTimestampFilter: TimestampFilter = EmptyTimestampFilter
                    ): Unit = {
    val transactions = (transactionTypeFilter, transactionTimestampFilter) match {
      case (AllTransactionsFilter, EmptyTimestampFilter) => transactionRepository.findAll()
      case (_, EmptyTimestampFilter) => transactionRepository.findByTransactionType(transactionTypeFilter)
      case (transactionType: TransactionTypeFilter, transactionTimestamp: TransactionTimestampFilter) =>
        transactionRepository.findByTransactionTypeAndTimestampFilter(transactionType, transactionTimestamp)
    }
    statementPrinter.printStatement(transactions)
  }
}

object Account {
  def apply(statementPrinter: StatementPrinter,
            transactionRepository: TransactionRepository,
            clock: Clock): Account =
    new Account(statementPrinter, transactionRepository, clock)
}
