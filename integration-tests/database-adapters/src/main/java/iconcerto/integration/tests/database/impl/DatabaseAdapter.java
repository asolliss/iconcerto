package iconcerto.integration.tests.database.impl;

import java.sql.Connection;

public interface DatabaseAdapter {

	void start() throws Exception;
	
	void stop() throws Exception;
	
	Connection getConnection() throws Exception;
	
	String getDatabaseName();
	
}
