package fr.nexa.dailyorg.repository.dailyorg;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg.model.dailyorg.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
	
	Optional<Category> findByTaskCategoryName(String taskCategoryName);
	List<Category> findAllByOrganizerUserOrganizerUserIdOrOrganizerUserOrganizerUserIdIsNull(Long organizerUserId);

}
