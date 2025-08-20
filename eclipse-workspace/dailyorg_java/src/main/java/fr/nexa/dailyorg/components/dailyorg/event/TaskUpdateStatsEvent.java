package fr.nexa.dailyorg.components.dailyorg.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class TaskUpdateStatsEvent {

	private final long organizerUser;
	private final int affectedTasksCount;
	private final int requiredEnergy;
	private final int priority;
	private final boolean isTaskMarkedDone;
	private final boolean didTaskCompletionStateChanged; // true if the task was marked done, false if it was unmarked done
}
