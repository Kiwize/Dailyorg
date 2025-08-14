package fr.nexa.dailyorg_java.components.dailyorg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class TaskChangedEvent {
	
	public enum TaskChangeType {
		CREATED, UPDATED, DELETED
	}
	
	private final TaskChangeType changeType;
	private final long taskId;
	private final boolean recurringTaskStateChanged;
}
