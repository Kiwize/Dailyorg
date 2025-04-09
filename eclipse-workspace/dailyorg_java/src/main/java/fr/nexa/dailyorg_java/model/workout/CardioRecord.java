package fr.nexa.dailyorg_java.model.workout;

import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@Transactional
//@AllArgsConstructor
public class CardioRecord extends WorkoutRecord {
    private int recordTimeSpentInMins;
    private int recordIntensity;
    private int recordCaloriesBurnt;
}
