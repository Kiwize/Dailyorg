package fr.nexa.dailyorg.service.dailyorg.impl;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.nexa.dailyorg.components.factory.AppUserFactory;
import fr.nexa.dailyorg.components.factory.dailyorg.OrganizerUserFactory;
import fr.nexa.dailyorg.model.dailyorg.Category;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.repository.dailyorg.ICategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
	
	@Mock
	private ICategoryRepository categoryRepository;
	
	@InjectMocks
	private CategoryService categoryService;
	
	//Factories
	private final OrganizerUserFactory organizerUserFactory = new OrganizerUserFactory();
	private final AppUserFactory appUserFactory = new AppUserFactory();
	
	@Test
	void testSave() throws Exception {
		Category category = Category.builder()
				.taskCategoryName("Work")
				.taskCategoryColor("#FF5733")
				.build();
		
		when(categoryRepository.save(category)).thenReturn(category);
		
		Category savedCategory = categoryService.save(category);
		
		assertEquals(category, savedCategory);
		verify(categoryRepository).save(category);
	}
	
	@Test
	void testDelete() throws Exception {
		Category category = Category.builder()
				.taskCategoryName("Personal")
				.taskCategoryColor("#33FF57")
				.build();
		
		categoryService.delete(category);
		
		verify(categoryRepository).delete(category);
	}
	

	
	@Test
	void testFindById() throws Exception {
		Category category = Category.builder()
				.idCategory(1L)
				.taskCategoryName("Health")
				.taskCategoryColor("#3357FF")
				.build();
		
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		
		Category foundCategory = categoryService.findById(1L);
		
		assertEquals(category, foundCategory);
		verify(categoryRepository).findById(1L);
	}
	
	@Test
	void testFindById_notFound() throws Exception {
		when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
		
		Category foundCategory = categoryService.findById(2L);
		
		assertEquals(null, foundCategory);
		verify(categoryRepository).findById(2L);
	}
	
	@Test
	void testFindAllByOrganizerUser() throws Exception {
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser());
		
		Category category1 = Category.builder()
				.idCategory(1L)
				.taskCategoryName("Fitness")
				.taskCategoryColor("#FF33A1")
				.organizerUser(organizerUser)
				.build();
		
		Category category2 = Category.builder()
				.idCategory(2L)
				.taskCategoryName("Travel")
				.taskCategoryColor("#33FFF6")
				.organizerUser(organizerUser)
				.build();
		
		when(categoryRepository.findAllByOrganizerUserIdOrOrganizerUserIdIsNull(organizerUser)).thenReturn(List.of(category1, category2));
		
		List<Category> foundCategories = categoryService.findAllByOrganizerUser(organizerUser);
		
		assertEquals(2, foundCategories.size());
		assertEquals(category1, foundCategories.get(0));
		assertEquals(category2, foundCategories.get(1));
	}
	
	@Test
	void testFindAllByOrganizerUser_empty() throws Exception {
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser());
		
		when(categoryRepository.findAllByOrganizerUserIdOrOrganizerUserIdIsNull(organizerUser)).thenReturn(List.of());
		
		List<Category> foundCategories = categoryService.findAllByOrganizerUser(organizerUser);
		
		assertEquals(0, foundCategories.size());
	}

}
