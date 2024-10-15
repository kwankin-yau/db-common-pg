/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ***************************************************************************** */
package com.lucendar.common.db.pg

import com.lucendar.common.db.types.{ServerVer, SqlDialect, SqlDialects}

import java.sql.Connection
import scala.util.Using

object SqlDialect_Pg extends SqlDialect {
  override def id: String = SqlDialects.POSTGRESQL

  /**
   * 取数据库服务端版本号
   *
   * @param conn 连接对象
   * @return 数据库服务端版本号
   */
  override def getServerVer(conn: Connection): ServerVer = {
    Using.resource(conn.createStatement()) {st => {
       Using.resource(st.executeQuery("SHOW server_version")) {rs => {
         val v = rs.getString(0)
         ServerVer.parse(v)
       }}
    }}
  }

  override def stringValueLiteral(s: String): String =
    org.postgresql.core.Utils.escapeLiteral(null, s, true).toString

  override def setConstraintsDeferred(conn: Connection): Unit = {
    Using.resource(conn.createStatement()) { st =>
      st.execute("SET CONSTRAINTS ALL DEFERRED")
    }
  }

  override def supportSingleStatementPagination: Boolean = true

  SqlDialects.register(SqlDialect_Pg)
}
