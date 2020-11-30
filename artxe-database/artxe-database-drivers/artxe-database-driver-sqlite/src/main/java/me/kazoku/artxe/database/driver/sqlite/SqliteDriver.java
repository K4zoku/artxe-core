package me.kazoku.artxe.database.driver.sqlite;

import me.kazoku.artxe.database.general.LocalDriver;
import me.kazoku.artxe.database.general.Setting;

import java.io.File;
import java.nio.file.Paths;

/**
 * A driver for SQLite
 */
public class SqliteDriver extends LocalDriver {
  public SqliteDriver() {
    super();
  }

  public SqliteDriver(File folder) {
    super(folder);
  }

  @Override
  public String getClassName() {
    return "org.sqlite.JDBC";
  }

  @Override
  public String convertURL(Setting setting) {
    return "jdbc:sqlite:" + Paths.get(getFolder().getAbsolutePath(), setting.getDatabaseName() + ".db").toString();
  }
}
