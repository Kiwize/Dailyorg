package fr.nexa.dailyorg.components.dailyorg;

import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import fr.nexa.dailyorg.components.dailyorg.event.TaskUpdateStatsEvent;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.service.dailyorg.impl.OrganizerUserService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TaskUpdateUserStats {

	private final OrganizerUserService organizerUserService;

	@EventListener
	public void handleTaskUpdateUserStats(TaskUpdateStatsEvent event) {
		if (event.isDidTaskCompletionStateChanged()) {
			int expDifference = (((event.getRequiredEnergy() * 10) * event.getPriority()) * event.getAffectedTasksCount()) * (event.isTaskMarkedDone() ? 1 : -1);

			Optional<OrganizerUser> organizerUserOpt = organizerUserService.findByAppUserId(event.getOrganizerUser());
			if (organizerUserOpt.isPresent()) {
				OrganizerUser organizerUser = organizerUserOpt.get();
				organizerUser.setExp_points(organizerUser.getExp_points() + expDifference);

				if (organizerUser.getExp_points() < 0) {
					organizerUser.setExp_points(0);
				}

				organizerUserService.update(organizerUser);
			}
		} else {
			if(event.isTaskMarkedDone()) {
				
			}
		}
	}
}
