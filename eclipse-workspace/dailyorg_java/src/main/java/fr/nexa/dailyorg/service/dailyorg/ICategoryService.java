package fr.nexa.dailyorg.service.dailyorg;

import java.util.List;

import fr.nexa.dailyorg.model.dailyorg.Category;

public interface ICategoryService {

	Category save(Category category) throws Exception;

	void delete(Category category) throws Exception;

	List<Category> findAllByOrganizerUser(Long organizerUserId) throws Exception;

	Category findById(long id) throws Exception;

}
