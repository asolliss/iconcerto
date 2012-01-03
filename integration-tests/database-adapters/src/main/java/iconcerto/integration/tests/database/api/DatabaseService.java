package iconcerto.integration.tests.database.api;

import java.sql.Connection;

public interface DatabaseService {

	void initialize(String databaseName);
	
	void shutdown();
	
	Connection getConnection();
	
}
