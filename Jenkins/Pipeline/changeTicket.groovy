String submitChangeTicket() {
    def changeTicket = input(
            message: "Would you like to deploy the app to AWS?",
            parameters: [
                    string(name: "CHANGE_TICKET", defaultValue: null, description: "Please enter a change request for deploying to Prod.")
            ])
    if (changeTicket == null) {
        throw new Exception("Please enter a valid changeTicket")
    }

    return changeTicket
}

return this