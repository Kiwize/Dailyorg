package fr.nexa.dailyorg.components.factory.dailyorg;

import fr.nexa.dailyorg.model.dailyorg.RecurringTaskState;

public class RecurringTaskStateFactory {
	
	public RecurringTaskState createEverydayRecurringTaskState() {
		return RecurringTaskState.builder()
				.frequency(RecurringTaskState.RecurringTaskFrequency.DAILY.getValue())
				.timeInterval(1)
				.build();
	}
}
