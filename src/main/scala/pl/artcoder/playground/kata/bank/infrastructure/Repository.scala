package pl.artcoder.playground.kata.bank.infrastructure

trait Repository[T] {
  def save(obj: T)

  def findAll(): List[T]
}