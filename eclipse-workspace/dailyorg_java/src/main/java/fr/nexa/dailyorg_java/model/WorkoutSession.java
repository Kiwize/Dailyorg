package fr.nexa.dailyorg_java.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "WorkoutSession")
public class WorkoutSession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idWorkoutSession;

	@Column(nullable = false)
	private Date workoutDate;

	@Column(nullable = true, length = 1024)
	private String notes;

	@ManyToOne
	@JoinColumn(name = "userId")
	@JsonIgnore
	private AppUser userId;

	@OneToMany(mappedBy = "workoutSession", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<WorkoutRecord> workoutRecords;

	@PrePersist
	void createdAt() {
		this.workoutDate = new Date();
	}
}
