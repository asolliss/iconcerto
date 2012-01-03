package iconcerto.dao.impl;

import iconcerto.dao.api.EntityDao;
import iconcerto.entities.ParentEntity;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class EntityDaoHibernateImpl implements EntityDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public ParentEntity getParentEntity(Integer id) {		
		return (ParentEntity) sessionFactory
				.getCurrentSession()
				.get(ParentEntity.class, id);
	}

	@Override
	public void save(ParentEntity parentEntity) {
		sessionFactory
			.getCurrentSession()
			.saveOrUpdate(parentEntity);
	}

	@Override
	public void delete(Integer id) {
		sessionFactory
			.getCurrentSession().delete(
					getParentEntity(id)
					);
	}

}
