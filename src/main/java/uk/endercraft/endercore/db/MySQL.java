package uk.endercraft.endercore.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MySQL {

	private MysqlDataSource dataSource;
	private Connection conn = null;

	private String user;
	private String password;
	private String server;
	private int port;
	private String databaseName;

	public MySQL(String user, String password, String server, int port, String databaseName) {
		this.user = user;
		this.password = password;
		this.server = server;
		this.port = port;
		this.databaseName = databaseName;
		dataSource = attach(new MysqlDataSource());
	}

	public Connection getConnection() {
		if (conn == null)
			try {
				conn = dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		try {
			if (conn.isClosed()) {
				dataSource = attach(new MysqlDataSource());
				conn = dataSource.getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void disconnect() {
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private MysqlDataSource attach(MysqlDataSource ds) {
		ds.setUser(user);
		ds.setPassword(password);
		ds.setServerName(server);
		ds.setPort(port);
		ds.setDatabaseName(databaseName);
		return ds;
	}

}
