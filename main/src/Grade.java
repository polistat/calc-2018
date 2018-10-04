public enum Grade {
    A,B,C,D;

    public static Grade parseGrade(char grade){
        switch (Character.toLowerCase(grade)){
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

    public static Grade parseGrade(String grade){
        return parseGrade(grade.charAt(0));
    }
}
