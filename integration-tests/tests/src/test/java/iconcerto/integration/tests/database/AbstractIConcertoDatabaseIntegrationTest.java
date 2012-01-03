package iconcerto.integration.tests.database;

import iconcerto.integration.tests.AbstractIConcertoIntegrationTest;
import iconcerto.integration.tests.database.api.DatabaseService;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractIConcertoDatabaseIntegrationTest 
	extends AbstractIConcertoIntegrationTest {

	@Override
	protected void postProcessBundleContext(BundleContext context)
			throws Exception {
		initializeDatabaseService(context);
		super.postProcessBundleContext(context);
	}
	
	protected Connection getConnection() throws Exception {				
		ServiceReference databaseServiceReference = 
				bundleContext.getServiceReference(DatabaseService.class.getCanonicalName());
		Object databaseService = bundleContext.getService(databaseServiceReference);
		
		Connection conn = null;
		
		try {
			/*Reflection API is used 
			 * to avoid the ClassCastException problem 
			 * with several ClassLoaders*/
			conn = (Connection) 
					databaseService
						.getClass()
						.getDeclaredMethod("getConnection")
						.invoke(databaseService);
		}
		finally {
			bundleContext.ungetService(databaseServiceReference);
		}
		
		return conn;
	}
	
	private void initializeDatabaseService(BundleContext context) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		waitOnContextCreation(
				context, 
				"iconcerto.integration-tests-bundles.database-adapters", 
				getDefaultWaitTime());
		
		ServiceReference databaseServiceReference = 
				context.getServiceReference(DatabaseService.class.getCanonicalName());
		Object databaseService = context.getService(databaseServiceReference);
		
		try {
			String databaseName = System.getProperty("databaseName", "HSQLDB");
			/*Reflection API is used 
			 * to avoid the ClassCastException problem 
			 * with several ClassLoaders*/
			databaseService
				.getClass()
				.getDeclaredMethod("initialize", String.class)
				.invoke(databaseService, databaseName);			
		}
		finally {
			context.ungetService(databaseServiceReference);
		}		
	}

}
