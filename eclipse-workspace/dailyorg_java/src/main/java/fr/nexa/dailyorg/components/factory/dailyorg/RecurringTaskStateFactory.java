package fr.nexa.dailyorg.components.factory.dailyorg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.nexa.dailyorg.model.dailyorg.RecurringTaskState;
import fr.nexa.dailyorg.repository.dailyorg.IRecurringTaskStateRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class RecurringTaskStateFactory {
	
	private final IRecurringTaskStateRepository recurringTaskStateRepository;
	
	public RecurringTaskState createOneRecurringTaskState() {
		return RecurringTaskState.builder()
				.frequency(RecurringTaskState.RecurringTaskFrequency.DAILY.getValue())
				.timeInterval(1)
				.build();
	}
	
	public RecurringTaskState createAndInsertOneRecurringTaskState() {
		RecurringTaskState recurringTaskState = createOneRecurringTaskState();
		return recurringTaskStateRepository.save(recurringTaskState); 
	}

}
