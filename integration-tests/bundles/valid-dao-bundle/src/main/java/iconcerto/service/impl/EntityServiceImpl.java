package iconcerto.service.impl;

import java.util.ArrayList;

import iconcerto.dao.api.EntityDao;
import iconcerto.entities.ChildEntity;
import iconcerto.entities.ParentEntity;
import iconcerto.service.api.EntityService;

public class EntityServiceImpl implements EntityService {

	private EntityDao entityDao;
	
	public EntityDao getEntityDao() {
		return entityDao;
	}

	public void setEntityDao(EntityDao entityDao) {
		this.entityDao = entityDao;
	}

	@Override
	public void createParentEntity(Integer id, String name) {
		ParentEntity parentEntity = new ParentEntity();
		parentEntity.setId(id);
		parentEntity.setName(name);
		entityDao.save(parentEntity);
	}

	@Override
	public void deleteParentEntity(Integer id) {
		entityDao.delete(id);
	}

	@Override
	public void addChildEntity(
			Integer parentEntityId, 
			Integer childEntityId,
			String childEntityName) {
		ParentEntity parentEntity =
				entityDao.getParentEntity(parentEntityId);
		ChildEntity childEntity = new ChildEntity();
		childEntity.setId(childEntityId);
		childEntity.setName(childEntityName);
		childEntity.setParentEntity(parentEntity);
		
		if (parentEntity.getChildren() == null) {
			parentEntity.setChildren(new ArrayList<ChildEntity>());
		}
		
		parentEntity.getChildren().add(childEntity);
	}

}
