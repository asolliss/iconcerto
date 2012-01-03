package iconcerto.dao.api;

import iconcerto.entities.ParentEntity;

public interface EntityDao {

	ParentEntity getParentEntity(Integer id);
	
	void save(ParentEntity parentEntity);
	
	void delete(Integer id);
	
}
