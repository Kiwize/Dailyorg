package fr.nexa.dailyorg_java.utils;

public enum EErrorMessages {
	
	// General error messages
	USER_NOT_FOUND("[User not found] "),
	USER_ALREADY_EXISTS("[User already exists] "),
	INVALID_USER_ID("[Invalid user ID] "),
	INVALID_EMAIL_FORMAT("[Invalid email format] "),
	UNAUTHORIZED_ACCESS("[Unauthorized access] "),
	INTERNAL_SERVER_ERROR("[Internal server error] "),
	DATA_NOT_FOUND("[Data not found] "),
	INVALID_INPUT("[Invalid input provided] "),
	RESOURCE_NOT_FOUND("[Requested resource not found] "),
	
	//Workout related error messages
	INVALID_MUSCLE_NAME("[Invalid muscle name] "),
	INVALID_EXERCISE_TYPE("[Invalid exercise type] ");
	

	private final String message;

	EErrorMessages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
