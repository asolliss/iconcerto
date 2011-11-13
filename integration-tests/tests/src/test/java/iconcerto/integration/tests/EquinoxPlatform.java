package iconcerto.integration.tests;

import java.util.Properties;

public class EquinoxPlatform extends
		org.springframework.osgi.test.platform.EquinoxPlatform {

	@Override
	public Properties getConfigurationProperties() {
		Properties properties = super.getConfigurationProperties();
		properties.setProperty("eclipse.consoleLog", "true");
		return properties;
	}

}
