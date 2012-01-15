package iconcerto.integration.tests.database.impl;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;

import org.hsqldb.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HsqldbAdapter implements DatabaseAdapter {
	
	private final static Logger logger = LoggerFactory.getLogger(HsqldbAdapter.class);
	
	private volatile Server server;

	@Override
	public void start() throws Exception {		
		server = new Server();
		server.setDaemon(true);
		server.setDatabaseName(0, "test");
		server.setDatabasePath(0, "mem:test");
		//integration with slf4j
		server.setLogWriter(new PrintWriter(new Writer() {
			
			StringBuilder sb = new StringBuilder();
			
			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {
				if (! logger.isDebugEnabled()) return;
					
				sb.append(cbuf, off, len);			
			}
			
			@Override
			public void flush() throws IOException {
				if (! logger.isDebugEnabled()) return;
				
				logger.debug(sb.toString().replace('\n', ' '));
				sb.delete(0, sb.length()-1);				
			}
			
			@Override
			public void close() throws IOException {
				flush();
			}
		}));
		server.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				if (server != null) {
					server.stop();
				}
			}
			
		});
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
