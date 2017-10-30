package pl.artcoder.playground.kata.bank.account

import java.time.LocalDate

import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.printer.StatementPrinter
import pl.artcoder.playground.kata.bank.transaction.TransactionType.{All, Deposit, Withdrawal}
import pl.artcoder.playground.kata.bank.transaction.{Transaction, TransactionRepository, TransactionType}
import pl.artcoder.playground.kata.bank.util.{Clock, CurrentClock}

sealed trait TimestampFilter

case class TransactionTimestampFilter(from: LocalDate, to: LocalDate = CurrentClock.now()) extends TimestampFilter

object EmptyTimestampFilter extends TimestampFilter

class Account(statementPrinter: StatementPrinter,
              transactionRepository: TransactionRepository,
              clock: Clock) {
  def deposit(amount: Money): Unit =
    transactionRepository.save(Transaction(clock.now(), amount, Deposit))

  def withdraw(amount: Money): Unit =
    transactionRepository.save(Transaction(clock.now(), amount, Withdrawal))

  def printStatement(
                      transactionTypeFilter: TransactionType = All,
                      transactionTimestampFilter: TimestampFilter = EmptyTimestampFilter
                    ): Unit = {
    val transactions = (transactionTypeFilter, transactionTimestampFilter) match {
      case (All, EmptyTimestampFilter) => transactionRepository.findAll()
      case (_, EmptyTimestampFilter) => transactionRepository.findByTransactionType(transactionTypeFilter)
      case (transactionType: TransactionType, transactionTimestamp: TransactionTimestampFilter) =>
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
