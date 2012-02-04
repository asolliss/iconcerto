package iconcerto.hibernate;

import iconcerto.extender.Extendable;
import iconcerto.extender.ExtenderClassLoader;
import iconcerto.hibernate.extender.HibernateBundle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Properties;
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

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.internal.BootstrapServiceRegistryImpl;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

public class HibernateSessionFactoryBean extends LocalSessionFactoryBean {
	
	private final static Logger logger = LoggerFactory.getLogger(HibernateSessionFactoryBean.class);

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
		this.classLoader = classLoader;
		
		/*if (classLoader instanceof ExtenderClassLoader) {
			((ExtenderClassLoader)classLoader).setDefaultClassLoader(getClass().getClassLoader());
		}*/
		
		super.setResourceLoader(new ResourceLoader() {
			
			@Override
			public Resource getResource(String location) {
				return new ClassPathResource(
						location, 
						HibernateSessionFactoryBean.this.classLoader
						);
			}
			
			@Override
			public ClassLoader getClassLoader() {
				return HibernateSessionFactoryBean.this.classLoader;
			}
		});
		
		Properties properties = getHibernateProperties();		
		properties.put("hibernate.classLoader.hibernate", this.classLoader);
		properties.put("hibernate.classLoader.application", this.classLoader);
		properties.put("hibernate.classLoader.environment", this.classLoader);
		properties.put("hibernate.classLoader.resources", this.classLoader);
		properties.put("hibernate.connection.provider_class", 
				"org.hibernate.service.jdbc.connections.internal.DatasourceConnectionProviderImpl");
		setHibernateProperties(properties);		
	}
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		
	}

	@Override
	public void afterPropertiesSet() throws IOException {
		/*
		 * Set default class loader for javassist Proxy Factory 
		 * classloader is our custom ClassLoader
		 */
		ProxyFactory.classLoaderProvider = new ClassLoaderProvider() {
			public ClassLoader get(ProxyFactory pf) {
				return HibernateSessionFactoryBean.this.classLoader;
			}
		};

		try {
			afterPropertiesSet0();
			sessionFactoryProxy = createSessionFactoryProxy();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings("serial")
	private void afterPropertiesSet0() throws IOException {
				
		DataSource dataSource = null;
		Resource[] configLocations = null;
		String[] mappingResources = null;
		Resource[] mappingLocations = null;
		Resource[] cacheableMappingLocations = null;
		Resource[] mappingJarLocations = null;
		Resource[] mappingDirectoryLocations = null;
		NamingStrategy namingStrategy = null;
		Properties hibernateProperties = null;
		Class<?>[] annotatedClasses = null;
		String[] annotatedPackages = null;
		String[] packagesToScan = null;
		ResourcePatternResolver resourcePatternResolver = null;
		
		Class<?> localSessionFactoryBeanClass = this.getClass().getSuperclass();
		
		try {						
			Field privateDataSource = localSessionFactoryBeanClass.getDeclaredField("dataSource"); 
			privateDataSource.setAccessible(true);
			dataSource = (DataSource) privateDataSource.get(this);
			
			Field privateConfigLocations= localSessionFactoryBeanClass.getDeclaredField("configLocations"); 
			privateConfigLocations.setAccessible(true);
			configLocations = (Resource[]) privateConfigLocations.get(this);
			
			Field privateMappingResources= localSessionFactoryBeanClass.getDeclaredField("mappingResources"); 
			privateMappingResources.setAccessible(true);
			mappingResources = (String[]) privateMappingResources.get(this);
			
			Field privateMappingLocations= localSessionFactoryBeanClass.getDeclaredField("mappingLocations"); 
			privateMappingLocations.setAccessible(true);
			mappingLocations = (Resource[]) privateMappingLocations.get(this);
			
			Field privateCacheableMappingLocations= localSessionFactoryBeanClass.getDeclaredField("cacheableMappingLocations"); 
			privateCacheableMappingLocations.setAccessible(true);
			cacheableMappingLocations = (Resource[]) privateCacheableMappingLocations.get(this);
			
			Field privateMappingJarLocations= localSessionFactoryBeanClass.getDeclaredField("mappingJarLocations"); 
			privateMappingJarLocations.setAccessible(true);
			mappingJarLocations = (Resource[]) privateMappingJarLocations.get(this);
			
			Field privateMappingDirectoryLocations= localSessionFactoryBeanClass.getDeclaredField("mappingDirectoryLocations"); 
			privateMappingDirectoryLocations.setAccessible(true);
			mappingDirectoryLocations = (Resource[]) privateMappingDirectoryLocations.get(this);
			
			Field privateNamingStrategy= localSessionFactoryBeanClass.getDeclaredField("namingStrategy"); 
			privateNamingStrategy.setAccessible(true);
			namingStrategy = (NamingStrategy) privateNamingStrategy.get(this);
			
			Field privateHibernateProperties= localSessionFactoryBeanClass.getDeclaredField("hibernateProperties"); 
			privateHibernateProperties.setAccessible(true);
			hibernateProperties = (Properties) privateHibernateProperties.get(this);
			
			Field privateAnnotatedClasses= localSessionFactoryBeanClass.getDeclaredField("annotatedClasses"); 
			privateAnnotatedClasses.setAccessible(true);
			annotatedClasses = (Class<?>[]) privateAnnotatedClasses.get(this);
			
			Field privateAnnotatedPackages= localSessionFactoryBeanClass.getDeclaredField("annotatedPackages"); 
			privateAnnotatedPackages.setAccessible(true);
			annotatedPackages = (String[]) privateAnnotatedPackages.get(this);
			
			Field privatePackagesToScan = localSessionFactoryBeanClass.getDeclaredField("packagesToScan"); 
			privatePackagesToScan.setAccessible(true);
			packagesToScan = (String[]) privatePackagesToScan.get(this);
			
			Field privateResourcePatternResolver = localSessionFactoryBeanClass.getDeclaredField("resourcePatternResolver"); 
			privateResourcePatternResolver.setAccessible(true);
			resourcePatternResolver = (ResourcePatternResolver) privateResourcePatternResolver.get(this);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		LocalSessionFactoryBuilder sfb = new LocalSessionFactoryBuilder(dataSource, resourcePatternResolver);

		if (configLocations != null) {
			for (Resource resource : configLocations) {
				// Load Hibernate configuration from given location.
				sfb.configure(resource.getURL());
			}
		}

		if (mappingResources != null) {
			// Register given Hibernate mapping definitions, contained in resource files.
			for (String mapping : mappingResources) {
				Resource mr = new ClassPathResource(mapping.trim(), resourcePatternResolver.getClassLoader());
				sfb.addInputStream(mr.getInputStream());
			}
		}

		if (mappingLocations != null) {
			// Register given Hibernate mapping definitions, contained in resource files.
			for (Resource resource : mappingLocations) {
				sfb.addInputStream(resource.getInputStream());
			}
		}

		if (cacheableMappingLocations != null) {
			// Register given cacheable Hibernate mapping definitions, read from the file system.
			for (Resource resource : cacheableMappingLocations) {
				sfb.addCacheableFile(resource.getFile());
			}
		}

		if (mappingJarLocations != null) {
			// Register given Hibernate mapping definitions, contained in jar files.
			for (Resource resource : mappingJarLocations) {
				sfb.addJar(resource.getFile());
			}
		}

		if (mappingDirectoryLocations != null) {
			// Register all Hibernate mapping definitions in the given directories.
			for (Resource resource : mappingDirectoryLocations) {
				File file = resource.getFile();
				if (!file.isDirectory()) {
					throw new IllegalArgumentException(
							"Mapping directory location [" + resource + "] does not denote a directory");
				}
				sfb.addDirectory(file);
			}
		}

		if (namingStrategy != null) {
			sfb.setNamingStrategy(namingStrategy);
		}

		if (hibernateProperties != null) {
			sfb.addProperties(hibernateProperties);
		}

		if (annotatedClasses != null) {
			sfb.addAnnotatedClasses(annotatedClasses);
		}

		if (annotatedPackages != null) {
			sfb.addPackages(annotatedPackages);
		}

		if (packagesToScan != null) {
			sfb.scanPackages(packagesToScan);
		}

		ClassLoaderService classLoaderService = null;
		try {
			classLoaderService = new ClassLoaderServiceImpl(classLoader);
			Field privateClassClassLoader = ClassLoaderServiceImpl.class.getDeclaredField("classClassLoader"); 
			privateClassClassLoader.setAccessible(true);
			
			Field modifiersField = Field.class.getDeclaredField("modifiers");
		    modifiersField.setAccessible(true);
		    modifiersField.setInt(privateClassClassLoader, privateClassClassLoader.getModifiers() & ~Modifier.FINAL);
		      
			privateClassClassLoader.set(classLoaderService, classLoader);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		Environment.verifyProperties(getHibernateProperties());
		ConfigurationHelper.resolvePlaceHolders(getHibernateProperties());
		final ServiceRegistry serviceRegistry =
				new ServiceRegistryBuilder(
						new BootstrapServiceRegistryImpl(
								classLoaderService, 
								new LinkedHashSet<Integrator>()))
				.applySettings(getHibernateProperties())
				.addService(ClassLoaderService.class, 
						new ClassLoaderServiceImpl(this.classLoader))
				.buildServiceRegistry();
		sfb.setSessionFactoryObserver(
				new SessionFactoryObserver() {
					@Override
					public void sessionFactoryCreated(SessionFactory factory) {
					}

					@Override
					public void sessionFactoryClosed(SessionFactory factory) {
						( (StandardServiceRegistryImpl) serviceRegistry ).destroy();
					}
				}
		);
		
		SessionFactory sessionFactory = sfb.buildSessionFactory(serviceRegistry);
		try {
			Field privateSessionFactory = localSessionFactoryBeanClass.getDeclaredField("sessionFactory"); 
			privateSessionFactory.setAccessible(true);
			privateSessionFactory.set(this, sessionFactory);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public SessionFactory getObject() {		
		return sessionFactoryProxy;
	}
	
	protected void recreateSessionFactory() throws Exception {
		logger.debug("Hibernate environment is being recreated");
		sessionFactoryAccessLock.tryLock(3000, TimeUnit.MILLISECONDS);
		
		try {			
			//super.destroy();		
			//afterPropertiesSet0();		
		}
		finally {
			sessionFactoryAccessLock.unlock();
		}
		logger.debug("Hibernate environment have been recreated");
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
							result = thisMethod.invoke(getObject(), args);
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
		logger.debug("{} classes had been set after bundle adding", 
				Arrays.toString(annotatedClasses.toArray()));
		recreateSessionFactory();
	}
	
	private void removeBundle(HibernateBundle bundle) throws Exception {
		for (Class<?> annotatedClass: annotatedClasses) {
			if (bundle.getEntityClassNames().contains(annotatedClass.getCanonicalName())) {
				annotatedClasses.remove(annotatedClass);
			}
		}
		
		setAnnotatedClasses(annotatedClasses.toArray(new Class[annotatedClasses.size()]));
		logger.debug("{} classes had been set after bundle removing", 
				Arrays.toString(annotatedClasses.toArray()));
		recreateSessionFactory();
	}
	
}
