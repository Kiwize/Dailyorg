package fr.nexa.dailyorg_java.model.dailyorg;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Builder
@Data
@Table(name = "RecurringTaskState")
public class RecurringTaskState {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long recurringTaskStateId;
	
	@Column(nullable = false)
	private int frequency;
	
	@Column(nullable = false)
	private int time_interval;
	
	@Column(nullable = true)
	private LocalTime preferredHour;
	
	@Column(nullable = true)
	private int weekday;
	
	@Column(nullable = true)
	private int monthday;
	
	@Column(nullable = false)
	private LocalDateTime recurrenceStartDate;
	
	@Column(nullable = false)
	private LocalDateTime recurrenceEndDate;
	
	@OneToMany(mappedBy = "recurringTaskState")
	private List<Task> tasks;
}
