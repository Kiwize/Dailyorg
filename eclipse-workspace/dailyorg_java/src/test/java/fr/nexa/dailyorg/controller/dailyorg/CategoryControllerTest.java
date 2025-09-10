package fr.nexa.dailyorg.controller.dailyorg;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.nexa.dailyorg.components.factory.AppUserFactory;
import fr.nexa.dailyorg.components.factory.dailyorg.OrganizerUserFactory;
import fr.nexa.dailyorg.config.JwtUtil;
import fr.nexa.dailyorg.dto.dailyorg.CategoryDTO;
import fr.nexa.dailyorg.mapper.dailyorg.CategoryMapper;
import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.dailyorg.Category;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.service.AppUserService;
import fr.nexa.dailyorg.service.dailyorg.impl.CategoryService;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoryService categoryService;

	@MockitoBean
	private AppUserService appUserService;
	
	@MockitoBean
	private CategoryMapper categoryMapper;

	@MockitoBean
	private JwtUtil jwtUtil;

	private final ObjectMapper objectMapper = new ObjectMapper();

	// Factories
	private final OrganizerUserFactory organizerUserFactory = new OrganizerUserFactory();
	private final AppUserFactory appUserFactory = new AppUserFactory();

	@Test
	void testCreateCategoryLinkedWithUser() throws Exception {
		AppUser appUser = appUserFactory.createOneAppUser();
		appUser.setUserId(0L);
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser(appUser);
		CategoryDTO categoryDTO = new CategoryDTO();

		appUser.setOrganizerUser(organizerUser);
		categoryDTO.setTaskCategoryName("Work");
		categoryDTO.setTaskCategoryColor("#FF5733");

		Category category = Category.builder().taskCategoryName(categoryDTO.getTaskCategoryName())
				.taskCategoryColor(categoryDTO.getTaskCategoryColor()).organizerUser(organizerUser).build();

		when(categoryMapper.toEntity(any())).thenReturn(category);
		when(appUserService.getAppUserByID(0L)).thenReturn(appUser);
		when(categoryService.save(category)).thenReturn(category);

		mockMvc.perform(put("/api/category/create/{userId}", 0L).contentType("application/json")
				.content(objectMapper.writeValueAsString(categoryDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

	}
	
	@Test
	void testCreateCategoryNotLinkedWithUser() throws Exception {
		AppUser appUser = appUserFactory.createOneAppUser();
		appUser.setUserId(0L);
		CategoryDTO categoryDTO = new CategoryDTO();

		appUser.setOrganizerUser(null);
		categoryDTO.setTaskCategoryName("Work");
		categoryDTO.setTaskCategoryColor("#FF5733");

		Category category = Category.builder().taskCategoryName(categoryDTO.getTaskCategoryName())
				.taskCategoryColor(categoryDTO.getTaskCategoryColor()).organizerUser(null).build();

		when(categoryMapper.toEntity(any())).thenReturn(category);
		when(appUserService.getAppUserByID(0L)).thenReturn(appUser);
		when(categoryService.save(category)).thenReturn(category);

		mockMvc.perform(put("/api/category/create/{userId}", 0L).contentType("application/json")
				.content(objectMapper.writeValueAsString(categoryDTO)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}
	
	@Test
	void testCreateWithNullCategory() throws Exception {
		when(categoryMapper.toEntity(any())).thenReturn(null);

		mockMvc.perform(put("/api/category/create/{userId}", 1L).contentType("application/json")
				.content(objectMapper.writeValueAsString(new CategoryDTO())))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
	
	@Test
	void testCreateCategoryGlobalAdmin() throws Exception {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setTaskCategoryName("Work");
		categoryDTO.setTaskCategoryColor("#FF5733");

		Category category = Category.builder().taskCategoryName(categoryDTO.getTaskCategoryName())
				.taskCategoryColor(categoryDTO.getTaskCategoryColor()).organizerUser(null).build();

		when(categoryMapper.toEntity(any())).thenReturn(category);
		when(categoryService.save(category)).thenReturn(category);

		mockMvc.perform(put("/api/category/create").contentType("application/json")
				.content(objectMapper.writeValueAsString(categoryDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

	}
	
	@Test
	void testCreateCategoryGlobalAdmin_nullEntity() throws Exception {
		when(categoryMapper.toEntity(any())).thenReturn(null);

		mockMvc.perform(put("/api/category/create").contentType("application/json")
				.content(objectMapper.writeValueAsString(new CategoryDTO())))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
	
	@Test
	void testCreateCategory_userNotFound() throws Exception {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setTaskCategoryName("Work");
		categoryDTO.setTaskCategoryColor("#FF5733");

		when(appUserService.getAppUserByID(0L)).thenReturn(null);

		mockMvc.perform(put("/api/category/create/{userId}", 0L).contentType("application/json")
				.content(objectMapper.writeValueAsString(categoryDTO)))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());

	}
	
	@Test
	void testDeleteCategory() throws Exception {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setIdCategory(1L);
		categoryDTO.setTaskCategoryName("Work");
		categoryDTO.setTaskCategoryColor("#FF5733");

		Category category = Category.builder().idCategory(categoryDTO.getIdCategory())
				.taskCategoryName(categoryDTO.getTaskCategoryName())
				.taskCategoryColor(categoryDTO.getTaskCategoryColor()).build();

		when(categoryMapper.toEntity(any())).thenReturn(category);

		mockMvc.perform(MockMvcRequestBuilders
				.delete("/api/category/delete").contentType("application/json")
				.content(objectMapper.writeValueAsString(categoryDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}
	
	@Test
	void testDeleteNullCategory() throws Exception {
		when(categoryMapper.toEntity(any())).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders
				.delete("/api/category/delete").contentType("application/json")
				.content(objectMapper.writeValueAsString(new CategoryDTO())))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
	
	@Test
	void testUpdateCategory() throws Exception {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setIdCategory(1L);
		categoryDTO.setTaskCategoryName("Work");
		categoryDTO.setTaskCategoryColor("#FF5733");

		Category category = Category.builder().idCategory(categoryDTO.getIdCategory())
				.taskCategoryName(categoryDTO.getTaskCategoryName())
				.taskCategoryColor(categoryDTO.getTaskCategoryColor()).build();

		when(categoryMapper.toEntity(any())).thenReturn(category);
		when(categoryService.save(category)).thenReturn(category);

		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/category/update").contentType("application/json")
				.content(objectMapper.writeValueAsString(categoryDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	void testUpdateNullCategory() throws Exception {
		when(categoryMapper.toEntity(any())).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/category/update").contentType("application/json")
				.content(objectMapper.writeValueAsString(new CategoryDTO())))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
	
	@Test
	void testGetCategoryById_found() throws Exception {
		Category category = Category.builder()
				.idCategory(1L)
				.taskCategoryName("Health")
				.taskCategoryColor("#3357FF")
				.build();
		
		when(categoryService.findById(1L)).thenReturn(category);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/category/get_by_id/{id}", 1L))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.idCategory").value(1L))
				.andExpect(MockMvcResultMatchers.jsonPath("$.taskCategoryName").value("Health"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.taskCategoryColor").value("#3357FF"));
	}
	
	@Test
	void testGetCategoryById_notFound() throws Exception {
		when(categoryService.findById(2L)).thenReturn(null);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/category/get_by_id/{id}", 2L))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
		
	}
	
	@Test
	void testGetAllCategoriesByUserId_userNotFound() throws Exception {
		when(appUserService.getAppUserByID(0L)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/category/get_all_by_user/{userId}", 0L))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
		
	}
	
	@Test
	void testGetAllCategoriesByUserId() throws Exception {
		AppUser appUser = appUserFactory.createOneAppUser();
		appUser.setUserId(0L);
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser(appUser);
		appUser.setOrganizerUser(organizerUser);

		when(appUserService.getAppUserByID(0L)).thenReturn(appUser);

		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/category/get_all_by_user/{userId}", 0L))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetAllCategoriesByUserId_shouldReturnTwoCategories() throws Exception {
		AppUser appUser = appUserFactory.createOneAppUser();
		appUser.setUserId(0L);
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser(appUser);
		appUser.setOrganizerUser(organizerUser);
		
		Category category1 = Category.builder()
				.idCategory(1L)
				.taskCategoryName("Health")
				.taskCategoryColor("#3357FF")
				.organizerUser(organizerUser)
				.build();
		
		Category category2 = Category.builder()
				.idCategory(2L)
				.taskCategoryName("Work")
				.taskCategoryColor("#FF5733")
				.organizerUser(organizerUser)
				.build();
		
		when(categoryService.findAllByOrganizerUser(organizerUser)).thenReturn(List.of(category1, category2));
		when(appUserService.getAppUserByID(0L)).thenReturn(appUser);

		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/category/get_all_by_user/{userId}", 0L))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
	}
}
