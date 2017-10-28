package pl.artcoder.playground.kata.bank.account

import pl.artcoder.playground.kata.bank._
import pl.artcoder.playground.kata.bank.money.Money
import pl.artcoder.playground.kata.bank.printer.StatementPrinter
import pl.artcoder.playground.kata.bank.transaction.Transaction
import pl.artcoder.playground.kata.bank.util.{Clock, Repository}

class Account(statementPrinter: StatementPrinter,
              transactionRepository: Repository[Transaction],
              clock: Clock) {
  def deposit(amount: Money): Unit = transactionRepository.save(Transaction(clock.now(), amount))

  def withdraw(amount: Money): Unit = transactionRepository.save(Transaction(clock.now(), -amount))

  def printStatement: Unit = {
    val transactions = transactionRepository.findAll()
    statementPrinter.printStatement(transactions)
  }
}

object Account {
  def apply(statementPrinter: StatementPrinter,
            transactionRepository: Repository[Transaction],
            clock: Clock): Account =
    new Account(statementPrinter, transactionRepository, clock)
}
