package fr.nexa.dailyorg_java.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StrengthExercise extends Exercise {

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "trains", joinColumns = @JoinColumn(name = "strength_exercise_id"), inverseJoinColumns = @JoinColumn(name = "muscle_id"))
	@JsonIgnore
	private Set<Muscle> muscles;
}
