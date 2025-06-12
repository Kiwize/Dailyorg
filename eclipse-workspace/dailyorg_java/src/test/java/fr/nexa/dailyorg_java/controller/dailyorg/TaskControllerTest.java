package fr.nexa.dailyorg_java.controller.dailyorg;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.model.dailyorg.Task;
import fr.nexa.dailyorg_java.service.AppUserService;
import fr.nexa.dailyorg_java.service.dailyorg.impl.OrganizerUserService;
import fr.nexa.dailyorg_java.service.dailyorg.impl.TaskOccurenceService;
import fr.nexa.dailyorg_java.service.dailyorg.impl.TaskService;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;
    @Mock
    private TaskOccurenceService taskOccurenceService;
    @Mock
    private AppUserService appUserService;
    @Mock
    private OrganizerUserService organizerUserService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void testCreateTaskWithNewProfile() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("task_name", "MyTask");
        data.put("task_required_energy", "5");
        data.put("user_email", "test@test.com");
        data.put("task_start_date", "2024-01-01T10:00:00");
        data.put("task_end_date", "2024-01-01T12:00:00");

        AppUser user = AppUser.builder().userId(1L).email("test@test.com").build();

        when(appUserService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(organizerUserService.create(any(OrganizerUser.class))).thenAnswer(inv -> {
            OrganizerUser ou = inv.getArgument(0);
            ou.setOrganizerUserId(1L);
            return ou;
        });
        when(appUserService.updateUser(any(AppUser.class))).thenReturn(user);
        when(taskService.addTask(any(Task.class))).thenReturn(Task.builder().taskId(1L).taskName("MyTask").build());

        ResponseEntity<?> response = taskController.createTask(data);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Task and profile created...");
    }

    @Test
    void testCreateTaskUserNotFound() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("task_name", "MyTask");
        data.put("task_required_energy", "5");
        data.put("user_email", "test@test.com");
        data.put("task_start_date", "2024-01-01T10:00:00");
        data.put("task_end_date", "2024-01-01T12:00:00");

        when(appUserService.findByEmail("test@test.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = taskController.createTask(data);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testUpdateTask() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("task_id", "1");
        data.put("task_name", "Updated");
        data.put("task_required_energy", "10");
        data.put("user_email", "test@test.com");
        data.put("task_start_date", "2024-01-01T10:00:00");
        data.put("task_end_date", "2024-01-01T12:00:00");

        AppUser user = AppUser.builder().userId(1L).email("test@test.com").build();
        OrganizerUser organizerUser = OrganizerUser.builder().organizerUserId(1L).appUser(user).build();
        user.setOrganizerUser(organizerUser);
        Task task = Task.builder().taskId(1L).taskName("Old").organizerUser(organizerUser)
                .taskRequiredEnergy(5)
                .taskStartDate(LocalDateTime.now()).taskEndDate(LocalDateTime.now())
                .build();

        when(appUserService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(taskService.getTaskById(1L)).thenReturn(task);
        when(taskService.updateTask(any(Task.class))).thenReturn(task);

        ResponseEntity<?> response = taskController.updateTask(data);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Task updated successfully...");
    }

    @Test
    void testDeleteTask() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("task_id", "1");
        data.put("user_email", "test@test.com");

        AppUser user = AppUser.builder().userId(1L).email("test@test.com").build();
        OrganizerUser organizerUser = OrganizerUser.builder().organizerUserId(1L).appUser(user).build();
        user.setOrganizerUser(organizerUser);
        Task task = Task.builder().taskId(1L).organizerUser(organizerUser).build();

        when(appUserService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(taskService.getTaskById(1L)).thenReturn(task);
        when(taskService.deleteTask(1L)).thenReturn(task);

        ResponseEntity<?> response = taskController.deleteTask(data);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Task deleted successfully...");
    }

    @Test
    void testGetTasksByDate() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("user_email", "test@test.com");
        data.put("date", "2024-01-01T10:00:00");

        AppUser user = AppUser.builder().userId(1L).email("test@test.com").build();
        OrganizerUser organizerUser = OrganizerUser.builder().organizerUserId(1L).appUser(user).build();
        user.setOrganizerUser(organizerUser);
        Task task = Task.builder().taskId(1L).taskName("MyTask").build();

        when(appUserService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(organizerUserService.findByAppUserId(user)).thenReturn(organizerUser);
        when(taskService.getAllTasksByUserIdAndDate(organizerUser, LocalDateTime.parse("2024-01-01T10:00:00")))
                .thenReturn(List.of(task));

        ResponseEntity<?> response = taskController.getTasksByDate(data);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(List.class);
    }
}

