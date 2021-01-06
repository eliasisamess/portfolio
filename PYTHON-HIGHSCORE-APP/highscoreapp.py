#!/usr/bin/python3

import cgi, config, csv, json, os

__author__ = "Elias Puukari"
__copyright__ = "Copyright 2020"

# This program will handle a high score list for a game. It was originally created for a school project mobile game JUST DIGGING (http://19tiko1b.projects.tamk.fi/).
#
# Author    Elias Puukari
# Version   1.0
# Date      19/04/2020
#
# Specific error codes returned from this program in JSON are:
#
# | ERROR CODE | DESCRIPTION
# |     0      | No errors.
# |     1      | Authentication error, no access to server (wrong username or password).
# |     2      | Tried to add a score entry which is smaller than the lowest entry on the current high score list.
# |     3      | File not found.
# |     4      | Imported module not found.
# |     5      | Sequence index out of range.
# |     6      | Variable not found in local or global scale.
# |     7      | ValueError. Wrong value was given.
# |     8      | Unknown error.
#

# Function to read the high score database (CSV) and return it as a list
def readMyCsvFile():

    # Retrieve the data from CSV file
    with open("scores.csv", newline="") as fileToRead:
        readingThis = csv.reader(fileToRead)
        retrievedHighScores = list(readingThis)

    # Copy the retrieved list to a new list
    convertedHighScores = retrievedHighScores

    # Convert the scores column to integers
    i = 0
    while i < 10:
        convertedHighScores[i][1] = int(retrievedHighScores[i][1])
        i += 1

    # Return the retrieved and converted scores as a list 
    return convertedHighScores

# Function to help sorting list according to scores column
def sortingHelper(e):
    return e[1]

# Function to doublecheck if the score made it to the list (this check is also done at the game before sending anything to server)
def checkIfPlayerMadeItToTheList(playerScore, lowestScore):
    if lowestScore >= playerScore:
        return False
    else:
        return True

# This function reads the high scores from server and sends them back as JSON string
def getHighScoresFromServer():

    # Get high scores from server with readMyCsvFile function
    highScoresFromServer = readMyCsvFile()

    # Print the converted high scores as JSON string
    print(json.dumps(highScoresFromServer))

# This function adds the player to high score if their score exceeds the lowest score on the current list
def addNewEntryToHighScores(user, password, player, score):

    # Verify the given username and password
    validPassword = config.verify_password(user, password)

    # If authentication fails, show error message.
    if not validPassword:
        message = "Authentication failed! No access granted to the server. The player " + player + " was not added to the high score list."
        response = [
            {"error": 1},
            {"message": message}
        ]
        print(json.dumps(response))

    # If authentication succeeds, add player to the high score list.
    if validPassword:

        # Define variables from the given parameters and convert them to string and int just to make sure they are correct for later use
        currentPlayerName = str(player)
        currentPlayerScore = int(score)

        # Get high scores from server with readMyCsvFile function
        highScoresFromServer = readMyCsvFile()

        # Define the lowest score found from the bottom of the list, index[9][1]
        currentLowestScore = highScoresFromServer[9][1]

        # Create a boolean to doublecheck if the player really made it to the list
        betterScore = checkIfPlayerMadeItToTheList(currentPlayerScore, currentLowestScore)

        # If doublecheck succeeds go forward
        if betterScore:

            # Create a new temporary copy of the list
            tempHighScores = highScoresFromServer

            # Delete the lowest score entry from the temporary list, which is the index [9]
            del tempHighScores[9]

            # Adding the new entry to the bottom of the list
            tempHighScores.append([currentPlayerName, currentPlayerScore])

            # Sort the new list by scores with sortingHelper (reverse means descending values, from highest to lowest)
            tempHighScores.sort(reverse=True, key=sortingHelper)

            # Finally write the new high scores to the CSV file on server
            with open("scores.csv", mode="w", newline="") as fileToWrite:
                newHighScores = csv.writer(fileToWrite)
                for row in tempHighScores:
                    newHighScores.writerow(row)

            # Print no errors message
            message = "All good now. The player " + player + " with score of " + str(currentPlayerScore) + " was added to the high score list."
            response = [
                {"error": 0},
                {"message": message}
            ]
            print(json.dumps(response))

        # If the doublecheck fails, it means someone has tried to send too small score to the list
        else:
            message = "Cheating is unacceptable. The score " + str(currentPlayerScore) + " is not good enough to get to the high score list."
            response = [
                {"error": 2},
                {"message": message}
            ]
            print(json.dumps(response))

# Program starts here. We will only return JSON prints.
print("Content-type: application/json\n")

try:
    # Find out which HTTP method was used for the request
    cgiMethod = os.environ["REQUEST_METHOD"].lower()

    # If method was GET, perform the Get() function
    if cgiMethod == "get":
        getHighScoresFromServer()

    # Else if the method was POST, parse parameters from the request, then send them forward and perform the Add() function
    elif cgiMethod == "post":
        data = cgi.FieldStorage()
        user = data.getvalue("user", "")
        password = data.getvalue("password", "")
        name = data.getvalue("name", "")
        score = int(data.getvalue("score", "-1"))
        addNewEntryToHighScores(user, password, name, score)

# Exception error prints:
except FileNotFoundError:
    message = "File not found."
    response = [
        {"error": 3},
        {"message": message}
    ]

except ImportError:
    message = "Imported module was not found."
    response = [
        {"error": 4},
        {"message": message}
    ]

except IndexError:
    message = "Sequence index out of range. There might be a problem with the high score database."
    response = [
        {"error": 5},
        {"message": message}
    ]

except NameError:
    message = "Variable not found in local or global scale. Have you entered correct parameters?"
    response = [
        {"error": 6},
        {"message": message}
    ]

except ValueError:
    message = "Wrong value was given. What and why? Go figure."
    response = [
        {"error": 7},
        {"message": message}
    ]

except:
    message = "Unknown error. Something went terribly wrong and cute kittens and puppies were accidentally dropped into a meat grinder. Whoops."
    response = [
        {"error": 8},
        {"message": message}
    ]

finally:
    print(json.dumps(response))