package iconcerto.extender;


import java.util.concurrent.BlockingQueue;

public interface BundleConsumer extends Runnable {

	void run();
	
	void initialize(
			BlockingQueue<ExtendedBundle> newBundles
			);
	
}
