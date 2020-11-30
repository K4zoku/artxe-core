package me.kazoku.artxe.database.driver.mysql;

import me.kazoku.artxe.database.general.Driver;
import me.kazoku.artxe.database.general.Setting;

/**
 * A driver for MySQL
 */
public class MySqlDriver implements Driver {

    @Override
    public String getClassName() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String convertURL(Setting setting) {
        return "jdbc:mysql://" + setting.getHost() + ':' + setting.getPort() + '/' + setting.getDatabaseName();
    }
}
