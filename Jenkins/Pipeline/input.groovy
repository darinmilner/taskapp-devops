String getInput(String message, String name, String description, List<String> choices) {
    def inputMessage = input(
            message: message,
            parameters: [
                    string(name: name, choices: choices, description: description)
            ])
    return inputMessage
}

return this