package fr.nexa.dailyorg.controller.dailyorg;

public enum ETaskControllerFields {

    TASK_ID("taskId"),
    TASK_NAME("taskName"),
    TASK_DESCRIPTION("taskDescription"), 
    TASK_REQUIRED_ENERGY("taskRequiredEnergy"),
    TASK_START_DATE("taskStartDate"),
    TASK_END_DATE("taskEndDate"),
    TASK_IS_COMPLETED("isTaskCompleted"),

    TASK_PRIORITY("taskPriority"),
    TASK_REPEAT_FREQUENCY_ID("taskRepeatFrequencyId"),
    TASK_REPEAT_END_DATE("taskRepeatEndDate"),
    TASK_IS_RECURRENT("isRecurrent"), TASK_CATEGORY_ID("taskCategoryId");

    private final String fieldName;

    ETaskControllerFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
