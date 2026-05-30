package com.srms;

/**
 * YearLevel - Enum used in the ChoiceBox.
 * getValue() returns the INT stored in the database (1, 2, 3, 4).
 */
public enum YearLevel {
    FIRST_YEAR(1),
    SECOND_YEAR(2),
    THIRD_YEAR(3),
    FOURTH_YEAR(4);

    private final int value;

    YearLevel(int value) {
        this.value = value;
    }

    /** Returns the integer stored in the database column year_level. */
    public int getValue() {
        return value;
    }

    /** Display label shown in the ChoiceBox. */
    @Override
    public String toString() {
        switch (this) {
            case FIRST_YEAR:  return "1st Year";
            case SECOND_YEAR: return "2nd Year";
            case THIRD_YEAR:  return "3rd Year";
            case FOURTH_YEAR: return "4th Year";
            default:          return "";
        }
    }

    /**
     * Converts a database INT value back to the matching enum constant.
     * Used when loading rows from the database into the ChoiceBox.
     */
    public static YearLevel fromInt(int value) {
        for (YearLevel y : values()) {
            if (y.value == value) return y;
        }
        return null;
    }
}
