package fr.nexa.dailyorg.components.dailyorg;

import org.springframework.stereotype.Component;

import fr.nexa.dailyorg.model.dailyorg.RecurringTaskState;
import fr.nexa.dailyorg.service.dailyorg.impl.RecurringTaskStateService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TaskRecurringTaskStateSeeder {

	private final RecurringTaskStateService taskRecurringTaskStateService;

	@PostConstruct
	public void populateRecurringTaskStates() {
		// If empty, populate with default values
		if (taskRecurringTaskStateService.findAll().isEmpty()) {
			taskRecurringTaskStateService.create(RecurringTaskState.builder().frequency(RecurringTaskState.RecurringTaskFrequency.DAILY.getValue()).timeInterval(1).build());
			taskRecurringTaskStateService.create(RecurringTaskState.builder().frequency(RecurringTaskState.RecurringTaskFrequency.WEEKLY.getValue()).timeInterval(1).build());
			taskRecurringTaskStateService.create(RecurringTaskState.builder().frequency(RecurringTaskState.RecurringTaskFrequency.MONTHLY.getValue()).timeInterval(1).build());
			
			taskRecurringTaskStateService.create(RecurringTaskState.builder().frequency(RecurringTaskState.RecurringTaskFrequency.DAILY.getValue()).timeInterval(2).build());
			taskRecurringTaskStateService.create(RecurringTaskState.builder().frequency(RecurringTaskState.RecurringTaskFrequency.WEEKLY.getValue()).timeInterval(2).build());
			
			taskRecurringTaskStateService.create(RecurringTaskState.builder().frequency(RecurringTaskState.RecurringTaskFrequency.DAILY.getValue()).timeInterval(3).build());
			taskRecurringTaskStateService.create(RecurringTaskState.builder().frequency(RecurringTaskState.RecurringTaskFrequency.WEEKLY.getValue()).timeInterval(3).build());

		
			System.out.println("RecurringTaskStates populated with default values.");
		}
	}

}
