package main

import definitions.NumberPairingProblem


fun main(args : Array<String>) {
    // Get user input from command line, prompt or default
    val sumFromCommandLineInput = NumberPairingProblem.getUserInput(args)

    val collectOtherResultsFromCLI = NumberPairingProblem.lookForSecondCommand(args)

    // Get the result and print
    val twoNumbersProblem = NumberPairingProblem(
        sumOfNumberPairing = sumFromCommandLineInput,
        collectOtherResults = collectOtherResultsFromCLI
    )
    twoNumbersProblem.printAllResults()
}