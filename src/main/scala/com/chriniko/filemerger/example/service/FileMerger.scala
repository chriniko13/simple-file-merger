package com.chriniko.filemerger.example.service


import java.io.{File, PrintWriter}

import scala.io.Source
import scala.util.matching.Regex

object FileMerger {

  def merge(dir: File, filenameSatisfier: Regex, resultFile: File)
           (implicit tester: (Long, Long) => Boolean): Unit = {

    val splittedFilenames = getListOfFiles(dir, filenameSatisfier).sortBy(_.getName)
    println(s"splittedFilenames: $splittedFilenames")

    val allContent: List[String] = mergeSplittedContent(splittedFilenames)

    val rowsBefore = Source.fromFile(resultFile).bufferedReader().lines().count()
    val rowsAfter = allContent.count { s => true }

    writeMergedContent(resultFile, allContent)

    if (tester(rowsBefore, rowsAfter)) {
      throw new IllegalStateException()
    }

  }

  private def writeMergedContent(resultFile: File, allContent: List[String]): Unit = {
    val writer = new PrintWriter(resultFile)
    allContent.foreach(auction => {
      writer.write(auction)
      writer.write("\n")
    })
    writer.close()
  }

  private def mergeSplittedContent(splittedFilenames: List[File]): List[String] = {
    splittedFilenames
      .map(splittedFilename => {
        val bufferedSource = Source.fromFile(splittedFilename)
        val lines = bufferedSource.getLines.toList
        bufferedSource.close
        lines
      })
      .collect {
        case v => v
      }
      .flatten
  }

  def countLinesOfFile(dir: File): Int = {
    val bufferedSource = Source.fromFile(dir)
    bufferedSource.getLines.toList.size
  }

  def getListOfFiles(dir: File, validation: Regex): List[File] = {
    dir.listFiles
      .filter(_.isFile)
      .toList
      .filter { file =>
        validation.findFirstIn(file.getName) match {
          case Some(_) => true
          case _ => false
        }
      }
  }

}
