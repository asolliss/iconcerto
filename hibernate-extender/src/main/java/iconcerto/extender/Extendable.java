package iconcerto.extender;

/**
 * <b>Thread-safe
 * 
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 *
 */
public interface Extendable {

	void addBundle(ExtendedBundle bundle);
	
	void removeBundle(ExtendedBundle bundle);
	
}
