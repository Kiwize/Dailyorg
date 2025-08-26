package fr.nexa.dailyorg.service.dailyorg.impl;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.nexa.dailyorg.components.factory.dailyorg.RecurringTaskStateFactory;
import fr.nexa.dailyorg.model.dailyorg.RecurringTaskState;
import fr.nexa.dailyorg.repository.dailyorg.IRecurringTaskStateRepository;
import fr.nexa.dailyorg.utils.EErrorMessages;

@ExtendWith(MockitoExtension.class)
public class RecurringTaskStateServiceTest {

	@InjectMocks
	private RecurringTaskStateService recurringTaskStateService;

	@Mock
	private IRecurringTaskStateRepository recurringTaskStateRepository;

	// Factories
	private final RecurringTaskStateFactory recurringTaskStateFactory = new RecurringTaskStateFactory();

	@Test
	void testFindById() {
		RecurringTaskState recurringTaskState = recurringTaskStateFactory.createEverydayRecurringTaskState();

		when(recurringTaskStateRepository.findById(1L)).thenReturn(Optional.of(recurringTaskState));

		Optional<RecurringTaskState> found = recurringTaskStateService.findById(1L);

		assert (found.isPresent());
		assertEquals(recurringTaskState, found.get());
	}

	@Test
	void testFindById_idIsNull() {
		Optional<RecurringTaskState> found = recurringTaskStateService.findById(null);

		assert (found.isEmpty());
	}

	@Test
	void testFindAllUniqueFrequencyAndTimeInterval() {
		RecurringTaskState recurringTaskState1 = recurringTaskStateFactory.createEverydayRecurringTaskState();
		RecurringTaskState recurringTaskState2 = recurringTaskStateFactory.createWeeklyRecurringTaskState();
		RecurringTaskState recurringTaskState3 = recurringTaskStateFactory.createEverydayRecurringTaskState(); // Duplicate frequency and timeInterval

		when(recurringTaskStateRepository.findAll()).thenReturn(java.util.List.of(recurringTaskState1, recurringTaskState2, recurringTaskState3));

		java.util.List<RecurringTaskState> found = recurringTaskStateService.findAllUniqueFrequencyAndTimeInterval();

		assertEquals(2, found.size());
		assert (found.contains(recurringTaskState1));
		assert (found.contains(recurringTaskState2));
	}

	@Test
	void testFindAll() {
		RecurringTaskState recurringTaskState1 = recurringTaskStateFactory.createEverydayRecurringTaskState();
		RecurringTaskState recurringTaskState2 = recurringTaskStateFactory.createWeeklyRecurringTaskState();

		when(recurringTaskStateRepository.findAll()).thenReturn(java.util.List.of(recurringTaskState1, recurringTaskState2));

		java.util.List<RecurringTaskState> found = recurringTaskStateService.findAll();

		assertEquals(2, found.size());
		assert (found.contains(recurringTaskState1));
		assert (found.contains(recurringTaskState2));
	}

	@Test
	void testFindByFrequencyAndTimeInterval() {
		RecurringTaskState recurringTaskState = recurringTaskStateFactory.createEverydayRecurringTaskState();

		when(recurringTaskStateRepository.findByFrequencyAndTimeInterval(recurringTaskState.getFrequency(), recurringTaskState.getTimeInterval())).thenReturn(Optional.of(recurringTaskState));

		Optional<RecurringTaskState> found = recurringTaskStateService.findByFrequencyAndInterval(recurringTaskState.getFrequency(), recurringTaskState.getTimeInterval());

		assert (found.isPresent());
		assertEquals(recurringTaskState, found.get());
	}

	@Test
	void testUpdate() {
		RecurringTaskState recurringTaskState = recurringTaskStateFactory.createEverydayRecurringTaskState();

		when(recurringTaskStateRepository.save(recurringTaskState)).thenReturn(recurringTaskState);

		RecurringTaskState updated = recurringTaskStateService.update(recurringTaskState);

		assertEquals(recurringTaskState, updated);
		verify(recurringTaskStateRepository).save(recurringTaskState);
	}

	@Test
	void testUpdate_recurringTaskStateIsNull() {
		assertThatIllegalArgumentException().isThrownBy(() -> recurringTaskStateService.update(null)).withMessage(EErrorMessages.NULL_VALUE.getMessage());
	}

	@Test
	void testCreate() {
		RecurringTaskState recurringTaskState = recurringTaskStateFactory.createEverydayRecurringTaskState();

		when(recurringTaskStateRepository.save(recurringTaskState)).thenReturn(recurringTaskState);

		RecurringTaskState created = recurringTaskStateService.create(recurringTaskState);

		assertEquals(recurringTaskState, created);
		verify(recurringTaskStateRepository).save(recurringTaskState);
	}

	@Test
	void testCreate_recurringTaskStateIsNull() {
		assertThatIllegalArgumentException().isThrownBy(() -> recurringTaskStateService.create(null)).withMessage(EErrorMessages.NULL_VALUE.getMessage());
	}

	@Test
	void testDelete() {
		RecurringTaskState recurringTaskState = recurringTaskStateFactory.createEverydayRecurringTaskState();

		recurringTaskStateService.delete(recurringTaskState);

		verify(recurringTaskStateRepository).delete(recurringTaskState);
	}

	@Test
	void testDelete_recurringTaskStateIsNull() {
		assertThatIllegalArgumentException().isThrownBy(() -> recurringTaskStateService.delete(null)).withMessage(EErrorMessages.NULL_VALUE.getMessage());
	}

}
