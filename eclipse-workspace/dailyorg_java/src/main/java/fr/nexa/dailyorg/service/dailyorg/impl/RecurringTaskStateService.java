package fr.nexa.dailyorg.service.dailyorg.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.dailyorg.RecurringTaskState;
import fr.nexa.dailyorg.repository.dailyorg.IRecurringTaskStateRepository;
import fr.nexa.dailyorg.service.dailyorg.IRecurringTaskStateService;
import fr.nexa.dailyorg.utils.EErrorMessages;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecurringTaskStateService implements IRecurringTaskStateService {

	private final IRecurringTaskStateRepository recurringTaskStateRepository; 

	@Override
	public Optional<RecurringTaskState> findById(Long id) {
		if (id == null) {
			return Optional.empty();
		}
		return recurringTaskStateRepository.findById(id);
	}
	
	@Override
	public List<RecurringTaskState> findAllUniqueFrequencyAndTimeInterval() {
		List<RecurringTaskState> allRecurringTaskStates = new ArrayList<>();
		recurringTaskStateRepository.findAll().forEach(allRecurringTaskStates::add);
		
		List<RecurringTaskState> uniqueRecurringTaskStates = new ArrayList<>();

		for (RecurringTaskState recurringTaskState : allRecurringTaskStates) {
			if (uniqueRecurringTaskStates.stream().noneMatch(
					rt -> rt.getFrequency() == recurringTaskState.getFrequency() && 
					rt.getTimeInterval() == recurringTaskState.getTimeInterval())) {
				uniqueRecurringTaskStates.add(recurringTaskState);
			}
		}

		return uniqueRecurringTaskStates;
	}
	
	
	@Override
	public List<RecurringTaskState> findAll() {
		List<RecurringTaskState> allRecurringTaskStates = new ArrayList<>();
		recurringTaskStateRepository.findAll().forEach(allRecurringTaskStates::add);

		return allRecurringTaskStates;
	}

	@Override
	public RecurringTaskState update(RecurringTaskState recurringTaskState) throws IllegalArgumentException {
		if(recurringTaskState == null) {
			Logger.getLogger(RecurringTaskStateService.class.getName()).severe(EErrorMessages.NULL_VALUE.getMessage());
			throw new IllegalArgumentException(EErrorMessages.NULL_VALUE.getMessage());
		}
		return recurringTaskStateRepository.save(recurringTaskState);
	}

	@Override
	public void delete(RecurringTaskState recurringTaskState) throws IllegalArgumentException {
		if(recurringTaskState == null) {
			Logger.getLogger(RecurringTaskStateService.class.getName()).severe(EErrorMessages.NULL_VALUE.getMessage());
			throw new IllegalArgumentException(EErrorMessages.NULL_VALUE.getMessage());
		}
		recurringTaskStateRepository.delete(recurringTaskState);
	}
	
	public Optional<RecurringTaskState> findByFrequencyAndInterval(int frequency, int time_interval) {
	    return recurringTaskStateRepository.findByFrequencyAndTimeInterval(frequency, time_interval);
	}

}
