String submitChangeTicket() {
    def changeTicket = input(
            message: "Would you like to deploy the app to AWS?",
            parameters: [
                    string(name: "CHANGE_TICKET", defaultValue: null, description: "Please enter a change request for deploying to Prod.")
            ])
    if (changeTicket == null) {
        throw new Exception("Please enter a valid changeTicket")
    }

    boolean isValid = validateChangeTicket(changeTicket)
    //TODO: change to try catch and throw an exception for invalid change ticket
    if (isValid) {
        echo "Change ticket is valid"
    } else {
        echo "Change ticket is not valid."
    }

    return changeTicket
}

boolean validateChangeTicket(String changeTicket) {
    Date now = new Date()
    def thisYear = now["year"]
    echo "$thisYear"
    boolean isValid = changeTicket.startsWith("CH") && changeTicket.contains(thisYear)
    return isValid
}

return this