package fr.nexa.dailyorg.components.dailyorg;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import fr.nexa.dailyorg.model.dailyorg.Category;
import fr.nexa.dailyorg.repository.dailyorg.ICategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TaskCategoriesSeeder {

	private final ICategoryRepository categoryRepository;

	@PostConstruct
	public void populateTaskCategories() {
		// Define default categories
		String[] defaultCategories = { "Work,#FF5733", "Personal,#33FF57", "Health,#3357FF", "Finance,#F1C40F",
				"Shopping,#8E44AD" };
		for (String categoryName : defaultCategories) {
			if (categoryRepository.findByTaskCategoryName(categoryName.split(",")[0]).isEmpty()) {
				categoryRepository.save(Category.builder().taskCategoryName(categoryName.split(",")[0])
						.taskCategoryColor(categoryName.split(",")[1]).build());
				
				Logger.getLogger(TaskCategoriesSeeder.class.getName()).info("Category created: " + categoryName.split(",")[0] + " with color " + categoryName.split(",")[1]);
			}
		}
	}
}
