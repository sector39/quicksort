package com.freick.quicksort

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

object QuickSort extends App {

  trait Step

  case class Initialize(left: Int, right: Int) extends Step {
    override def toString: String = s"Call QuickSort with ${array.slice(left, left + right + 1 - left).mkString("[", " ", "]")}"
  }

  case object MoveRight extends Step {
    override def toString: String = "Move right pointer to the left"
  }

  case object MoveLeft extends Step {
    override def toString: String = "Move left pointer to the right"
  }

  case class Swap(left: Int, right: Int) extends Step {
    override def toString: String = s"Swap $left with $right and move both pointers"
  }

  def yellow(obj: Any): String = "\u001B[33m" + obj + "\u001B[0m"

  def red(obj: Any): String = "\u001B[31m" + obj + "\u001B[0m"

  def blue(obj: Any): String = "\u001B[34m" + obj + "\u001B[0m"

  def purple(obj: Any): String = "\u001B[35m" + obj + "\u001B[0m"

  def printStep(left: Int, right: Int, pivot: Int, step: Step): Unit = {
    if (left > right) {
      return
    }

    steps = steps + 1

    if (step.isInstanceOf[Swap]) {
      swaps = swaps + 1
    }

    if (step.isInstanceOf[Initialize]) {
      for (i <- metaLine.indices) {
        if (metaLine(i) != " ") {
          metaLine.update(i, "|")
        }
      }

      metaLine.update(left * 2, yellow("|"))
      metaLine.update((right + 1) * 2, yellow("|"))
    }

    val leftArrow = (left + 1) * 2 - 1
    val rightArrow = right * 2 + 1

    val stringBuilder = new StringBuilder

    // line1 | array
    stringBuilder ++= array.mkString("[", " ", "]")
    stringBuilder.delete(pivot * 2 + 1, pivot * 2 + 2)
    stringBuilder.insert(pivot * 2 + 1, yellow(array(pivot)))
    stringBuilder ++= s"\t\tSteps: $steps\tSwaps: $swaps\t$step\n"

    // line2 | metaLine
    if (leftArrow == rightArrow) {
      stringBuilder ++= metaLine.updated(leftArrow, purple("↑")).mkString("")
    } else {
      stringBuilder ++= metaLine.updated(leftArrow, red("↑")).updated(rightArrow, blue("↑")).mkString("")
    }

    println(stringBuilder.toString)
  }

  def swap(index1: Int, index2: Int): Unit = {
    val temp = array(index1)

    array.update(index1, array(index2))
    array.update(index2, temp)
  }

  @tailrec
  def partition(left: Int, right: Int, pivot: Int, step: Step): Int = {
    printStep(left, right, pivot, step)

    if (array(left) < array(pivot)) {
      partition(left + 1, right, pivot, MoveLeft)
    } else if (array(right) > array(pivot)) {
      partition(left, right - 1, pivot, MoveRight)
    } else if (left >= right) {
      right
    } else {
      swap(left, right)

      if (pivot == left) {
        partition(left + 1, right - 1, right, Swap(array(left), array(right)))
      } else if (pivot == right) {
        partition(left + 1, right - 1, left, Swap(array(left), array(right)))
      } else {
        partition(left + 1, right - 1, pivot, Swap(array(left), array(right)))
      }
    }
  }

  def quickSort(left: Int, right: Int): Unit = {
    if (left < right) {
      val pivot = partition(left, right, (left + right) / 2, Initialize(left, right))

      quickSort(left, pivot)
      quickSort(pivot + 1, right)
    }
  }

  val array: ArrayBuffer[Int] = ArrayBuffer(args.map(arg => Integer.parseInt(arg.trim)): _*)
  val metaLine: ArrayBuffer[String] = new ArrayBuffer().appendAll((0 until array.length * 2 + 1).map(_ => " "))
  var steps = 0
  var swaps = 0

  require(array.forall(number => number >= 0 && number < 10), "All numbers must be in the range [0,9]")

  quickSort(0, array.size - 1)

}
