package com.lucendar.common.db.pg

import java.io.{BufferedReader, InputStreamReader}
import java.util
import scala.util.Using

trait BackupRunnerCallback {

  /**
   *
   * @param exitCode Exit code of `pg_dump` command.
   * @param errors   Error messages. null if no error.
   */
  def backupComplete(exitCode: Int, errors: java.util.List[String]): Unit
}

class BackupRunner(
                    pgDbInfo      : PgDbInfo,
                    userName      : String,
                    password      : String,
                    backupFileName: String,
                    callback      : BackupRunnerCallback
                  ) extends Runnable {
  override def run(): Unit = {
    val pb = new ProcessBuilder(
      "pg_dump",
      "--host", pgDbInfo.host,
      "--port", pgDbInfo.port.toString,
      "--username", userName,
      "--no-password",
      "--format", "custom",
      "--blobs",
      "--file", backupFileName,
      pgDbInfo.dbName
    )

    val env = pb.environment()
    env.put("PGPASSWORD", password)
    val p = pb.start()

    val errors = new util.ArrayList[String]()
    Using(new BufferedReader(new InputStreamReader(p.getErrorStream))) {
      reader =>
        var ln = reader.readLine()
        while (ln != null) {
          errors.add(ln)
          ln = reader.readLine()
        }
    }

    p.waitFor()

    callback.backupComplete(p.exitValue(), if (errors.isEmpty) null else errors)
  }

}
