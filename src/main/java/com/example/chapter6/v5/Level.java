package com.example.chapter6.v5;

public enum Level {
    GOLD(3), SILVER(2), BASIC(1);

    private final int value;

    Level(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public Level nextLevel() {
        return switch (this) {
            case BASIC -> SILVER;
            case SILVER -> GOLD;
            case GOLD -> null;
        };
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
