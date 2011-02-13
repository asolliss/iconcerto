package iconcerto.wiki.data;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import iconcerto.wiki.domain.Page;
import iconcerto.wiki.domain.persistence.PageEntity;
import iconcerto.wiki.domain.persistence.RevisionEntity;

@PostgreSQL
public class PostgreSQLDAO implements WikiDAO {

	private EntityManager em;

	@Override
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}	

	@Override
	public Page getPage(String name) throws DAORuntimeException {
		Page page = null;
		try {
			PageEntity pageEntity = em.find(PageEntity.class, name);
			
			if (pageEntity == null) throw new NoResultException(); 
			
			RevisionEntity revisionEntity = 
				(RevisionEntity) em.createQuery("select r from RevisionEntity r where r.page = :page order by r.timestamp desc")
				.setParameter("page", pageEntity).setMaxResults(1).getSingleResult();
			
			page = new Page(pageEntity, revisionEntity);
		}
		catch (NoResultException e) {
			page = new Page();
			page.setName(name);
		}
		catch (PersistenceException e) {
			throw new UnderlyingDataSourceException(e);
		}
		
		return page;
	}

	@Override
	public Page save(Page page) throws DAORuntimeException {
		Page updatedPage = null;
		try {
			PageEntity pageEntity = null;
			if (page.isNew()) {
				pageEntity = new PageEntity();
				pageEntity.setName(page.getName());
				em.persist(pageEntity);
			}
			else {
				pageEntity = em.find(PageEntity.class, page.getName());					
			}
			
			RevisionEntity revisionEntity = new RevisionEntity();
			revisionEntity.setRevision(UUID.randomUUID().toString());
			revisionEntity.setTimestamp(new Date());
			revisionEntity.setPage(pageEntity);		
			revisionEntity.setCode(page.getCode());
			revisionEntity.setXHTML(page.getXHTML());
			
			em.persist(revisionEntity);
			
			updatedPage = new Page(pageEntity, revisionEntity);
		}
		catch (PersistenceException e) {
			throw new UnderlyingDataSourceException(e);
		}
		
		return updatedPage;
	}

	@Override
	public boolean isEntityManagerNeed() {
		return true;
	}

}
