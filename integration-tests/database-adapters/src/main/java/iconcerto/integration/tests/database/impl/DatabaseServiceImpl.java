package iconcerto.integration.tests.database.impl;

import iconcerto.integration.tests.database.api.DatabaseService;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseServiceImpl implements DatabaseService {
	
	private final static Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);

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
				logger.error(e.getMessage(), e);
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
			logger.error(e.getMessage(), e);
		}		
	}

	@Override
	public void shutdown() {
		if (databaseAdapter == null) return;
		
		try {
			databaseAdapter.stop();
			databaseAdapter = null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public Connection getConnection() {
		try {
			return databaseAdapter.getConnection();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		};
		return null;
	}

}
