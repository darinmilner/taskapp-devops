boolean getInput(String message, String description) {
    String inputChoice = input(
            parameters: [
                    choice(name: "REGIONS", choices: ["ALL_REGIONS", "SELECTED_REGION"], description: description)
            ]
    )

    echo "$inputChoice chosen"
    return inputChoice
}

return this