/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ***************************************************************************** */
package com.lucendar.common.db.pg

import com.lucendar.common.db.types.{SqlDialect, SqlDialects}

import java.sql.Connection
import scala.util.Using

object SqlDialect_Pg extends SqlDialect {
  override def id: String = SqlDialects.POSTGRESQL

  override def stringValueLiteral(s: String): String =
    org.postgresql.core.Utils.escapeLiteral(null, s, true).toString

  override def setConstraintsDeferred(conn: Connection): Unit = {
    Using.resource(conn.createStatement()) { st =>
      st.execute("SET CONSTRAINTS ALL DEFERRED")
    }
  }

  override def supportSingleStatementPagination: Boolean = true
}
