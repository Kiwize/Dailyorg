package fr.nexa.dailyorg_java.components.dailyorg;

public enum ETaskPriorities {
	
	LOW("Low", 1),
	MEDIUM("Medium", 2),
	HIGH("High", 3),
	URGENT("Urgent", 4);

	private final String priorityName;
	private final int priorityLevel;
	
	ETaskPriorities(String priorityName, int priorityLevel) {
		this.priorityName = priorityName;
		this.priorityLevel = priorityLevel;
	}
	
	public String getPriorityName() {
		return priorityName;
	}
	
	public int getPriorityLevel() {
		return priorityLevel;
	}
	
	public static ETaskPriorities fromString(String priorityName) {
		for (ETaskPriorities priority : ETaskPriorities.values()) {
			if (priority.priorityName.equalsIgnoreCase(priorityName)) {
				return priority;
			}
		}
		throw new IllegalArgumentException("No enum constant with name " + priorityName);
	}

}
