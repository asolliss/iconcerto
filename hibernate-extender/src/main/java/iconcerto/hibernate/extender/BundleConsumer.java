package iconcerto.hibernate.extender;

import java.util.concurrent.BlockingQueue;

public interface BundleConsumer extends Runnable {

	void run();
	
	void initialize(
			BlockingQueue<ExtendedBundle> newBundles,
			String[] mappingPaths
			);
	
}
