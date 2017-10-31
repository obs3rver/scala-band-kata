package pl.artcoder.playground.kata.bank.domain.printer

case class Line(value: String) extends AnyVal

trait Printer {
  def printLine(line: Line)
}

object ConsolePrinter extends Printer {
  override def printLine(line: Line): Unit = println(line.value)
}
