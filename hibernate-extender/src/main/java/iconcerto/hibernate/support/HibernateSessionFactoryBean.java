package iconcerto.hibernate.support;

import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class HibernateSessionFactoryBean extends AnnotationSessionFactoryBean {

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		// disabling BeanClassLoaderAware from LocalSessionFactoryBean
	}

	/**
	 * Set a class loader with support of mapping classes 
	 * @param classLoader
	 */
	public void setClassLoader(ClassLoader classLoader) {
		super.setBeanClassLoader(classLoader);
	}
	
}
