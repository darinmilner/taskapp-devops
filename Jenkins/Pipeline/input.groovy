boolean getInput(String message, String description) {
    String inputChoice
    input(
            parameters: [
                    choice(name: "REGIONS", choices: ["All Regions", "Selected Regions"], description: description)
            ]
    )

    inputChoice = params.REGIONS
    echo "$inputChoice chosen"
    return inputChoice
}

return this