package dataholder;

/**
 * An enum representing 538's pollster grades.
 */
public enum Grade {
    A, B, C, D;

    /**
     * Parse a dataholder.Grade object from a character. Not case-sensitive.
     *
     * @param grade A character representing a grade.
     * @return The corresponding grade, or null if the letter entered isn't A, B, C, or D.
     */
    public static Grade parseGrade(char grade) {
        switch (Character.toLowerCase(grade)) {
            case 'a':
                return A;
            case 'b':
                return B;
            case 'c':
                return C;
            case 'd':
                return D;
        }
        return null;
    }

    /**
     * Parse a dataholder.Grade object from the first character of a string. Not case-sensitive.
     *
     * @param grade A string representing a grade.
     * @return The corresponding grade, or null if the letter entered isn't A, B, C, or D.
     */
    public static Grade parseGrade(String grade) {
        return parseGrade(grade.charAt(0));
    }

    public static int indexGrade(Grade grade) {
        switch (grade) {
            case A:
                return 0;
            case B:
                return 1;
            case C:
                return 2;
            case D:
                return 3;
        }
        return 4;
    }
}
