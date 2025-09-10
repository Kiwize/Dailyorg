package fr.nexa.dailyorg.service.dailyorg.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.dailyorg.Category;
import fr.nexa.dailyorg.repository.dailyorg.ICategoryRepository;
import fr.nexa.dailyorg.service.dailyorg.ICategoryService;
import jakarta.transaction.Transactional;

@Service
public class CategoryService implements ICategoryService {
	
	@Autowired
	private ICategoryRepository categoryRepository;

	@Override
	public Category save(Category category) throws Exception {
		return categoryRepository.save(category);
	}

	@Override
	@Transactional
	public void delete(Category category) throws Exception {
		categoryRepository.delete(category);
	}
	
	@Override
	public List<Category> findAllByOrganizerUser(Long organizerUserId) throws Exception {
		return categoryRepository.findAllByOrganizerUserOrganizerUserIdOrOrganizerUserOrganizerUserIdIsNull(organizerUserId);
	}

	@Override
	public Category findById(long id) throws Exception {
		return categoryRepository.findById(id).orElse(null);
	}
	
	

}
