package iconcerto.hibernate.extender;

import iconcerto.extender.BundleConsumer;
import iconcerto.extender.ExtendedBundle;
import iconcerto.extender.Extendable;
import iconcerto.extender.ExtendedBundle.Actions;
import iconcerto.extender.ExtenderClassLoader;

import java.util.concurrent.BlockingQueue;

import org.hibernate.SessionFactory;

/**
 * 
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 *
 */
public class SessionFactoryCreator implements BundleConsumer {
	
	private volatile Extendable extendable;
	private volatile ExtenderClassLoader extenderClassLoader;
	private volatile BlockingQueue<ExtendedBundle> newBundles;
	
	public Extendable getExtendable() {
		return extendable;
	}

	public void setExtendable(Extendable extendable) {
		this.extendable = extendable;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		if (sessionFactory instanceof Extendable) {		
			extendable = (Extendable) sessionFactory;
		}
	}

	public ExtenderClassLoader getExtenderClassLoader() {
		return extenderClassLoader;
	}

	public void setExtenderClassLoader(ExtenderClassLoader extenderClassLoader) {
		this.extenderClassLoader = extenderClassLoader;
	}

	@Override
	public void run() {
		
		ExtendedBundle bundle = null;
		
		while (!Thread.currentThread().isInterrupted()) {
			
			try {
				bundle = newBundles.take();
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
			
			if (!bundle.isValidForExtending()) continue;
			
			if (Actions.ADDING.equals(bundle.getAction())) {
				addBundle(bundle);
			}
			else if (Actions.REMOVING.equals(bundle.getAction())) {
				removeBundle(bundle);
			}
			
		}
		
	}

	@Override
	public void initialize(BlockingQueue<ExtendedBundle> newBundles) {
		this.newBundles = newBundles;
	}
	
	private void addBundle(ExtendedBundle bundle) {				
		bundle.extend();		
		extenderClassLoader.addBundle(bundle);
		extendable.addBundle(bundle);
		bundle.markAsCompleted();
	}
	
	private void removeBundle(ExtendedBundle bundle) {
		bundle.extend();		
		extendable.removeBundle(bundle);
		extenderClassLoader.removeBundle(bundle);
		bundle.markAsCompleted();
	}

}
