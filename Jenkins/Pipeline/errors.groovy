class Errors {
    static void throwError(Exception err, String message) {
        echo "$message Error: $err"
        echo err.getMessage()
        throw err
    }
}