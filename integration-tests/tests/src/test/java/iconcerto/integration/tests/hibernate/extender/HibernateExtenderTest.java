package iconcerto.integration.tests.hibernate.extender;

import iconcerto.integration.tests.AbstractIConcertoIntegrationTest;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.osgi.framework.ServiceReference;
import org.springframework.transaction.support.ResourceTransactionManager;

public class HibernateExtenderTest extends AbstractIConcertoIntegrationTest {
	
	@Override
	protected String[] getTestBundlesNames() {
		return new String[]
				{					
					"iconcerto,hibernate-extender,0.0.1-SNAPSHOT"				
				};
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void testGetDataSource() {
		ServiceReference dataSourceServiceReference =
				bundleContext.getServiceReference(DataSource.class.getCanonicalName());
		assertNotNull(dataSourceServiceReference);
		
		try {			
			DataSource dataSource =
					(DataSource) bundleContext.getService(dataSourceServiceReference);			
			
			assertNotNull(dataSource);			
		}
		finally {
			bundleContext.ungetService(dataSourceServiceReference);
		}		
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void testGetSessionFactory() {
		ServiceReference sessionFactoryServiceReference =
				bundleContext.getServiceReference(SessionFactory.class.getCanonicalName());
		assertNotNull(sessionFactoryServiceReference);
		
		try {			
			SessionFactory sessionFactory =
					(SessionFactory) bundleContext.getService(sessionFactoryServiceReference);
			
			assertNotNull(sessionFactory);			
		}
		finally {
			bundleContext.ungetService(sessionFactoryServiceReference);
		}		
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void testGetTransactionManager() {
		ServiceReference transactionManagerServiceReference =
				bundleContext.getServiceReference(ResourceTransactionManager.class.getCanonicalName());
		assertNotNull(transactionManagerServiceReference);
		
		try {			
			ResourceTransactionManager transactionManager =
					(ResourceTransactionManager) bundleContext.getService(transactionManagerServiceReference);
			
			assertNotNull(transactionManager);			
		}
		finally {
			bundleContext.ungetService(transactionManagerServiceReference);
		}		
	}

	@Override
	protected long getDefaultWaitTime() {
		return 600L;
	}	
	
}
