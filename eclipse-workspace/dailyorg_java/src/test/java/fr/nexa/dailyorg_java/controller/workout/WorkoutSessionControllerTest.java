package fr.nexa.dailyorg_java.controller.workout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
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
import fr.nexa.dailyorg_java.model.workout.WorkoutSession;
import fr.nexa.dailyorg_java.service.AppUserService;
import fr.nexa.dailyorg_java.service.workout.impl.WorkoutRecordService;
import fr.nexa.dailyorg_java.service.workout.impl.WorkoutSessionService;
import fr.nexa.dailyorg_java.service.workout.impl.StrengthExerciseService;
import fr.nexa.dailyorg_java.service.workout.impl.CardioExerciseService;
import fr.nexa.dailyorg_java.service.workout.impl.ExerciseService;

@ExtendWith(MockitoExtension.class)
public class WorkoutSessionControllerTest {

    @Mock
    private WorkoutRecordService workoutRecordService;
    @Mock
    private WorkoutSessionService workoutSessionService;
    @Mock
    private StrengthExerciseService strengthExerciseService;
    @Mock
    private CardioExerciseService cardioExerciseService;
    @Mock
    private AppUserService appUserService;
    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private WorkoutSessionController workoutSessionController;

    @Test
    void testCreateWorkout() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("email", "test@test.com");

        AppUser user = AppUser.builder().userId(1L).email("test@test.com").build();
        when(appUserService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(workoutSessionService.addWorkoutSession(any(WorkoutSession.class))).thenReturn(new WorkoutSession());

        ResponseEntity<String> response = workoutSessionController.createWorkout(data);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testDeleteWorkout() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("email", "test@test.com");
        data.put("workout_session_id", "1");

        ResponseEntity<String> response = workoutSessionController.deleteWorkout(data);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
