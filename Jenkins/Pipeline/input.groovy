boolean getInput(String message, String description) {
//    boolean inputMessage = input(
//            message: message,
//            parameters: [
//                    booleanParam(defaultValue: true, description: description)
//            ])
//    return inputMessage
    String inputChoice
    choice(name: message, choices: ["proceed", "abort"], description: description)
    inputChoice = params.message
    echo "${params.message} chosen"
    return inputChoice
}

return this