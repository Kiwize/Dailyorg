package fr.nexa.dailyorg.controller.dailyorg;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.nexa.dailyorg.components.factory.AppUserFactory;
import fr.nexa.dailyorg.components.factory.dailyorg.OrganizerUserFactory;
import fr.nexa.dailyorg.components.factory.dailyorg.RecurringTaskStateFactory;
import fr.nexa.dailyorg.components.factory.dailyorg.TaskFactory;
import fr.nexa.dailyorg.components.factory.dailyorg.TaskPriorityFactory;
import fr.nexa.dailyorg.config.JwtUtil;
import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.model.dailyorg.RecurringTaskState;
import fr.nexa.dailyorg.model.dailyorg.Task;
import fr.nexa.dailyorg.model.dailyorg.TaskPriority;
import fr.nexa.dailyorg.service.AppUserService;
import fr.nexa.dailyorg.service.dailyorg.impl.CategoryService;
import fr.nexa.dailyorg.service.dailyorg.impl.OrganizerUserService;
import fr.nexa.dailyorg.service.dailyorg.impl.RecurringTaskStateService;
import fr.nexa.dailyorg.service.dailyorg.impl.TaskPriorityService;
import fr.nexa.dailyorg.service.dailyorg.impl.TaskService;
import fr.nexa.dailyorg.utils.EErrorMessages;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private TaskService taskService;
	
	@MockitoBean
	private RecurringTaskStateService recurringTaskStateService;
	
	@MockitoBean
	private AppUserService appUserService;
	
	@MockitoBean
	private OrganizerUserService organizerUserService;
	
	@MockitoBean
	private TaskPriorityService taskPriorityService;
	
	@MockitoBean
	private CategoryService categoryService;
	
	@MockitoBean
	private JwtUtil jwtUtil;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	//Factories
	private final TaskFactory taskFactory = new TaskFactory();
	private final TaskPriorityFactory taskPriorityFactory = new TaskPriorityFactory();
	private final AppUserFactory appUserFactory = new AppUserFactory();
	private final OrganizerUserFactory organizerUserFactory = new OrganizerUserFactory();
	private final RecurringTaskStateFactory recurringTaskStateFactory = new RecurringTaskStateFactory();
	
	// ###### TASK CREATION TESTS ######
	
	@Test
	void testCreateTask_withValidDataAndNoOrganizerProfile_returnsOk() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), "Test task");
		requestBody.put(ETaskControllerFields.TASK_DESCRIPTION.getFieldName(), "This is a test task");
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), LocalDateTime.now().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), LocalDateTime.now().plusHours(1).toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), "High");
		
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority("High", 3);
		when(taskPriorityService.getTaskPriorityByTaskPriorityName("High")).thenReturn(Optional.of(taskPriority));
		
		AppUser appUser = appUserFactory.createOneAppUser();
		when(jwtUtil.extractUsernameFromCookies(any())).thenReturn("test@test.fr");
		when(appUserService.findByEmail("test@test.fr")).thenReturn(Optional.of(appUser));
		
		mockMvc.perform(put("/api/task/create_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().isOk())
		.andExpect(content().string("Task and profile created..."));
	}
	
	@Test
	void testCreateTask_withValidDataAndOrganizerProfile_returnsOk() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), "Test task");
		requestBody.put(ETaskControllerFields.TASK_DESCRIPTION.getFieldName(), "This is a test task");
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), LocalDateTime.now().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), LocalDateTime.now().plusHours(1).toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), "High");
		
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority("High", 3);
		when(taskPriorityService.getTaskPriorityByTaskPriorityName("High")).thenReturn(Optional.of(taskPriority));
		
		AppUser appUser = appUserFactory.createOneAppUser();
		appUser.setOrganizerUser(organizerUserFactory.createOneOrganizerUser());
		
		when(jwtUtil.extractUsernameFromCookies(any())).thenReturn("test@test.fr");
		when(appUserService.findByEmail("test@test.fr")).thenReturn(Optional.of(appUser));
		
		mockMvc.perform(put("/api/task/create_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().isOk())
		.andExpect(content().string("Task created..."));
	}
	
	@Test
	void testCreateTask_withValidDataAndRecurrentTask_returnsOk() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), "Test task");
		requestBody.put(ETaskControllerFields.TASK_DESCRIPTION.getFieldName(), "This is a test task");
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), LocalDateTime.now().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), LocalDateTime.now().plusHours(1).toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), "High");
		
		requestBody.put(ETaskControllerFields.TASK_IS_RECURRENT.getFieldName(), "true");
		requestBody.put(ETaskControllerFields.TASK_REPEAT_FREQUENCY_ID.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_REPEAT_END_DATE.getFieldName(), LocalDate.now().plusDays(7).toString());
		
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority("High", 3);
		when(taskPriorityService.getTaskPriorityByTaskPriorityName("High")).thenReturn(Optional.of(taskPriority));
		
		RecurringTaskState recurringTaskState = recurringTaskStateFactory.createEverydayRecurringTaskState();
		when(recurringTaskStateService.findById(1L)).thenReturn(Optional.of(recurringTaskState));
		
		AppUser appUser = appUserFactory.createOneAppUser();
		
		when(jwtUtil.extractUsernameFromCookies(any())).thenReturn("test@test.fr");
		when(appUserService.findByEmail("test@test.fr")).thenReturn(Optional.of(appUser));
		
		mockMvc.perform(put("/api/task/create_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().isOk())
		.andExpect(content().string("Task and profile created..."));
	}
	
	// ## Invalid data tests ##
	
	@Test
	void testCreateTask_withWrongPriorityName_returnsBadRequest() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), "Test task");
		requestBody.put(ETaskControllerFields.TASK_DESCRIPTION.getFieldName(), "This is a test task");
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), LocalDateTime.now().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), LocalDateTime.now().plusHours(1).toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), "Huhgdfg");
		
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority("High", 3);
		when(taskPriorityService.getTaskPriorityByTaskPriorityName("High")).thenReturn(Optional.of(taskPriority));
		
		AppUser appUser = appUserFactory.createOneAppUser();
		appUser.setOrganizerUser(organizerUserFactory.createOneOrganizerUser());
		
		when(jwtUtil.extractUsernameFromCookies(any())).thenReturn("test@test.fr");
		when(appUserService.findByEmail("test@test.fr")).thenReturn(Optional.of(appUser));
		
		mockMvc.perform(put("/api/task/create_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testCreateTask_withMissingStartAndEndDates_returnsBadRequest() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), "Test task");
		requestBody.put(ETaskControllerFields.TASK_DESCRIPTION.getFieldName(), "This is a test task");
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), "High");
		
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority("High", 3);
		when(taskPriorityService.getTaskPriorityByTaskPriorityName("High")).thenReturn(Optional.of(taskPriority));
		
		AppUser appUser = appUserFactory.createOneAppUser();
		appUser.setOrganizerUser(organizerUserFactory.createOneOrganizerUser());
		
		when(jwtUtil.extractUsernameFromCookies(any())).thenReturn("test@test.fr");
		when(appUserService.findByEmail("test@test.fr")).thenReturn(Optional.of(appUser));
		
		mockMvc.perform(put("/api/task/create_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testCreateTask_withInvalidNumberFormatException() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), "Test task");
		requestBody.put(ETaskControllerFields.TASK_DESCRIPTION.getFieldName(), "This is a test task");
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), LocalDateTime.now().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), LocalDateTime.now().plusHours(1).toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), "one"); // Invalid number format	
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), "High");
		
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority("High", 3);
		when(taskPriorityService.getTaskPriorityByTaskPriorityName("High")).thenReturn(Optional.of(taskPriority));
		
		AppUser appUser = appUserFactory.createOneAppUser();
		when(jwtUtil.extractUsernameFromCookies(any())).thenReturn("test@test.fr");
		when(appUserService.findByEmail("test@test.fr")).thenReturn(Optional.of(appUser));
		
		mockMvc.perform(put("/api/task/create_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testCreateTask_withInvalidDateFormat() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), "Test task");
		requestBody.put(ETaskControllerFields.TASK_DESCRIPTION.getFieldName(), "This is a test task");
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), "2024/10/10 10:00"); // Invalid date format
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), "2024/10/10 11:00"); // Invalid date format
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), "High");
		
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority("High", 3);
		when(taskPriorityService.getTaskPriorityByTaskPriorityName("High")).thenReturn(Optional.of(taskPriority));
		
		AppUser appUser = appUserFactory.createOneAppUser();
		when(jwtUtil.extractUsernameFromCookies(any())).thenReturn("test@test.fr");
		when(appUserService.findByEmail("test@test.fr")).thenReturn(Optional.of(appUser));
		
		mockMvc.perform(put("/api/task/create_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is5xxServerError());
	}
	
	@Test
	void testCreateTask_withInvalidUser() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), "Test task");
		requestBody.put(ETaskControllerFields.TASK_DESCRIPTION.getFieldName(), "This is a test task");
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), LocalDateTime.now().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), LocalDateTime.now().plusHours(1).toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), "High");
		
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority("High", 3);
		when(taskPriorityService.getTaskPriorityByTaskPriorityName("High")).thenReturn(Optional.of(taskPriority));
		
		AppUser appUser = appUserFactory.createOneAppUser();
		when(jwtUtil.extractUsernameFromCookies(any())).thenReturn("wrong@email.fr"); // User not found
		when(appUserService.findByEmail("test@test.fr")).thenReturn(Optional.of(appUser));
		
		mockMvc.perform(put("/api/task/create_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testCreateTask_withMissingRecurringState() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), "Test task");
		requestBody.put(ETaskControllerFields.TASK_DESCRIPTION.getFieldName(), "This is a test task");
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), LocalDateTime.now().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), LocalDateTime.now().plusHours(1).toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), "High");
		
		requestBody.put(ETaskControllerFields.TASK_IS_RECURRENT.getFieldName(), "true");
		requestBody.put(ETaskControllerFields.TASK_REPEAT_FREQUENCY_ID.getFieldName(), "42"); // Recurring state not found
		requestBody.put(ETaskControllerFields.TASK_REPEAT_END_DATE.getFieldName(), LocalDate.now().plusDays(7).toString());
		
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority("High", 3);
		when(taskPriorityService.getTaskPriorityByTaskPriorityName("High")).thenReturn(Optional.of(taskPriority));
		
		RecurringTaskState recurringTaskState = recurringTaskStateFactory.createEverydayRecurringTaskState();
		when(recurringTaskStateService.findById(1L)).thenReturn(Optional.of(recurringTaskState));
		
		AppUser appUser = appUserFactory.createOneAppUser();
		
		when(jwtUtil.extractUsernameFromCookies(any())).thenReturn("test@test.fr");
		when(appUserService.findByEmail("test@test.fr")).thenReturn(Optional.of(appUser));
		
		mockMvc.perform(put("/api/task/create_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError());
	}
	
	// ##### TASK UPDATE TESTS ######
	
	@Test
	void testUpdateTask_withValidData_returnsOk() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), existingTask.getTaskName());
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), existingTask.getTaskStartDate().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), existingTask.getTaskEndDate().toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), Integer.toString(existingTask.getTaskRequiredEnergy()));
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), existingTask.getTaskPriority().getTaskPriorityName());
		
		when(taskPriorityService.getTaskPriorityByTaskPriorityName(existingTask.getTaskPriority().getTaskPriorityName()))
			.thenReturn(Optional.of(existingTask.getTaskPriority()));
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(existingTask.getOrganizerUser().getAppUser()));
		
		when(taskService.updateTask(any(Task.class))).thenReturn(existingTask);
		
		mockMvc.perform(put("/api/task/update_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().isOk())
		.andExpect(content().string("Task updated successfully..."));
	}
	
	@Test
	void testUpdateTask_withValidDataAndAddRecurrentFrequency_returnsOk() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), existingTask.getTaskName());
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), existingTask.getTaskStartDate().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), existingTask.getTaskEndDate().toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), Integer.toString(existingTask.getTaskRequiredEnergy()));
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), existingTask.getTaskPriority().getTaskPriorityName());
		
		requestBody.put(ETaskControllerFields.TASK_IS_RECURRENT.getFieldName(), "true");
		requestBody.put(ETaskControllerFields.TASK_REPEAT_FREQUENCY_ID.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_REPEAT_END_DATE.getFieldName(), LocalDate.now().plusDays(7).toString());
		
		RecurringTaskState recurringTaskState = recurringTaskStateFactory.createEverydayRecurringTaskState();
		when(recurringTaskStateService.findById(1L)).thenReturn(Optional.of(recurringTaskState));
		
		when(taskPriorityService.getTaskPriorityByTaskPriorityName(existingTask.getTaskPriority().getTaskPriorityName()))
			.thenReturn(Optional.of(existingTask.getTaskPriority()));
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(existingTask.getOrganizerUser().getAppUser()));
		
		when(taskService.updateTask(any(Task.class))).thenReturn(existingTask);
		
		mockMvc.perform(put("/api/task/update_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().isOk())
		.andExpect(content().string("Task updated successfully..."));
	}
	
	@Test
	void testUpdateTask_withValidDataAndMarkTaskDone_returnsOk() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), existingTask.getTaskName());
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), existingTask.getTaskStartDate().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), existingTask.getTaskEndDate().toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), Integer.toString(existingTask.getTaskRequiredEnergy()));
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), existingTask.getTaskPriority().getTaskPriorityName());
		requestBody.put(ETaskControllerFields.TASK_IS_COMPLETED.getFieldName(), "true");
		
		when(taskPriorityService.getTaskPriorityByTaskPriorityName(existingTask.getTaskPriority().getTaskPriorityName()))
			.thenReturn(Optional.of(existingTask.getTaskPriority()));
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(existingTask.getOrganizerUser().getAppUser()));
		
		when(taskService.updateTask(any(Task.class))).thenReturn(existingTask);
		
		mockMvc.perform(put("/api/task/update_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().isOk())
		.andExpect(content().string("Task updated successfully..."));
	}
	
	@Test
	void testUpdateTask_withValidDataAndMarkTaskUndone_returnsOk() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		existingTask.setTaskCompleted(true);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), existingTask.getTaskName());
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), existingTask.getTaskStartDate().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), existingTask.getTaskEndDate().toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), Integer.toString(existingTask.getTaskRequiredEnergy()));
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), existingTask.getTaskPriority().getTaskPriorityName());
		requestBody.put(ETaskControllerFields.TASK_IS_COMPLETED.getFieldName(), "false");
		
		when(taskPriorityService.getTaskPriorityByTaskPriorityName(existingTask.getTaskPriority().getTaskPriorityName()))
			.thenReturn(Optional.of(existingTask.getTaskPriority()));
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(existingTask.getOrganizerUser().getAppUser()));
		
		when(taskService.updateTask(any(Task.class))).thenReturn(existingTask);
		
		mockMvc.perform(put("/api/task/update_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().isOk())
		.andExpect(content().string("Task updated successfully..."));
	}
	
	// ## Invalid data tests ##
	
	@Test
	void testUpdateTask_withUnknownUser_returnsBadRequest() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), existingTask.getTaskName());
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), existingTask.getTaskStartDate().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), existingTask.getTaskEndDate().toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), Integer.toString(existingTask.getTaskRequiredEnergy()));
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), existingTask.getTaskPriority().getTaskPriorityName());
		
		when(taskPriorityService.getTaskPriorityByTaskPriorityName(existingTask.getTaskPriority().getTaskPriorityName()))
			.thenReturn(Optional.of(existingTask.getTaskPriority()));
		
		AppUser differentAppUser = appUserFactory.createOneAppUser();
		differentAppUser.setUserId(10L);
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(differentAppUser));
		when(taskService.updateTask(any(Task.class))).thenReturn(existingTask);
		
		mockMvc.perform(put("/api/task/update_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testUpdateTask_withMissingMandatoryFields_returnsBadRequest() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), existingTask.getTaskStartDate().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), existingTask.getTaskEndDate().toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), Integer.toString(existingTask.getTaskRequiredEnergy()));
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), existingTask.getTaskPriority().getTaskPriorityName());
		
		when(taskPriorityService.getTaskPriorityByTaskPriorityName(existingTask.getTaskPriority().getTaskPriorityName()))
			.thenReturn(Optional.of(existingTask.getTaskPriority()));
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(existingTask.getOrganizerUser().getAppUser()));
		when(taskService.updateTask(any(Task.class))).thenReturn(existingTask);
		
		mockMvc.perform(put("/api/task/update_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testUpdateTask_withNonExistingUser_returnsBadRequest() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), existingTask.getTaskName());
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), existingTask.getTaskStartDate().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), existingTask.getTaskEndDate().toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), Integer.toString(existingTask.getTaskRequiredEnergy()));
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), existingTask.getTaskPriority().getTaskPriorityName());
		
		when(taskPriorityService.getTaskPriorityByTaskPriorityName(existingTask.getTaskPriority().getTaskPriorityName()))
			.thenReturn(Optional.of(existingTask.getTaskPriority()));
		
		when(taskService.updateTask(any(Task.class))).thenReturn(existingTask);
		
		mockMvc.perform(put("/api/task/update_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError())
		.andExpect(content().string(EErrorMessages.USER_NOT_FOUND.getMessage()));
	}
	
	@Test
	void testUpdateTask_withNumberFormatException_returnsBadRequest() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), existingTask.getTaskName());
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), existingTask.getTaskStartDate().toString());
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), existingTask.getTaskEndDate().toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), "two"); // Invalid number format
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), existingTask.getTaskPriority().getTaskPriorityName());
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(existingTask.getOrganizerUser().getAppUser()));
		when(taskPriorityService.getTaskPriorityByTaskPriorityName(existingTask.getTaskPriority().getTaskPriorityName()))
			.thenReturn(Optional.of(existingTask.getTaskPriority()));
		
		when(taskService.updateTask(any(Task.class))).thenReturn(existingTask);
		
		mockMvc.perform(put("/api/task/update_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError())
		.andExpect(content().string(EErrorMessages.INVALID_INPUT.getMessage()));
	}
	
	@Test
	void testUpdateTask_withInvalidDateFormat_returnsBadRequest() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		requestBody.put(ETaskControllerFields.TASK_NAME.getFieldName(), existingTask.getTaskName());
		requestBody.put(ETaskControllerFields.TASK_START_DATE.getFieldName(), "2024/10/10 10:00"); // Invalid date format
		requestBody.put(ETaskControllerFields.TASK_END_DATE.getFieldName(), existingTask.getTaskEndDate().toString());
		requestBody.put(ETaskControllerFields.TASK_REQUIRED_ENERGY.getFieldName(), Integer.toString(existingTask.getTaskRequiredEnergy()));
		requestBody.put(ETaskControllerFields.TASK_PRIORITY.getFieldName(), existingTask.getTaskPriority().getTaskPriorityName());
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(existingTask.getOrganizerUser().getAppUser()));
		when(taskPriorityService.getTaskPriorityByTaskPriorityName(existingTask.getTaskPriority().getTaskPriorityName()))
			.thenReturn(Optional.of(existingTask.getTaskPriority()));
		
		when(taskService.updateTask(any(Task.class))).thenReturn(existingTask);
		
		mockMvc.perform(put("/api/task/update_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is5xxServerError())
		.andExpect(content().string(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage()));
	}
	
	// ##### TASK DELETION TESTS ######
	
	@Test
	void testDeleteTask_withValidData_returnsOk() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(existingTask.getOrganizerUser().getAppUser()));
		
		mockMvc.perform(delete("/api/task/delete_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().isOk())
		.andExpect(content().string("Task deleted successfully..."));
	}
	
	// ## Invalid data tests ##
	
	@Test
	void testDeleteTask_withUnknownUser_returnsBadRequest() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		
		AppUser differentAppUser = appUserFactory.createOneAppUser();
		differentAppUser.setUserId(10L);
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(differentAppUser));
		
		mockMvc.perform(delete("/api/task/delete_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testDeleteTask_withMissingTaskId_returnsBadRequest() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(existingTask.getOrganizerUser().getAppUser()));
		
		mockMvc.perform(delete("/api/task/delete_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testDeleteTask_withNonExistingUser_returnsBadRequest() throws Exception {
		Task existingTask = taskFactory.createOneTaskWithOrganizerUserAndTaskPriority(organizerUserFactory, appUserFactory, taskPriorityFactory);
		when(taskService.getTaskById(1L)).thenReturn(existingTask);
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		
		when(appUserService.findByEmail(any())).thenReturn(null);
		
		mockMvc.perform(delete("/api/task/delete_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is5xxServerError())
		.andExpect(content().string(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage()));
	}
	
	@Test
	void testDeleteTask_withNonExistingTask_returnsBadRequest() throws Exception {
		when(taskService.getTaskById(1L)).thenReturn(null);
		
		AppUser appUser = appUserFactory.createOneAppUser();
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(appUser));
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "1");
		
		mockMvc.perform(delete("/api/task/delete_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError())
		.andExpect(content().string(EErrorMessages.RESOURCE_NOT_FOUND.getMessage()));
	}
	
	@Test
	void testDeleteTask_withInvalidTaskIdFormat_returnsBadRequest() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(ETaskControllerFields.TASK_ID.getFieldName(), "one"); // Invalid number format
		
		AppUser appUser = appUserFactory.createOneAppUser();
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(appUser));
		
		mockMvc.perform(delete("/api/task/delete_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError())
		.andExpect(content().string(EErrorMessages.INVALID_INPUT.getMessage()));
	}
	
	@Test
	void testDeleteTask_withEmptyRequestBody_returnsBadRequest() throws Exception {
		Map<String, String> requestBody = new HashMap<>();
		
		mockMvc.perform(delete("/api/task/delete_task")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
				)
		.andExpect(status().is4xxClientError())
		.andExpect(content().string(EErrorMessages.INVALID_INPUT.getMessage()));
	}
	
	// ##### GET ALL TASK RECURRING STATES TESTS ######
	
	@Test
	void testGetAllRecurringTaskStates_returnsOk() throws Exception {
		List<RecurringTaskState> recurringTaskStates = new ArrayList<>();
		recurringTaskStates.add(recurringTaskStateFactory.createEverydayRecurringTaskState());
		recurringTaskStates.add(recurringTaskStateFactory.createWeeklyRecurringTaskState());
		when(recurringTaskStateService.findAll()).thenReturn(recurringTaskStates);
		
		mockMvc.perform(get("/api/task/get_all_tasks_recurring_states")
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(2))
		.andExpect(jsonPath("$[0].display_name").value("Every Day"))
		.andExpect(jsonPath("$[1].display_name").value("Every Week"));
	}
	
	@Test
	void testGetAllRecurringTaskStates_withMoreData_returnsOk() throws Exception {
		List<RecurringTaskState> recurringTaskStates = new ArrayList<>();
		recurringTaskStates.add(recurringTaskStateFactory.createEverydayRecurringTaskState());
		recurringTaskStates.add(recurringTaskStateFactory.createWeeklyRecurringTaskState());
		recurringTaskStates.add(recurringTaskStateFactory.createEveryXDayRecurringTaskState(2));
		recurringTaskStates.add(recurringTaskStateFactory.createEveryXWeekRecurringTaskState(3));
		when(recurringTaskStateService.findAll()).thenReturn(recurringTaskStates);
		
		mockMvc.perform(get("/api/task/get_all_tasks_recurring_states")
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(4))
		.andExpect(jsonPath("$[0].display_name").value("Every Day"))
		.andExpect(jsonPath("$[1].display_name").value("Every Week"))
		.andExpect(jsonPath("$[2].display_name").value("Every 2 Day"))
		.andExpect(jsonPath("$[3].display_name").value("Every 3 Week"));
	}
	
	@Test
	void testGetAllRecurringTaskStates_withNoData_returnsOkWithEmptyList() throws Exception {
		when(recurringTaskStateService.findAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get("/api/task/get_all_tasks_recurring_states")
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(0));
	}
	
	// ##### GET ALL TASKS BY ORGANIZER USER AND DATE RANGE TESTS ######
	
	@Test
	void testGetTasksBetweenDates_returnsOk() throws Exception {
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser();
		AppUser appUser = appUserFactory.createOneAppUser();
		organizerUser.setAppUser(appUser);
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(appUser));
		
		Task task1 = taskFactory.createOneTaskWithTaskPriority(taskPriorityFactory);
		Task task2 = taskFactory.createOneTaskWithTaskPriority(taskPriorityFactory);
		task1.setOrganizerUser(organizerUser);
		task2.setOrganizerUser(organizerUser);
		List<Task> tasks = Arrays.asList(task1, task2);
		
		LocalDateTime startDate = LocalDateTime.now().minusDays(7);
		LocalDateTime endDate = LocalDateTime.now().plusDays(7);
		
		when(taskService.getAllTasksByUserIdAndDateRange(any(), eq(startDate), eq(endDate)))
			.thenReturn(tasks);
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("start_date", startDate.toString());
		requestBody.put("end_date", endDate.toString());
		
		mockMvc.perform(post("/api/task/get_tasks_between_dates")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
			)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(2));
	}
	
	// ## Invalid data tests ##
	
	@Test
	void testGetTasksBetweenDates_withInvalidUser_returnBadRequest() throws Exception {
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser();
		AppUser appUser = appUserFactory.createOneAppUser();
		organizerUser.setAppUser(appUser);
		
		Task task1 = taskFactory.createOneTaskWithTaskPriority(taskPriorityFactory);
		Task task2 = taskFactory.createOneTaskWithTaskPriority(taskPriorityFactory);
		task1.setOrganizerUser(organizerUser);
		task2.setOrganizerUser(organizerUser);
		List<Task> tasks = Arrays.asList(task1, task2);
		
		LocalDateTime startDate = LocalDateTime.now().minusDays(7);
		LocalDateTime endDate = LocalDateTime.now().plusDays(7);
		
		when(taskService.getAllTasksByUserIdAndDateRange(any(), eq(startDate), eq(endDate)))
			.thenReturn(tasks);
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("start_date", startDate.toString());
		requestBody.put("end_date", endDate.toString());
		
		mockMvc.perform(post("/api/task/get_tasks_between_dates")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
			)
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testGetTasksBetweenDates_withMissingData_returnBadRequest() throws Exception {
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser();
		AppUser appUser = appUserFactory.createOneAppUser();
		organizerUser.setAppUser(appUser);
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(appUser));
		
		Task task1 = taskFactory.createOneTaskWithTaskPriority(taskPriorityFactory);
		Task task2 = taskFactory.createOneTaskWithTaskPriority(taskPriorityFactory);
		task1.setOrganizerUser(organizerUser);
		task2.setOrganizerUser(organizerUser);
		List<Task> tasks = Arrays.asList(task1, task2);
		
		LocalDateTime startDate = LocalDateTime.now().minusDays(7);
		LocalDateTime endDate = LocalDateTime.now().plusDays(7);
		
		when(taskService.getAllTasksByUserIdAndDateRange(any(), eq(startDate), eq(endDate)))
			.thenReturn(tasks);
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("start_date", startDate.toString());
		
		mockMvc.perform(post("/api/task/get_tasks_between_dates")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
			)
		.andExpect(status().is4xxClientError())
		.andExpect(content().string(EErrorMessages.INVALID_INPUT.getMessage()));
	}
	
	@Test
	void testGetTasksBetweenDates_withWrongDateFormat_returnBadRequest() throws Exception {
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser();
		AppUser appUser = appUserFactory.createOneAppUser();
		organizerUser.setAppUser(appUser);
		
		when(appUserService.findByEmail(any())).thenReturn(Optional.of(appUser));
		
		Task task1 = taskFactory.createOneTaskWithTaskPriority(taskPriorityFactory);
		Task task2 = taskFactory.createOneTaskWithTaskPriority(taskPriorityFactory);
		task1.setOrganizerUser(organizerUser);
		task2.setOrganizerUser(organizerUser);
		List<Task> tasks = Arrays.asList(task1, task2);
		
		LocalDateTime startDate = LocalDateTime.now().minusDays(7);
		LocalDateTime endDate = LocalDateTime.now().plusDays(7);
		
		when(taskService.getAllTasksByUserIdAndDateRange(any(), eq(startDate), eq(endDate)))
			.thenReturn(tasks);
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("start_date", "2024/10/10 10:00"); // Invalid date format
		requestBody.put("end_date", "2024/10/10 10:00"); // Invalid date format
		
		mockMvc.perform(post("/api/task/get_tasks_between_dates")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
			)
		.andExpect(status().is5xxServerError())
		.andExpect(content().string(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage()));
	}
}
