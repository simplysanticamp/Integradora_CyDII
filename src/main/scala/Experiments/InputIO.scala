package Experiments

import Algorithms.ListUtils
import java.io.{BufferedReader, File, FileReader, PrintWriter}
import scala.annotation.tailrec

object InputIO {

  /**
   * Tells whether a file exists.
   *
   * @param path the file path
   * @return true if the file exists
   */
  def exists(path: String): Boolean = new File(path).exists()

  /**
   * Writes one integer per line.
   *
   * @param writer the destination
   * @param values the integers still to write
   */
  @tailrec
  private def writeInts(writer: PrintWriter, values: List[Int]): Unit = values match {
    case Nil => ()
    case head :: tail =>
      writer.println(head)
      writeInts(writer, tail)
  }

  /**
   * Writes one point per line, as `x y`.
   *
   * @param writer the destination
   * @param points the points still to write
   */
  @tailrec
  private def writePoints(writer: PrintWriter, points: List[List[Int]]): Unit = points match {
    case Nil => ()
    case List(x, y) :: tail =>
      writer.println(s"$x $y")
      writePoints(writer, tail)
    case _ :: tail => writePoints(writer, tail)
  }

  /**
   * Reads one integer per line.
   *
   * @param reader the source
   * @param acc    integers read so far, in reverse order
   * @return the integers read, in reverse order
   */
  @tailrec
  private def readInts(reader: BufferedReader, acc: List[Int]): List[Int] = {
    val line = reader.readLine()
    if (line == null) acc else readInts(reader, line.trim.toInt :: acc)
  }

  /**
   * Reads one point per line.
   *
   * @param reader the source
   * @param acc    points read so far, in reverse order
   * @return the points read, in reverse order
   */
  @tailrec
  private def readPoints(reader: BufferedReader, acc: List[List[Int]]): List[List[Int]] = {
    val line = reader.readLine()
    if (line == null) acc
    else line.trim.split(' ') match {
      case Array(x, y) => readPoints(reader, List(x.toInt, y.toInt) :: acc)
      case _ => readPoints(reader, acc)
    }
  }

  /**
   * Saves an integer list to a file, creating the folder if needed.
   *
   * @param path   the file path
   * @param values the integers to save
   */
  def saveInts(path: String, values: List[Int]): Unit = {
    new File(path).getParentFile.mkdirs()
    val writer = new PrintWriter(path)
    try writeInts(writer, values)
    finally writer.close()
  }

  /**
   * Saves a list of points to a file, creating the folder if needed.
   *
   * @param path   the file path
   * @param points the points to save
   */
  def savePoints(path: String, points: List[List[Int]]): Unit = {
    new File(path).getParentFile.mkdirs()
    val writer = new PrintWriter(path)
    try writePoints(writer, points)
    finally writer.close()
  }

  /**
   * Loads an integer list from a file.
   *
   * @param path the file path
   * @return the integers, in file order
   */
  def loadInts(path: String): List[Int] = {
    val reader = new BufferedReader(new FileReader(path))
    try ListUtils.reverse(readInts(reader, Nil))
    finally reader.close()
  }

  /**
   * Loads a list of points from a file.
   *
   * @param path the file path
   * @return the points, in file order
   */
  def loadPoints(path: String): List[List[Int]] = {
    val reader = new BufferedReader(new FileReader(path))
    try ListUtils.reverse(readPoints(reader, Nil))
    finally reader.close()
  }
}
