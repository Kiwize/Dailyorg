package fr.nexa.dailyorg.controller.dailyorg;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg.dto.dailyorg.CategoryDTO;
import fr.nexa.dailyorg.mapper.dailyorg.CategoryMapper;
import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.dailyorg.Category;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.service.AppUserService;
import fr.nexa.dailyorg.service.dailyorg.impl.CategoryService;
import fr.nexa.dailyorg.utils.EErrorMessages;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {

	private final AppUserService appUserService;
	private final CategoryService categoryService;
	private final CategoryMapper categoryMapper;
	
	@PutMapping("/create/{userId}")
	public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long userId) {
		try {
			Category category = categoryMapper.toEntity(categoryDTO);
			if(category == null)
				throw new Exception("Category mapping failed");
			
			//Check for the user
			AppUser user = appUserService.getAppUserByID(userId);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EErrorMessages.USER_NOT_FOUND.getMessage());
			} else {
				OrganizerUser organizerUser = user.getOrganizerUser();
				if (organizerUser == null) {
					Logger.getLogger(CategoryController.class.getName()).warning("No organizer user linked to the app user with ID: " + userId);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(EErrorMessages.USER_NOT_FOUND.getMessage());
				}
				category.setOrganizerUser(organizerUser);
			}
			
			Category savedCategory = categoryService.save(category);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating category");
		}
	}
	
	// Creates a category available for all users, restricted to admin use
	@PutMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
		try {
			Category category = categoryMapper.toEntity(categoryDTO);
			if(category == null)
				throw new Exception("Category mapping failed");
			category.setOrganizerUser(null); // Global category
			Category savedCategory = categoryService.save(category);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating category");
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteCategory(@RequestBody CategoryDTO categoryDTO) {
		try {
			Category category = categoryMapper.toEntity(categoryDTO);
			if(category == null)
				throw new Exception("Category mapping failed");
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
				throw new Exception("Category mapping failed");
			Category updatedCategory = categoryService.save(category);
			return ResponseEntity.ok(updatedCategory);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating category");
		}
	}
	
	@GetMapping("/get_all_by_user/{userId}")
	public ResponseEntity<?> getAllCategoriesByUserId(@PathVariable Long userId) {
		try {
			AppUser user = appUserService.getAppUserByID(userId);
			if (user == null) {
				throw new Exception(EErrorMessages.USER_NOT_FOUND.getMessage());
			}
			OrganizerUser organizerUser = user.getOrganizerUser();
			
			return ResponseEntity.ok(categoryService.findAllByOrganizerUser(organizerUser));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving categories");
		}
	}
	
	@GetMapping("/get_by_id/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
		try {
			Category category = categoryService.findById(id);
			if (category == null) {
				throw new Exception(EErrorMessages.RESOURCE_NOT_FOUND.getMessage());
			}
			
			return ResponseEntity.ok(category);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving category");
		}
	}
	
}
