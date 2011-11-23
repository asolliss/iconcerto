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
	
	public void testGetDataSource() {
		ServiceReference<DataSource> dataSourceServiceReference =
				bundleContext.getServiceReference(DataSource.class);
		assertNotNull(dataSourceServiceReference);
		
		try {			
			DataSource dataSource =
					bundleContext.getService(dataSourceServiceReference);			
			
			assertNotNull(dataSource);			
		}
		finally {
			bundleContext.ungetService(dataSourceServiceReference);
		}		
	}
	
	public void testGetSessionFactory() {
		ServiceReference<SessionFactory> sessionFactoryServiceReference =
				bundleContext.getServiceReference(SessionFactory.class);
		assertNotNull(sessionFactoryServiceReference);
		
		try {			
			SessionFactory sessionFactory =
					bundleContext.getService(sessionFactoryServiceReference);
			
			assertNotNull(sessionFactory);			
		}
		finally {
			bundleContext.ungetService(sessionFactoryServiceReference);
		}		
	}
	
	public void testGetTransactionManager() {
		ServiceReference<ResourceTransactionManager> transactionManagerServiceReference =
				bundleContext.getServiceReference(ResourceTransactionManager.class);
		assertNotNull(transactionManagerServiceReference);
		
		try {			
			ResourceTransactionManager transactionManager =
					bundleContext.getService(transactionManagerServiceReference);
			
			assertNotNull(transactionManager);			
		}
		finally {
			bundleContext.ungetService(transactionManagerServiceReference);
		}		
	}
	

	public void testStartingValidBundleForExtendingAfterStartingHibernateExtender() throws BundleException, IOException {
		Bundle bundleWithEntities = bundleContext.installBundle(
				locateBundle("iconcerto.integration-tests-bundles,bundle-with-entities,0.0.1-SNAPSHOT").getURI().toASCIIString()
				);				
		
		ServiceReference<SessionFactory> sessionFactoryServiceReference =
				bundleContext.getServiceReference(SessionFactory.class);

		try {
			SessionFactory sessionFactory =
					bundleContext.getService(sessionFactoryServiceReference);
			
			assertNotNull(sessionFactory);

			bundleWithEntities.start();
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
				bundleWithEntities.uninstall();
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
	
	public void testStartingValidBundleForExtendingBeforeStartingHibernateExtender() throws BundleException, IOException {
		Bundle hibernateExtenderBundle = null;
		for (Bundle bundle: bundleContext.getBundles()) {
			if (bundle.getSymbolicName().contains("hibernate-extender")) {
				hibernateExtenderBundle = bundle;
				break;
			}
		}
		assertNotNull(hibernateExtenderBundle);
		
		hibernateExtenderBundle.stop();
		
		Bundle bundleWithEntities = bundleContext.installBundle(
				locateBundle("iconcerto.integration-tests-bundles,bundle-with-entities,0.0.1-SNAPSHOT").getURI().toASCIIString()
				);				
		
		ServiceReference<SessionFactory> sessionFactoryServiceReference = null;
		SessionFactory sessionFactory = null;
		
		try {			
			bundleWithEntities.start();
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					assertNull(e);
				}
				
				hibernateExtenderBundle.start();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					assertNull(e);
				}
				sessionFactoryServiceReference = 
						bundleContext.getServiceReference(SessionFactory.class);

				sessionFactory =
						bundleContext.getService(sessionFactoryServiceReference);
				
				assertNotNull(sessionFactory);

				ClassMetadata testEntity1ClassMetadata = sessionFactory.getClassMetadata("iconcerto.entities.TestEntity1");			
				assertNotNull(testEntity1ClassMetadata);

				ClassMetadata testEntity2ClassMetadata = sessionFactory.getClassMetadata("iconcerto.entities.TestEntity2");			
				assertNotNull(testEntity2ClassMetadata);

			}
			finally {
				bundleWithEntities.uninstall();
			}

			//bundle with entities have uninstalled			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				assertNull(e);
			}						

			ClassMetadata testEntity1ClassMetadata = sessionFactory.getClassMetadata("iconcerto.entities.TestEntity1");			
			assertNull(testEntity1ClassMetadata);

			ClassMetadata testEntity2ClassMetadata = sessionFactory.getClassMetadata("iconcerto.entities.TestEntity2");			
			assertNull(testEntity2ClassMetadata);			
			
		}
		finally {
			if (sessionFactoryServiceReference != null) {
				bundleContext.ungetService(sessionFactoryServiceReference);
			}
		}
	}

	@Override
	protected long getDefaultWaitTime() {
		return 600L;
	}	
	
}
