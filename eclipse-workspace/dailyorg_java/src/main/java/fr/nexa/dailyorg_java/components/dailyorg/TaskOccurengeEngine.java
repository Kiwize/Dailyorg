package fr.nexa.dailyorg_java.components.dailyorg;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// This component is responsible to create any task occurrences
// When a change is made in the database (task table), this component will update the occurrences...
@Component
public class TaskOccurengeEngine {
	
	@EventListener
	public void handleTaskChangedEvent(TaskChangedEvent event) {
		// Handle the task change event
		// This method will be called when a task is created, updated or deleted
		System.out.println("Task change event received: " + event.getChangeType() + " for task ID: " + event.getTaskId());
		
		switch (event.getChangeType()) {
			case CREATED:
				// Logic to handle task creation
				break;
			case UPDATED:
				// Logic to handle task update
				break;
			case DELETED:
				// Logic to handle task deletion
				break;
			default:
				throw new IllegalArgumentException("Unknown task change type: " + event.getChangeType());
		}
	}
}