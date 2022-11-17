package lectures.part2afp

import scala.io.Source.stdin
import scala.util.Try

// ## Partial Functions #############
object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  // more niceer thea before, with pattern matching
  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  //  {1,2,5} => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value

  // aNicerFussyFunction is equivalent to aPartialFunction

  println(aPartialFunction(2))
  //  println(aPartialFunction(57273))    // throws a MatchError and doesn't throws a FunctionNotApplicableException

  // PF utilities
  println(aPartialFunction.isDefinedAt(67)) // do we call without exceptions?

  // lift - return the same function with a wrapper to an Option
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(lifted(98))


  // Partial funtions chain
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal functions

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1, 2, 3).map {
    // it's a partial function
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
    Note: PF can only have ONE parameter type
   */

  /**
   * Exercises
   *
   * 1 - construct a PF instance yourself (anonymous class)
   * instancing anonymous class which derived from partial function
   * 2 - dumb chatbot as a PF
   */

  // My implementation

  // 1
  //  val pFAnonymousClass = new PartialFunction {
  //    val pf : PartialFunction[Int, Int] = {
  //      case 1 => 42
  //      case 2 => 56
  //      case 5 => 999
  //    }
  //  }
  //
  //  pFAnonymousClass.pf.isDefinedAt(3)
  // IT IS WRONG

  // 2
  // IT IS WRONG
  //
  //  class ChatBot with PartialFunction {
  //
  //  }
  //
  //  val myChatBot = new PartialFunction[Unit, Unit] {
  //    override def apply(nothing: Unit): Unit = {
  //    }
  //
  //    override def isDefinedAt(phrase: String): Boolean = {
  //      Try(
  //        apply(phrase)
  //      ).isSuccess
  //    }
  //
  //
  //  }
  //
  //  scala.io.Source.stdin.getLines().foreach(
  //    line => println(myChatBot.userSay(line))
  //  )


  // Solutions

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(x: Int): Int = x match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
    }

    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 5
  }

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi, my name is HAL9000"
    case "goodbye" => "Once you start talking to me, there is no return, human!"
    case "call mom" => "Unable to find your phone without your credit card"
    case _ => "I don't understand"
  }

  def myPrintln(string: String): Unit = println(s"chat bot: $string")

  stdin.getLines().map(chatbot).foreach(myPrintln)


}
