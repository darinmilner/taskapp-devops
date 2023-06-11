class Errors {
    def throwError(Exception err, String message) {
        echo "$message Error: $err"
        echo err.getMessage()
        throw err
    }
}

return new Errors()