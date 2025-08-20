package fr.nexa.dailyorg.repository.dailyorg;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg.model.dailyorg.RecurringTaskState;

@Repository
public interface IRecurringTaskStateRepository extends CrudRepository<RecurringTaskState, Long> {
	
	Optional<RecurringTaskState> findByFrequencyAndTimeInterval(int frequency, int time_interval);

	
}
