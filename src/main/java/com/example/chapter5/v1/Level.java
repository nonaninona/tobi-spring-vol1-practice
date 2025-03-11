package com.example.chapter5.v1;

public enum Level {
    BASIC(1), SILVER(2), GOLD(3);

    private final int value;

    Level(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public static Level valueOf(int value) {
        return switch (value) {
            case 1 -> Level.BASIC;
            case 2 -> Level.SILVER;
            case 3 -> Level.GOLD;
            default -> throw new AssertionError("Unknown value : " + value);
        };
    }
}
