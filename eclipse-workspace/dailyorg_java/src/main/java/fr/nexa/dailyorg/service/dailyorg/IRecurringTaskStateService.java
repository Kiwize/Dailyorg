package fr.nexa.dailyorg.service.dailyorg;

import java.util.List;
import java.util.Optional;

import fr.nexa.dailyorg.model.dailyorg.RecurringTaskState;

public interface IRecurringTaskStateService {
	
	public Optional<RecurringTaskState> findById(Long id);
	public List<RecurringTaskState> findAllUniqueFrequencyAndTimeInterval();
	List<RecurringTaskState> findAll();
	public RecurringTaskState update(RecurringTaskState recurringTaskState);
	public RecurringTaskState create(RecurringTaskState recurringTaskState);
	public void delete(RecurringTaskState recurringTaskState);
	public Optional<RecurringTaskState> findByFrequencyAndInterval(int frequency, int timeInterval);

}
