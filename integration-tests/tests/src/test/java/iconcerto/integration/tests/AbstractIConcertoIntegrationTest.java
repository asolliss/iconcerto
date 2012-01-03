package iconcerto.integration.tests;

import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.osgi.framework.Constants;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;

public abstract class AbstractIConcertoIntegrationTest extends AbstractConfigurableBundleCreatorTests {
	
	private ResourceBundle configuration = ResourceBundle.getBundle("configuration");
	
	public AbstractIConcertoIntegrationTest() {
		super();
	}

	public String getConfigurationValue(String key) {
		return configuration.getString(key);
	}	

	@Override
	protected Resource getTestingFrameworkBundlesConfiguration() {
		return new InputStreamResource(
				AbstractIConcertoIntegrationTest.class.getResourceAsStream("boot-bundles.properties"));
	}

	@Override
	protected String getPlatformName() {
		return EquinoxPlatform.class.getCanonicalName();
	}
	
	protected String[] getDynamicImportPackages() {
		return new String[0];
	}
	
	@Override
	protected Manifest getManifest() {
		Manifest mf = super.getManifest();
		Attributes attributes = mf.getMainAttributes();
		
		if (getDynamicImportPackages() != null &&
				getDynamicImportPackages().length > 0) {			
			StringBuilder regex = new StringBuilder();
			StringBuilder newDynamicImportPackage = new StringBuilder();
			
			for (String currentPackage: getDynamicImportPackages()) {
				regex.append('(');
				regex.append(currentPackage.replace(".", "\\."));				
				regex.append(")|");
				newDynamicImportPackage.append(currentPackage);
				newDynamicImportPackage.append(',');
			}
			
			regex.deleteCharAt(regex.length()-1);			
			
			String importPackage =
				attributes.getValue(Constants.IMPORT_PACKAGE)
					.replaceAll(regex.toString(), "")
					.replaceAll("(,{2,})", ",");
			
			if (importPackage.startsWith(",")) {
				importPackage = importPackage.substring(1, importPackage.length());
			}
			
			if (importPackage.endsWith(",")) {
				importPackage = importPackage.substring(0, importPackage.length()-1);
			}
						
			
			attributes.putValue(Constants.IMPORT_PACKAGE, importPackage);

			String dynamicImportPackage = attributes.getValue(Constants.DYNAMICIMPORT_PACKAGE);
			if (dynamicImportPackage != null && 
					dynamicImportPackage.length() > 0) {
				newDynamicImportPackage.append(dynamicImportPackage);
			}
			else {
				newDynamicImportPackage
					.deleteCharAt(newDynamicImportPackage.length()-1);
			}
			
			attributes.putValue(Constants.DYNAMICIMPORT_PACKAGE, newDynamicImportPackage.toString());
		}
		
		return mf;
	}
	
	@Override
	protected long getDefaultWaitTime() {
		return Long.valueOf(getConfigurationValue("defaul.wait.time"));
	}
	
}
