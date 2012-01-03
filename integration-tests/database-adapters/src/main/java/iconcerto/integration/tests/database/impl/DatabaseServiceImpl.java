package iconcerto.integration.tests.database.impl;

import iconcerto.integration.tests.database.api.DatabaseService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseServiceImpl implements DatabaseService {

	private final DatabaseAdapter[] databaseAdapters = new DatabaseAdapter[] {
		new HsqldbAdapter()	
	};
	
	private volatile DatabaseAdapter databaseAdapter;
	
	@Override
	public void initialize(String databaseName) {
		if (databaseAdapter != null) {
			try {
				databaseAdapter.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (DatabaseAdapter currentDatabaseAdapter: databaseAdapters) {
			if (currentDatabaseAdapter
					.getDatabaseName()
					.equalsIgnoreCase(databaseName)) {
				databaseAdapter = 
						currentDatabaseAdapter;
				break;
			}
		}
		
		try {
			databaseAdapter.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void shutdown() {
		if (databaseAdapter == null) return;
		
		try {
			databaseAdapter.stop();
			databaseAdapter = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Connection getConnection() {
		 try {
		      Class.forName("org.hsqldb.jdbc.JDBCDriver" );
		  } catch (Exception e) {
		      System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
		      e.printStackTrace();
		  }
		
		try {
			return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/test", "SA", "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return null;
	}

}
