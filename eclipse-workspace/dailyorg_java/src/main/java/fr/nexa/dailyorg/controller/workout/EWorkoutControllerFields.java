package fr.nexa.dailyorg.controller.workout;

public enum EWorkoutControllerFields {

	//Exercise types
	CARDIO("cardio"),
	STRENGTH("strength"),
	
	//Controller fields for workout-related operations
    WORKOUT_SESSION_ID("workout_session_id"),
    MUSCLE_NAME("muscle_name"),
    EXERCISE_ID("exercise_id"),
    EXERCISE_TYPE("exercise_type"),
    TIME_SPENT_IN_MINS("time_spent_in_mins"),
    INTENSITY("intensity"),
    CALORIES_BURNT("calories_burnt");

    private String fieldName;

    EWorkoutControllerFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

}
