package iconcerto.integration.tests.database.impl;


import java.sql.Connection;
import java.sql.DriverManager;

import org.hsqldb.server.Server;

public class HsqldbAdapter implements DatabaseAdapter {
	
	private volatile Server server;
	private volatile boolean started = false;

	@Override
	public void start() throws Exception {		
		server = new Server();
		server.setDaemon(true);
		server.setDatabaseName(0, "test");
		server.setDatabasePath(0, "mem:test");
		server.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				if (server != null) {
					server.stop();
				}
			}
			
		});
		
		started = true;
	}

	@Override
	public void stop() throws Exception {
		
	}

	@Override
	public Connection getConnection() throws Exception {
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		return DriverManager.getConnection("jdbc:hsqldb:hsqls://localhost/test", "SA", "");
	}

	@Override
	public String getDatabaseName() {
		return "HSQLDB";
	}

}
