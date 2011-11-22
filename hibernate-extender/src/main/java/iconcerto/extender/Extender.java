package iconcerto.extender;

import iconcerto.extender.ExtendedBundle.Actions;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

/**
 * This class contains implementation of extender pattern.
 * It watches all bundle events. 
 * If a bundle is valid for extending then the extender will wrap it into ExtendedBundle 
 * and will pass bundle to the BlockingQueue.
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 *
 */
public class Extender implements SynchronousBundleListener {
	
	private BundleContext bundleContext;
	private Executor executor;
	private BlockingQueue<ExtendedBundle> bundles = new LinkedBlockingQueue<ExtendedBundle>();		
	private BundleConsumer bundleConsumer;
	private ExtendedBundleFactory extendedBundleFactory;
	
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public BlockingQueue<ExtendedBundle> getBundles() {
		return bundles;
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

		try {
			bundles.put(extendedBundle);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}		
	}
	
	public void initialize() {
		getBundleConsumer().initialize(getBundles());
		
		getExecutor().execute(bundleConsumer);
		
		getBundleContext().addBundleListener(this);
		
		for (Bundle bundle: getBundleContext().getBundles()) {
			if (Bundle.ACTIVE != bundle.getState()) continue;
			
			ExtendedBundle extendedBundle = 
					extendedBundleFactory.newExtendedFactory(
							bundle, 
							Actions.ADDING
							);

			try {
				bundles.put(extendedBundle);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}
	
}
