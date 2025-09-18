package fr.nexa.dailyorg.controller.dailyorg;

import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg.config.JwtUtil;
import fr.nexa.dailyorg.dto.dailyorg.CategoryDTO;
import fr.nexa.dailyorg.mapper.dailyorg.CategoryMapper;
import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.dailyorg.Category;
import fr.nexa.dailyorg.model.dailyorg.Task;
import fr.nexa.dailyorg.service.AppUserService;
import fr.nexa.dailyorg.service.dailyorg.impl.CategoryService;
import fr.nexa.dailyorg.service.dailyorg.impl.TaskService;
import fr.nexa.dailyorg.utils.EErrorMessages;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {

	private final AppUserService appUserService;
	private final CategoryService categoryService;
	private final TaskService taskService;
	private final CategoryMapper categoryMapper;
	
	private final JwtUtil jwtUtil;
	
	@PutMapping("/create")
	public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO, @NonNull HttpServletRequest request) {
		try {
			String userEmail = jwtUtil.extractUsernameFromCookies(request.getCookies());
			Optional<AppUser> user = appUserService.findByEmail(userEmail);
			if (user.isEmpty()) {
				throw new IllegalStateException(EErrorMessages.USER_NOT_FOUND.getMessage());
			}
			
			Category category = categoryMapper.toEntity(categoryDTO);
			if(category == null)
				throw new IllegalStateException("Category mapping failed");
			
			category.setOrganizerUser(user.get().getOrganizerUser());
			
			Category savedCategory = categoryService.save(category);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating category");
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteCategory(@RequestBody CategoryDTO categoryDTO, @NonNull HttpServletRequest request) {
		try {
			Category category = categoryMapper.toEntity(categoryDTO);
			if(category == null)
				throw new IllegalStateException("Category mapping failed");
			
			String userEmail = jwtUtil.extractUsernameFromCookies(request.getCookies());
			Optional<AppUser> user = appUserService.findByEmail(userEmail);
			if (user.isEmpty()) {
				throw new IllegalStateException(EErrorMessages.USER_NOT_FOUND.getMessage());
			}
			
			Category existingCategory = categoryService.findById(category.getIdCategory());
			
			if(existingCategory.getOrganizerUser() == null || existingCategory.getOrganizerUser().getOrganizerUserId() != (user.get().getOrganizerUser().getOrganizerUserId())) {
				throw new IllegalStateException(EErrorMessages.OPERATION_NOT_PERMITTED.getMessage());
			}
			
			//Check for tasks using this category
			List<Task> tasksWithCategory = taskService.getAllTasksByCategoryId(existingCategory.getIdCategory());
			
			for(Task t : tasksWithCategory) {
				t.setCategory(null);
				taskService.updateTask(t);
			}
			
			categoryService.delete(category);
			return ResponseEntity.ok("Category deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting category");
		}
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO) {
		try {
			Category category = categoryMapper.toEntity(categoryDTO);
			if(category == null)
				throw new IllegalStateException("Category mapping failed");
			
			Category existingCategory = categoryService.findById(category.getIdCategory());
			existingCategory.setTaskCategoryName(category.getTaskCategoryName());
			existingCategory.setTaskCategoryColor(category.getTaskCategoryColor());
			
			Category updatedCategory = categoryService.save(existingCategory);
			return ResponseEntity.ok(updatedCategory);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating category");
		}
	}
	
	@GetMapping("/get_all_by_user")
	public ResponseEntity<?> getAllCategoriesByUserId(@NonNull HttpServletRequest request) {
		try {
			//Extract user ID from JWT token
			String userEmail = jwtUtil.extractUsernameFromCookies(request.getCookies());
			
			Optional<AppUser> user = appUserService.findByEmail(userEmail);
			if (user.isEmpty()) {
				throw new IllegalStateException(EErrorMessages.USER_NOT_FOUND.getMessage());
			}
			return ResponseEntity.ok(categoryService.findAllByOrganizerUser(user.get().getOrganizerUser().getOrganizerUserId()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + "Error retrieving categories");
		}
	}
	
	@GetMapping("/get_by_id/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
		try {
			Category category = categoryService.findById(id);
			if (category == null) {
				throw new IllegalStateException(EErrorMessages.RESOURCE_NOT_FOUND.getMessage());
			}
			
			return ResponseEntity.ok(category);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving category");
		}
	}
	
}
