package fr.nexa.dailyorg_java.model.workout;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@Getter @Setter
@SuperBuilder
@AllArgsConstructor
public class StrengthRecord extends WorkoutRecord {
	
	@OneToMany(mappedBy = "strengthRecordId", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutSerie> workoutSeries;
	
	@Override
	public boolean equals(Object o) {
		if(!super.equals(o)) return false;
		if (!(o instanceof StrengthRecord)) return false;
		StrengthRecord that = (StrengthRecord) o;
		return workoutSeries.equals(that.getWorkoutSeries());
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}

