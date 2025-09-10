package fr.nexa.dailyorg.mapper.dailyorg;

import org.springframework.stereotype.Component;

import fr.nexa.dailyorg.dto.dailyorg.CategoryDTO;
import fr.nexa.dailyorg.model.dailyorg.Category;

@Component
public class CategoryMapper {
	
	public CategoryDTO toDTO(Category category) {
		if (category == null) {
			return null;
		}
		
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setIdCategory(category.getIdCategory());
		categoryDTO.setTaskCategoryColor(category.getTaskCategoryColor());
		categoryDTO.setTaskCategoryName(category.getTaskCategoryName());
		return categoryDTO;
	}
	
	public Category toEntity(CategoryDTO categoryDTO) {
		if (categoryDTO == null) {
			return null;
		}
		
		Category category = new Category();
		category.setIdCategory(categoryDTO.getIdCategory());
		category.setTaskCategoryColor(categoryDTO.getTaskCategoryColor());
		category.setTaskCategoryName(categoryDTO.getTaskCategoryName());
		return category;
	}

}
