package pl.artcoder.playground.kata.bank.util

trait Repository[T] {
  def save(obj: T)

  def findAll(): List[T]
}