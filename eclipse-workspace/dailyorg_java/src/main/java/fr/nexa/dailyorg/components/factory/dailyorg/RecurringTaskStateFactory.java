package fr.nexa.dailyorg.components.factory.dailyorg;

import fr.nexa.dailyorg.model.dailyorg.RecurringTaskState;

public class RecurringTaskStateFactory {
	
	public RecurringTaskState createEverydayRecurringTaskState() {
		return RecurringTaskState.builder()
				.frequency(RecurringTaskState.RecurringTaskFrequency.DAILY.getValue())
				.timeInterval(1)
				.build();
	}
	
	public RecurringTaskState createEveryXDayRecurringTaskState(int x) {
		return RecurringTaskState.builder()
				.frequency(RecurringTaskState.RecurringTaskFrequency.DAILY.getValue())
				.timeInterval(x)
				.build();
	}
	
	public RecurringTaskState createWeeklyRecurringTaskState() {
		return RecurringTaskState.builder()
				.frequency(RecurringTaskState.RecurringTaskFrequency.WEEKLY.getValue())
				.timeInterval(1)
				.build();
	}
	
	public RecurringTaskState createEveryXWeekRecurringTaskState(int x) {
		return RecurringTaskState.builder()
				.frequency(RecurringTaskState.RecurringTaskFrequency.WEEKLY.getValue())
				.timeInterval(x)
				.build();
	}
	
	public RecurringTaskState createMonthlyRecurringTaskState() {
		return RecurringTaskState.builder()
				.frequency(RecurringTaskState.RecurringTaskFrequency.MONTHLY.getValue())
				.timeInterval(1)
				.build();
	}
	
	public RecurringTaskState createEveryXMonthRecurringTaskState(int x) {
		return RecurringTaskState.builder()
				.frequency(RecurringTaskState.RecurringTaskFrequency.MONTHLY.getValue())
				.timeInterval(x)
				.build();
	}
}
