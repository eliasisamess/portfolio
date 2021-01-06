import os

# This function clears the terminal screen for a better user experience.
def clear(): 
    # For windows operating systems 
    if os.name == 'nt': 
        os.system('cls') 
    # For mac and linux operating systems (here, os.name is 'posix') 
    else: 
        os.system('clear')

# This function asks for player names in the beginning of the game and returns them as a list.
def askPlayerNames():
    p1_name = input("Please enter player 1 name: ")
    while not checkGivenName(p1_name):
        p1_name = input("Please try again (Your name must be 3-20 characters long): ")
    p2_name = input("Now enter player 2 name: ")
    while not checkGivenName(p2_name):
        p2_name = input("Please try again (Your name must be 3-20 characters long): ")
    thisListOfNames = [p1_name, p2_name]
    return thisListOfNames

# This function checks that the given name is between 3 to 20 characters long.
def checkGivenName(givenName):
    return len(givenName) > 2 and len(givenName) < 21

# This function prints the gameboard UI.
def printBoard(board):
    clear()
    for x in board:
        print(' '.join(str(y) for y in x))
    print("\n")

# This function checks if the gameboard is full.
def boardIsFull(thisBoard):
    if thisBoard[1][1] == "_" or thisBoard[1][2] == "_" or thisBoard[1][3] == "_" or thisBoard[2][1] == "_" or thisBoard[2][2] == "_" or thisBoard[2][3] == "_" or thisBoard[3][1] == "_" or thisBoard[3][2] == "_" or thisBoard[3][3] == "_":
        return False
    else:
        return True

# This function checks the gameboard for winners and returns the result code of the game.
#
# Function returns an integer value between 0-7:
#
#   0 = Undefined, game is still on
#   1 = X won with a horizontal streak.
#   2 = O won with a horizontal streak.
#   3 = X won with a vertical streak.
#   4 = O won with a vertical streak.
#   5 = X won with a diagonal streak.
#   6 = O won with a diagonal streak.
#   7 = Tie, no empty spots left, but now winner.
#
def checkBoard(board):
    result = 0
    i = 1
    j = 1
    checkContinues = True
    while i <= 3:
        stringToCheck = board[i][1] + board[i][2] + board[i][3]
        if stringToCheck == "XXX":
            result = 1
            checkContinues = False
            break
        elif stringToCheck == "OOO":
            result = 2
            checkContinues = False
            break
        i += 1
    while j <= 3:
        stringToCheck = board[1][j] + board[2][j] + board[3][j]
        if stringToCheck == "XXX":
            result = 3
            checkContinues = False
            break
        elif stringToCheck == "OOO":
            result = 4
            checkContinues = False
            break
        j += 1
    if checkContinues:
        diagonalCheck1 = board[1][1] + board[2][2] + board[3][3]
        diagonalCheck2 = board[3][1] + board[2][2] + board[1][3]
        if diagonalCheck1 == "XXX" or diagonalCheck2 == "XXX":
            result = 5
            checkContinues = False
        elif diagonalCheck1 == "OOO" or diagonalCheck2 == "OOO":
            result = 6
            checkContinues = False
    if checkContinues and boardIsFull(board):
        result = 7
    return result

# Function for checking if the given gameboard spot is empty.
def emptySpot(board, coordinates):
    x = int(coordinates[0])
    y = int(coordinates[1])
    if board[y][x] == "_":
        return True
    else:
        return False

# Function for entering a character to the gameboard.
def enterCharacterToGameBoard(board, coordinates, gameChar):
    x = int(coordinates[0])
    y = int(coordinates[1])
    board[y][x] = gameChar
    return board

# Function to check if given coordinates are proper values (between 1 and 3).
def properCoordinates(coordinates):
    coordinates[0] = int(coordinates[0])
    coordinates[1] = int(coordinates[1])
    return coordinates[0] < 4 and coordinates[0] > 0 and coordinates[1] < 4 and coordinates[1] > 0

# Function for cleaning the formatting of user input.
def cleanThis(userInput):
    userInput = userInput.replace(" ", "")
    userInput = userInput.split(",")
    return userInput

# Function for one playturn.
def playerTurn(board, playername, gamechar):
    while True:
        try:
            # Get user input for coordinates
            userInput = input("Player " + playername + " (" + gamechar + ") enter coordinates for your move: ")
            # Clean coordinate formatting
            userInput = cleanThis(userInput)
            while not properCoordinates(userInput):
                userInput = input("Try again " + playername + ", those are not proper coordinates: ")
                userInput = cleanThis(userInput)
            # Check if the spot is empty
            while not emptySpot(board, userInput):
                userInput = input("Sharpen up " + playername + ", it was not an empty spot.")
                userInput = cleanThis(userInput)
            # If the spot is empty, add character
            if emptySpot(board, userInput):
                return enterCharacterToGameBoard(board, userInput, gamechar)
        except IndexError:
            print("Those numbers don't match with the gameboard.")
        except ValueError:
            print("You did not enter proper coordinates.")
        except TypeError:
            print("That's not what I asked for.")
        except:
            print("Try again.")

# This function prints the end result of the game, according to the code returned with checkBoard().
def switchPrintResult(resultNumber, player1_name, player2_name):
    switcher = {
        1: "Player " + player1_name + " (X) won with a horizontal streak!",
        2: "Player " + player2_name + " (O) won with a horizontal streak!",
        3: "Player " + player1_name + " (X) won with a vertical streak!",
        4: "Player " + player2_name + " (O) won with a vertical streak!",
        5: "Player " + player1_name + " (X) won with a diagonal streak!",
        6: "Player " + player2_name + " (O) won with a diagonal streak!",
        7: "It's a tie, no one wins."
    }
    print(switcher.get(resultNumber, "Internal error."))

# Game starts here
clear()
theActualGameBoard =    [
                        [" ", "1", "2", "3"],
                        ["1", "_", "_", "_"],
                        ["2", "_", "_", "_"],
                        ["3", "_", "_", "_"]
                        ]
print("Welcome to the game!")
playerNames = askPlayerNames()
player1_name = playerNames[0]
player2_name = playerNames[1]
while not boardIsFull(theActualGameBoard):
    printBoard(theActualGameBoard)
    theActualGameBoard = playerTurn(theActualGameBoard, player1_name, "X")
    if checkBoard(theActualGameBoard) != 0:
        break
    printBoard(theActualGameBoard)
    theActualGameBoard = playerTurn(theActualGameBoard, player2_name, "O")
    if checkBoard(theActualGameBoard) != 0:
        break
resultNumber = checkBoard(theActualGameBoard)
printBoard(theActualGameBoard)
switchPrintResult(resultNumber, player1_name, player2_name)
print("Thanks for playing! \n")
