package util

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource
import groovy.sql.Sql

/**
 * Created by IntelliJ IDEA.
 * User: dendiz
 * Date: Jun 2, 2009
 * Time: 8:43:35 PM
 * To change this template use File | Settings | File Templates.
 */

public class database {
	static instance
	static conf = new ConfigSlurper().parse(new File('/etc/tobo.properties').toURL())
	static getInstance() {
		if (instance == null ) {
			def source = new MysqlConnectionPoolDataSource();
			source.user = conf.dbuser
			source.password = conf.dbpassword
			source.serverName = conf.dbhost
			source.port = 3306
			source.databaseName = conf.dbname
			instance = new Sql(source)
		}
		return instance
	}
}