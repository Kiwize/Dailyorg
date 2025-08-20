package fr.nexa.dailyorg.model.dailyorg;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "RecurringTaskState")
public class RecurringTaskState {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long recurringTaskStateId;

	@Column(nullable = false)
	private int frequency;

	@Column(nullable = false, name = "time_interval")
    private int timeInterval; 

	

	@OneToMany(mappedBy = "recurringTaskState")
	@JsonIgnore
	private List<Task> tasks;

	public enum RecurringTaskFrequency {
		DAILY(0, "Day"), WEEKLY(1, "Week"), MONTHLY(2, "Month");

		private final int value;
		private final String label;

		RecurringTaskFrequency(int value, String label) {
			this.value = value;
			this.label = label;
		}

		public int getValue() {
			return value;
		}
		
		public String getLabel() {
			return label;
		}

		public static RecurringTaskFrequency fromValue(int value) {
			for (RecurringTaskFrequency frequency : values()) {
				if (frequency.getValue() == value) {
					return frequency;
				}
			}
			throw new IllegalArgumentException("Invalid RecurringTaskFrequency value: " + value);
		}
	}
}
