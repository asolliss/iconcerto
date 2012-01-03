package iconcerto.service.api;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EntityService {
	
	void createParentEntity(Integer id, String name);
	
	void deleteParentEntity(Integer id);
	
	void addChildEntity(
			Integer parentEntityId, 
			Integer childEntityId, 
			String childEntityName);
	
}
