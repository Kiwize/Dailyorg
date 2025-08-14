package fr.nexa.dailyorg_java.controller.dailyorg;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.model.dailyorg.Task;
import fr.nexa.dailyorg_java.service.AppUserService;
import fr.nexa.dailyorg_java.service.dailyorg.impl.OrganizerUserService;
import fr.nexa.dailyorg_java.service.dailyorg.impl.TaskService;

@WebMvcTest(TaskController.class)
public class TaskControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;

	@MockBean
	private AppUserService appUserService;

	@MockBean
	private OrganizerUserService organizerUserService;

	@Autowired
	private ObjectMapper objectMapper;

	private AppUser appUser;
	private OrganizerUser organizerUser;

	@BeforeEach
	void setup() {
		appUser = new AppUser();
		appUser.setUserId(1L);
		appUser.setEmail("user@example.com");

		organizerUser = OrganizerUser.builder().appUser(appUser).energy_profile("Normal").level(1).exp_points(0)
				.build();

		appUser.setOrganizerUser(organizerUser);
	}

	@Test
	void createTask_shouldReturn200_whenInputIsValid() throws Exception {
		Map<String, String> payload = Map.of("task_name", "Test Task", "task_required_energy", "50", "user_email",
				"user@example.com", "task_start_date", "2023-06-01T10:00", "task_end_date", "2023-06-01T11:00");

		when(appUserService.findByEmail("user@example.com")).thenReturn(Optional.of(appUser));

		mockMvc.perform(put("/api/task/create_task").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payload))).andExpect(status().isOk())
				.andExpect(content().string("Task created..."));

		verify(taskService, times(1)).addTask(any(Task.class));
	}

	@Test
	void createTask_shouldReturn400_whenMissingFields() throws Exception {
		Map<String, String> payload = Map.of("user_email", "user@example.com");

		mockMvc.perform(put("/api/task/create_task").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payload))).andExpect(status().isBadRequest())
				.andExpect(content().string("One or multiple mandatory fields are missing..."));
	}

	@Test
	void createTask_shouldReturn400_whenUserNotFound() throws Exception {
		Map<String, String> payload = Map.of("task_name", "Test Task", "task_required_energy", "50", "user_email",
				"notfound@example.com", "task_start_date", "2023-06-01T10:00", "task_end_date", "2023-06-01T11:00");

		when(appUserService.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

		mockMvc.perform(put("/api/task/create_task").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payload))).andExpect(status().isBadRequest())
				.andExpect(content().string("User not found..."));
	}

	@Test
	void createTask_shouldReturn400_whenInvalidEnergyFormat() throws Exception {
		Map<String, String> payload = Map.of("task_name", "Test Task", "task_required_energy", "not_a_number",
				"user_email", "user@example.com", "task_start_date", "2023-06-01T10:00", "task_end_date",
				"2023-06-01T11:00");

		when(appUserService.findByEmail("user@example.com")).thenReturn(Optional.of(appUser));

		mockMvc.perform(put("/api/task/create_task").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payload))).andExpect(status().isBadRequest())
				.andExpect(content().string("Invalid number format for task_required_energy..."));
	}
}