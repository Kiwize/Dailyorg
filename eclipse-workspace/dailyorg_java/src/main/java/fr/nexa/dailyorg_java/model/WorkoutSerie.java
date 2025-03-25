package fr.nexa.dailyorg_java.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSerie {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long workoutSerieID;
	
	private float repWeight;
    private int repNumber;
    private int restTimeInMins;
    
    @ManyToOne
    @JoinColumn(name = "strengthRecordId")
    @JsonIgnore
    private StrengthRecord strengthRecordId;
	
}
