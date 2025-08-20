package fr.nexa.dailyorg.model.workout;

import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@Getter @Setter
@SuperBuilder
@Transactional
//@AllArgsConstructor
public class CardioRecord extends WorkoutRecord {
    private int recordTimeSpentInMins;
    private int recordIntensity;
    private int recordCaloriesBurnt;
}
