package iconcerto.hibernate.extender;

import iconcerto.hibernate.extender.ExtendedBundle.Actions;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

/**
 * Hibernate extender. This class contains implementation of extender pattern.
 * It watches all bundle events. 
 * If a bundle is valid for extending then the extender will wrap it into ExtendedBundle 
 * and will pass bundle to the BlockingQueue.
 * @author Ivan Pogudin
 *
 */
public class Extender implements SynchronousBundleListener {
	
	private Executor executor;
	private BlockingQueue<ExtendedBundle> bundles = new LinkedBlockingQueue<ExtendedBundle>();	
	private String[] mappingPaths = {"META-INF/hibernate/*.properties"};
	private BundleConsumer bundleConsumer;
	private ExtendedBundleFactory extendedBundleFactory;
	
	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public BlockingQueue<ExtendedBundle> getBundles() {
		return bundles;
	}

	public void setBundles(BlockingQueue<ExtendedBundle> bundles) {
		this.bundles = bundles;
	}

	public String[] getMappingPaths() {
		return mappingPaths;
	}

	public void setMappingPaths(String[] mappingPaths) {
		this.mappingPaths = mappingPaths;
	}

	public BundleConsumer getBundleConsumer() {
		return bundleConsumer;
	}

	public ExtendedBundleFactory getExtendedBundleFactory() {
		return extendedBundleFactory;
	}

	public void setExtendedBundleFactory(ExtendedBundleFactory extendedBundleFactory) {
		this.extendedBundleFactory = extendedBundleFactory;
	}

	public void setBundleConsumer(BundleConsumer bundleConsumer) {
		this.bundleConsumer = bundleConsumer;
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		
		Actions	action = null;
		
		switch (event.getType()) {

		case BundleEvent.STARTED: 
			action = Actions.ADDING;
			break;
		case BundleEvent.STOPPING:
			action = Actions.REMOVING;
			break;
		};
		
		if (action == null) return;

		ExtendedBundle extendedBundle = 
				extendedBundleFactory.newExtendedFactory(
						event.getBundle(), 
						action
						);
		
		if (!extendedBundle.isValidForExtending()) return;

		while (true) {
			try {
				bundles.put(extendedBundle);
			} catch (InterruptedException e) {
				continue;
			}
			break;
		}

	}
	
	public void initialize() {
		bundleConsumer.initialize(bundles, mappingPaths);
		
		executor.execute(bundleConsumer);
	}
	
}
