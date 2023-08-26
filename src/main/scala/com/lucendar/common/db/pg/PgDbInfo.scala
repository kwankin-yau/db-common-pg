package com.lucendar.common.db.pg

import org.postgresql.PGProperty

case class PgDbInfo(host: String, port: Int, dbName: String)

object PgDbInfo {
  def parse(pgJdbcUrl: String): PgDbInfo = {
    val props = org.postgresql.Driver.parseURL(pgJdbcUrl, null)
    val host = props.getProperty(PGProperty.PG_HOST.getName)
    val port = props.getProperty(PGProperty.PG_PORT.getName).toInt
    val dbName = props.getProperty(PGProperty.PG_DBNAME.getName)

    PgDbInfo(host, port, dbName)
  }
}
