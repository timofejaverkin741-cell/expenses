package com.example.expenses.model;

public enum Category {
    FOOD("Еда"),
    TRANSPORT("Транспорт"),
    UTILITIES("ЖКХ"),
    ENTERTAINMENT("Развлечения"),
    OTHER("Прочее");

    private final String title;

    Category(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
