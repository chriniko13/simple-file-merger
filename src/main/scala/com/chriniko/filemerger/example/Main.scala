package com.chriniko.filemerger.example

import java.io.File

import com.chriniko.filemerger.example.service.{FileMerger, Measurer}

object Main {

  def main(args: Array[String]): Unit = {

    implicit val t: (Long, Long) => Boolean = (x, y) => x != y

    Measurer.measureExecutionTime[Unit](
      FileMerger.merge(
        new File("src/main/resources/"),
        "part_.*".r,
        new File("src/main/resources/result.csv")
      )
    )

  }

}
