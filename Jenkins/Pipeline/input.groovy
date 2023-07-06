boolean getInput(String message, String description) {
    String inputChoice
    choice(name: "REGIONS", choices: ["All Regions", "Selected Regions"], description: description)
    inputChoice = params.REGIONS
    echo "$inputChoice chosen"
    return inputChoice
}

return this