package iconcerto.integration.tests.hibernate.extender;

import java.io.IOException;

import iconcerto.integration.tests.AbstractIConcertoIntegrationTest;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
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
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void testStartingValidBundleForExtendingAfterStartingHibernateExtender() throws BundleException, IOException {
		Bundle bundle = bundleContext.installBundle(
				locateBundle("iconcerto.integration-tests-bundles,hibernate-extended-test-bundle,0.0.1-SNAPSHOT").getURI().toASCIIString()
				);				
		
		ServiceReference sessionFactoryServiceReference =
				bundleContext.getServiceReference(SessionFactory.class.getCanonicalName());

		try {
			SessionFactory sessionFactory =
					(SessionFactory) bundleContext.getService(sessionFactoryServiceReference);
			
			assertNotNull(sessionFactory);

			bundle.start();
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					assertNull(e);
				}																

				ClassMetadata testEntity1ClassMetadata = sessionFactory.getClassMetadata("iconcerto.entities.TestEntity1");			
				assertNotNull(testEntity1ClassMetadata);

				ClassMetadata testEntity2ClassMetadata = sessionFactory.getClassMetadata("iconcerto.entities.TestEntity2");			
				assertNotNull(testEntity2ClassMetadata);

			}
			finally {
				bundle.uninstall();
			}

			//bundle with entities have uninstalled			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				assertNull(e);
			}						

			ClassMetadata testEntity1ClassMetadata = sessionFactory.getClassMetadata("iconcerto.entities.TestEntity1");			
			assertNull(testEntity1ClassMetadata);

			ClassMetadata testEntity2ClassMetadata = sessionFactory.getClassMetadata("iconcerto.entities.TestEntity2");			
			assertNull(testEntity2ClassMetadata);			
			
		}
		finally {
			bundleContext.ungetService(sessionFactoryServiceReference);
		}
	}
	
	public void testStartingValidBundleForExtendingBeforeStartingHibernateExtender() {
		
	}

	@Override
	protected long getDefaultWaitTime() {
		return 600L;
	}	
	
}
