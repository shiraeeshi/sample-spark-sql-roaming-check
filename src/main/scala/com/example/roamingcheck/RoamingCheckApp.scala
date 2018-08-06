package com.example.roamingcheck

import java.util.Date

import org.apache.spark.sql.{Row, SparkSession}

object RoamingCheckApp {
  import Domain._

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder
      .appName("Roaming Check")
      .config("spark.sql.crossJoin.enabled", "true")
      .getOrCreate()
    import spark.implicits._

    val toTuple = { r: Row =>
      (r.getString(0), r.getString(1), r.getString(2), r.getString(3))
    }

    val fromRowToConversation = toTuple.andThen(toConversation)

    val localRDD = spark.read.csv("./data/input/local.csv") map fromRowToConversation

    localRDD.createOrReplaceTempView("local")

    val roamingRDD = spark.read.csv("./data/input/roaming.csv") map fromRowToConversation

    roamingRDD.createOrReplaceTempView("roaming")

    val result = spark.sql("""
      SELECT roaming.a
      FROM local
      JOIN roaming
      ON local.b = roaming.b
      AND abs(local.duration - roaming.duration) < 11
      AND abs(unix_timestamp(local.startTime) - unix_timestamp(roaming.startTime)) < 60
      WHERE roaming.a <> local.a
      """)

    result.write.csv(s"./data/output-${(new Date).getTime}")

  }

}

object Domain {
  case class Conversation(a: String, b: String, startTime: String, duration: Long)
  def toConversation(t: (String, String, String, String)): Domain.Conversation = t match {
    case (a, b, st, d) =>
      Conversation(a, b, st, d.toLong)
  }
}
