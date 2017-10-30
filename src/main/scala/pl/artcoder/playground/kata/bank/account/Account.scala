package pl.artcoder.playground.kata.bank.account

import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.printer.StatementPrinter
import pl.artcoder.playground.kata.bank.transaction.TransactionType.{All, Deposit, Withdrawal}
import pl.artcoder.playground.kata.bank.transaction.{Transaction, TransactionRepository, TransactionType}
import pl.artcoder.playground.kata.bank.util.Clock

class Account(statementPrinter: StatementPrinter,
              transactionRepository: TransactionRepository,
              clock: Clock) {
  def deposit(amount: Money): Unit =
    transactionRepository.save(Transaction(clock.now(), amount, Deposit))

  def withdraw(amount: Money): Unit =
    transactionRepository.save(Transaction(clock.now(), amount, Withdrawal))

  def printStatement(filter: TransactionType = All): Unit = {
    val transactions = filter match {
      case All => transactionRepository.findAll()
      case _ => transactionRepository.findByTransactionType(filter)
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
