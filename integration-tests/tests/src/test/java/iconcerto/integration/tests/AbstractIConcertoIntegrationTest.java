package iconcerto.integration.tests;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;

public abstract class AbstractIConcertoIntegrationTest extends AbstractConfigurableBundleCreatorTests {
	
	@Override
	protected Resource getTestingFrameworkBundlesConfiguration() {
		return new InputStreamResource(
				AbstractIConcertoIntegrationTest.class.getResourceAsStream("boot-bundles.properties"));
	}

	@Override
	protected String getPlatformName() {
		return EquinoxPlatform.class.getCanonicalName();
	}
	
	
}
