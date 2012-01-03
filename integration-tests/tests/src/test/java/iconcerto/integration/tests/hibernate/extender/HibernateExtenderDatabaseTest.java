package iconcerto.integration.tests.hibernate.extender;

import iconcerto.integration.tests.database.AbstractIConcertoDatabaseIntegrationTest;
import iconcerto.service.api.EntityService;

import java.sql.Connection;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class HibernateExtenderDatabaseTest extends
		AbstractIConcertoDatabaseIntegrationTest {
	
	public void testEntityServiceWithOneWritingTransaction() throws Exception {
		Connection conn = getConnection();
		conn.createStatement().execute(
				"CREATE TABLE parent_entities (id integer PRIMARY KEY, name varchar(1024))"
				);
		conn.commit();
		
		Bundle valiDaoBundle = bundleContext.installBundle(
				locateBundle("iconcerto.integration-tests-bundles,valid-dao-bundle,0.0.1-SNAPSHOT").getURI().toASCIIString()
				);
		
		ServiceReference<EntityService> entityServiceReference = null;
				
		try {
			valiDaoBundle.start();
			
			waitOnContextCreation(valiDaoBundle.getSymbolicName());
			
			entityServiceReference =
					bundleContext.getServiceReference(EntityService.class);
			
			assertNotNull(entityServiceReference);
			
			EntityService entityService =
					bundleContext.getService(entityServiceReference);
			
			assertNotNull(entityService);
			
			entityService.createParentEntity(1, "first");
		}
		finally {
			if (entityServiceReference != null) {
				bundleContext.ungetService(entityServiceReference);
			}
			valiDaoBundle.uninstall();
		}
		
	}
	
	public void testTTT() {
		
	}
	
	@Override
	protected String[] getDynamicImportPackages() {
		return new String[] {"iconcerto.service.api", "iconcerto.entities", "org.hsqldb.jdbc"};
	}

	@Override
	protected String[] getTestBundlesNames() {
		return new String[]
				{
					"iconcerto,hibernate-extender,"+getConfigurationValue("iconcerto.version")
				};
	}
	
}
