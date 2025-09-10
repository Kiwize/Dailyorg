package fr.nexa.dailyorg.service.dailyorg;

import java.util.List;

import fr.nexa.dailyorg.model.dailyorg.Category;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;

public interface ICategoryService {

	Category save(Category category) throws Exception;

	void delete(Category category) throws Exception;

	List<Category> findAllByOrganizerUser(OrganizerUser organizerUser) throws Exception;

	Category findById(long id) throws Exception;

}
