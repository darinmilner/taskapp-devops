String getInput(String message, String name, String description) {
    def inputMessage = input(
            message: message,
            parameters: [
                    string(name: name, defaultValue: null, description: description)
            ])
    return inputMessage
}

return this