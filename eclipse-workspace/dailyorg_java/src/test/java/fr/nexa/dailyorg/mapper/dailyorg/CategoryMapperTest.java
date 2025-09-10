package fr.nexa.dailyorg.mapper.dailyorg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.nexa.dailyorg.dto.dailyorg.CategoryDTO;
import fr.nexa.dailyorg.model.dailyorg.Category;

public class CategoryMapperTest {
	
	private CategoryMapper categoryMapper;
	
	@BeforeEach
	void setUp() {
		categoryMapper = new CategoryMapper();
	}
	
	@Test
	void testToDTO_withValidCategory_shouldMapCorrectly() {
		Category category = new Category();
		category.setIdCategory(1L);
		category.setTaskCategoryName("Work");
		category.setTaskCategoryColor("#FF5733");
		
		CategoryDTO dto = categoryMapper.toDTO(category);
		
		assertEquals(category.getIdCategory(), dto.getIdCategory());
		assertEquals(category.getTaskCategoryName(), dto.getTaskCategoryName());
		assertEquals(category.getTaskCategoryColor(), dto.getTaskCategoryColor());
	}
	
	@Test
	void testToEntity_withValidDTO_shouldMapCorrectly() {
		CategoryDTO dto = new CategoryDTO();
		dto.setIdCategory(2L);
		dto.setTaskCategoryName("Personal");
		dto.setTaskCategoryColor("#33FF57");
		
		Category category = categoryMapper.toEntity(dto);
		
		assertEquals(dto.getIdCategory(), category.getIdCategory());
		assertEquals(dto.getTaskCategoryName(), category.getTaskCategoryName());
		assertEquals(dto.getTaskCategoryColor(), category.getTaskCategoryColor());
	}
	
	@Test
	void testToDTO_withNullCategory_shouldReturnNull() {
		assertEquals(null, categoryMapper.toDTO(null));
	}
	
	@Test
	void testToEntity_withNullDTO_shouldReturnNull() {
		assertEquals(null, categoryMapper.toEntity(null));
	}
}
