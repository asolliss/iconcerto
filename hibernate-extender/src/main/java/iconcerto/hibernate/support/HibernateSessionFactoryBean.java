package iconcerto.hibernate.support;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class HibernateSessionFactoryBean extends AnnotationSessionFactoryBean {

	private SessionFactory sessionFactoryProxy;
	private Lock sessionFactoryAccessLock = 
			new ReentrantLock(true);
	
	/**
	 * Set a class loader with support of mapping classes 
	 * @param classLoader
	 */
	public void setClassLoader(ClassLoader classLoader) {
		setBeanClassLoader(classLoader);
	}

	@Override
	public void afterPropertiesSet() throws Exception {		
		super.afterPropertiesSet();
		//initialization
		sessionFactoryProxy = createSessionFactoryProxy();
	}

	@Override
	public SessionFactory getObject() {
		try {
			recreateSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sessionFactoryProxy;
	}
	
	protected void recreateSessionFactory() throws Exception {
		boolean locked = false;
		do {
			try {
				locked = sessionFactoryAccessLock.tryLock(3000, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e) {}
		}
		while (!locked);
		
		try {
			super.destroy();		
			super.afterPropertiesSet();		
		}
		finally {
			sessionFactoryAccessLock.unlock();
		}
	}
	
	protected SessionFactory createSessionFactoryProxy() throws InstantiationException, IllegalAccessException {
		ProxyFactory sessionFactoryProxyFactory = new ProxyFactory();
		sessionFactoryProxyFactory.setInterfaces(new Class[] {SessionFactory.class});
		
		sessionFactoryProxyFactory.setFilter(new MethodFilter() {
			public boolean isHandled(Method m) {
				// ignore finalize()
				return !m.getName().equals("finalize");				
			}
		});
		
		Class sessionFactoryProxyClass = sessionFactoryProxyFactory.createClass();
		MethodHandler mi = new MethodHandler() {
			public Object invoke(Object self, Method thisMethod, Method proceed,
					Object[] args) throws Throwable {
				Object result = null;
				
				boolean locked = false;
				do {
					try {
						locked = sessionFactoryAccessLock.tryLock(3000, TimeUnit.MILLISECONDS);
					}
					catch (InterruptedException e) {}
				}
				while (!locked);
				
				try {
					// execute the original method of SessionFactory
					result = proceed.invoke(getSessionFactory(), args);
				}
				finally {
					sessionFactoryAccessLock.unlock();
				}
				
				return result;
			}
		};
		
		SessionFactory sessionFactory = (SessionFactory)sessionFactoryProxyClass.newInstance();
		((ProxyObject)sessionFactory).setHandler(mi);
		
		return sessionFactory;
	}
	
}
