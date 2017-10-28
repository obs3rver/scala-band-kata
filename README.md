# Bank Kata in Scala

Inspired by https://github.com/sandromancuso/Bank-kata
Implemented in Scala using outside-in TDD.

Requirements
------------

Deposit and Withdrawal  
Transfer  
Account statement (date, amount, balance)  
Statement printing  
Statement filters (just deposits, withdrawal, date)

Example
------------

Started from defining an acceptance test:

> Given a client makes a deposit of 1000 on 10-10-2017  
And a deposit of 2000 on 12-10-2017  
And a withdrawal of 500 on 14-10-2017  
When she prints her bank statement  
Then she would see  
date       || transaction || balance  
14/10/2017 || -500.00     || 2500.00   
12/10/2017 || 2000.00     || 3000.00  
10/10/2017 || 1000.00     || 1000.00  