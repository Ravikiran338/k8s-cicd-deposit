/**
 * 
 */
package com.sci.services.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.davidmoten.rx.jdbc.ConnectionProvider;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.pool.NonBlockingConnectionPool;
import org.davidmoten.rx.jdbc.pool.Pools;

/**
 * @author mn259
 *
 */
public class DatabaseUtil {
	private static DatabaseUtil databaseUtil = null;

	private DatabaseUtil() {

	}

	public static DatabaseUtil getInstance() {
		if (databaseUtil == null) {
			databaseUtil = new DatabaseUtil();
		}
		return databaseUtil;
	}

	public Database getDatabase() {
		Database db = null;
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://182.74.133.92:3306/micro_banking_dev?user=sciitdev&password=welcome@1&autoReconnect=true&failOverReadOnly=false&maxReconnects=10");
			NonBlockingConnectionPool pool = Pools.nonBlocking()
					.maxPoolSize(Runtime.getRuntime().availableProcessors() * 5)
					.connectionProvider(ConnectionProvider.from(connection)).build();
			db = Database.from(pool, () -> {
				pool.close();
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return db;
	}
}
