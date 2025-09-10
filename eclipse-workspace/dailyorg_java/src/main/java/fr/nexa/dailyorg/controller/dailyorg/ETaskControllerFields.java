package fr.nexa.dailyorg.controller.dailyorg;

public enum ETaskControllerFields {

    TASK_ID("task_id"),
    TASK_NAME("task_name"),
    TASK_DESCRIPTION("task_description"), 
    TASK_REQUIRED_ENERGY("task_required_energy"),
    TASK_START_DATE("task_start_date"),
    TASK_END_DATE("task_end_date"),
    TASK_IS_COMPLETED("is_task_completed"),

    TASK_PRIORITY("task_priority"),
    TASK_REPEAT_FREQUENCY_ID("task_repeat_frequency"),
    TASK_REPEAT_END_DATE("task_repeat_end_date"),
    TASK_IS_RECURRENT("is_recurrent"), TASK_CATEGORY_ID("task_category_id");

    private final String fieldName;

    ETaskControllerFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
