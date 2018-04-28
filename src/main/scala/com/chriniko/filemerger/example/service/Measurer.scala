package com.chriniko.filemerger.example.service

import java.util.concurrent.TimeUnit

object Measurer {

  def measureExecutionTime[T](code: => T): T = {

    val startTime = System.nanoTime()
    val res = code
    val stopTime = System.nanoTime() - startTime
    val executionTimeInMillis = TimeUnit.MILLISECONDS.convert(stopTime, TimeUnit.NANOSECONDS)
    println(s"executionTimeInMillis = $executionTimeInMillis ms")
    res
  }

}
