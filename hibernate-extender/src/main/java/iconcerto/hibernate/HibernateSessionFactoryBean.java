package iconcerto.hibernate;

import iconcerto.extender.Extendable;
import iconcerto.hibernate.extender.HibernateBundle;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyFactory.ClassLoaderProvider;
import javassist.util.proxy.ProxyObject;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class HibernateSessionFactoryBean extends AnnotationSessionFactoryBean {

	private SessionFactory sessionFactoryProxy;
	private Lock sessionFactoryAccessLock = 
			new ReentrantLock(true);
	private ClassLoader classLoader;
	private Set<Class<?>> annotatedClasses = new CopyOnWriteArraySet<Class<?>>();
	
	/**
	 * Set a class loader with support of entity classes 
	 * @param classLoader
	 */
	public void setClassLoader(ClassLoader classLoader) {
		super.setBeanClassLoader(classLoader);
		this.classLoader = classLoader;		
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//Set default class loader for javassist Proxy Factory
		//classloader is our custom ClassLoader
		ProxyFactory.classLoaderProvider = new ClassLoaderProvider() {
			public ClassLoader get(ProxyFactory pf) {
				return HibernateSessionFactoryBean.this.classLoader;
			}
		};
		
		super.afterPropertiesSet();
		sessionFactoryProxy = createSessionFactoryProxy();
	}

	@Override
	public SessionFactory getObject() {
		return sessionFactoryProxy;
	}
	
	protected void recreateSessionFactory() throws Exception {
		sessionFactoryAccessLock.tryLock(3000, TimeUnit.MILLISECONDS);
		
		try {			
			super.destroy();		
			super.afterPropertiesSet();		
		}
		finally {
			sessionFactoryAccessLock.unlock();
		}
	}
	
	protected SessionFactory createSessionFactoryProxy() throws InstantiationException, IllegalAccessException, InterruptedException {

		ProxyFactory sessionFactoryProxyFactory = new ProxyFactory();
		sessionFactoryProxyFactory.setInterfaces(new Class[] {SessionFactory.class, Extendable.class});

		sessionFactoryProxyFactory.setFilter(new MethodFilter() {
			public boolean isHandled(Method m) {
				// ignore finalize()
				return !m.getName().equals("finalize");				
			}
		});		

		Class<?> sessionFactoryProxyClass = sessionFactoryProxyFactory.createClass();
		MethodHandler mi = new MethodHandler() {
			public Object invoke(Object self, Method thisMethod, Method proceed,
					Object[] args) throws Throwable {
				Object result = null;

				ClassLoader defaultClassLoader = Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(classLoader);
				try {
					//It's not a efficient implementation of method invocations
					// It must be re-implemented in the future
					if (thisMethod.getName().contains("addBundle")) {
						addBundle((HibernateBundle) args[0]);
					}
					else if (thisMethod.getName().contains("removeBundle")) {
						removeBundle((HibernateBundle) args[0]);						
					}
					else {
						sessionFactoryAccessLock.tryLock(3000, TimeUnit.MILLISECONDS);

						try {
							// execute the original method of SessionFactory
							result = thisMethod.invoke(getSessionFactory(), args);
						}
						finally {
							sessionFactoryAccessLock.unlock();
						}
					}
				}
				finally {
					Thread.currentThread().setContextClassLoader(defaultClassLoader);
				}

				return result;
			}
		};

		SessionFactory sessionFactory = (SessionFactory)sessionFactoryProxyClass.newInstance();
		((ProxyObject)sessionFactory).setHandler(mi);


		return sessionFactory;
	}
	
	private void addBundle(HibernateBundle bundle) throws Exception {
		for (String entityClassName: bundle.getEntityClassNames()) {
			annotatedClasses.add(classLoader.loadClass(entityClassName));
		}
		
		setAnnotatedClasses(annotatedClasses.toArray(new Class[annotatedClasses.size()]));
		recreateSessionFactory();
	}
	
	private void removeBundle(HibernateBundle bundle) throws Exception {
		for (Class<?> annotatedClass: annotatedClasses) {
			if (bundle.getEntityClassNames().contains(annotatedClass.getCanonicalName())) {
				annotatedClasses.remove(annotatedClass);
			}
		}
		
		setAnnotatedClasses(annotatedClasses.toArray(new Class[annotatedClasses.size()]));
		recreateSessionFactory();
	}
	
}
