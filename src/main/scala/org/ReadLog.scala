package org
import scala.io.Source
import scala.util.matching.Regex


object ReadLog {
  def main(args: Array[String]): Unit = {
    val logFilePath: String = "D:\\UVT\\simestre 2\\pi-auth-api.log"
    val logFile = Source.fromFile(logFilePath)

    var exceptionCount = 0
    var errorCount = 0
    var warningCount = 0
    var infoCount = 0
    var requestCount = 0
    var totalResponseTime = 0L

    // Regex pour extraire les temps de réponse (par exemple : "responseTime=123ms")
    val responseTimePattern: Regex = """responseTime=(\d+)ms""".r

    try {
      for (line <- logFile.getLines()) {
        val lowerCaseLine = line.toLowerCase

        if (lowerCaseLine.contains("exception")) {
          exceptionCount += 1
        } else if (lowerCaseLine.contains("error")) {
          errorCount += 1
        } else if (lowerCaseLine.contains("warn") || lowerCaseLine.contains("warning")) {
          warningCount += 1
        } else if (lowerCaseLine.contains("info")) {
          infoCount += 1
        }

        // Suivi des requêtes utilisateur
        if (lowerCaseLine.contains("request")) {
          requestCount += 1
        }

        // Calcul des temps de réponse
        for (matchResult <- responseTimePattern.findAllMatchIn(line)) {
          val responseTime = matchResult.group(1).toLong
          totalResponseTime += responseTime
        }
      }

      // Calcul du temps de réponse moyen
      val averageResponseTime = if (requestCount > 0) totalResponseTime / requestCount else 0

      // Affichage des résultats
      println("| Type                | Count |")
      println("|---------------------|-------|")
      println(f"| Exception           | $exceptionCount%5d |")
      println(f"| Error               | $errorCount%5d |")
      println(f"| Warning             | $warningCount%5d |")
      println(f"| Info                | $infoCount%5d |")
      println(f"| User Requests       | $requestCount%5d |")
      println(f"| Average Response Time | $averageResponseTime%5d ms |")

    } finally {
      logFile.close()
    }
  }
}
