class Errors {
    def throwError(Exception err, String message) {
        println "$message Error: $err"
        println err.getMessage()
        throw err
    }
}

return new Errors()