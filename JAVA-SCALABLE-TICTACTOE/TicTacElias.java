package tictacelias;
 
import java.util.Scanner;
import java.io.IOException;

/**
    * This is a scalable version of the classic Tic-Tac-Toe or Five-In-A-Row game.
    *
    * <p>This game is made as a project work for Introduction to Programming course on the first year of Data Processing Degree Programme. It is my own take on the classic Tic-Tac-Toe game where the player can choose the size of the gameboard and also how many marks in a row is needed for a win.
    * <p>As an extra the player can also choose a difficulty level which replies to how long streak the player has in a row (horizontal, vertical or diagonal). With the easiest level the machine will only react and prevent the players win when the players streak is one mark away from winning. Levels 2 and 3 are only available in the game when the gameboard size is set to larger than 4. There is a built-in primitive AI which tries to prevent the human player from winning with relatively simple principles
    *
    * @author      Elias Puukari <elias.puukari@tuni.fi>
    * @version     2019.1216
    * @since       8.0          
    */
public class TicTacElias {

    /** 
     * This variable defines the width of one box in the ASCII graphical output.
     */
    final static int width = 11;

    /** 
     * This variable defines the height of one box in the ASCII graphical output.
     */
    final static int height = 5;

    /** 
     * This variable defines the character which will be used to mark the human player within the gameboard and ASCII graphical output.
     */
    final static char player = 'X';

    /** 
     * This variable defines the character which will be used to mark the AI (machine) within the gameboard and ASCII graphical output.
     */
    final static char machine = 'O';

    /** 
     * This variable defines the character which will be used to an empty space within the gameboard and ASCII graphical output.
     */
    final static char empty = ' ';

    /** 
     * This variable will include information of the operating system in use.
     */
    static String opSys = "";

    /** 
     * This variable will define the size of the gameboard (size*size).
     */
    static int size;

    /** 
     * This variable will define how many consecutive characters will be needed to win the game.
     */
    static int toWin;

    /** 
     * This variable will define the difficulty and intelligence level of the built-in AI.
     */
    static int level;

    /** 
     * This variable will define which amount of human player characters (toWin - threat) will trigger a threat warning to AI. This variable changes according to the difficulty/intelligence level.  
     */
    static int threat;

    /**
     * <p>This 1-dimensional integer array will include the game parameters.
     * 
     * <p>Index[0] will include the value for the operating system (1 for windows, 2 for mac/linux, 3 for unknown).
     * <p>Index[1] will include the value of the static variable size.
     * <p>Index[2] will include the value of the static variable toWin.
     * <p>Index[3] will include the value of the static variable level.
     * <p>Index[4] will include the value of the static variable toWin.
     */
    static int[] gameParameters = new int[5];

    /** 
     * This variable will turn to true when someone has won or the gameboard is full.
     */
    static boolean endGame = false;

    /** 
     * This variable will include all relevant information about the current situation in the game.
     */
    static int[] situation = new int[41];

    /** 
     * This variable will include the actual gameboard.
     */
    static char[][] board;

    /** 
     * This method gets the game started, asks the user for game parameters and then sets these parameters to the gameParameters int[] array.
     */
    public static void getStarted() {

        // emptyScreen(opSys);
        System.out.println("Welcome to Tic Tac Elias! This is a simple game with traditional rules of TicTacToe and/or five-in-a-row.");

        // Set the game parameters by asking them from the user in the method askInfo
        gameParameters = askInfo();

        /*
        // These lines are for testing purposes only.
        System.out.println("Setting parameters now.");
        gameParameters[0] = 2;  // operating system
        gameParameters[1] = 3;  // size
        gameParameters[2] = 3;  // toWin
        gameParameters[3] = 1;  // level
        gameParameters[4] = 2;  // threat
        */

        // Determine which operating system is in use
        if (gameParameters[0] == 1) {
            opSys = "win";
        }
        else if (gameParameters[0] == 2) {
            opSys = "mac";
        }
        else if (gameParameters[0] == 3) {
            opSys = "unknown";
        }
        else {
            opSys = "unknown";
        }

        // Set basic parameters for the game
        size = gameParameters[1];
        toWin = gameParameters[2];
        level = gameParameters[3];
        threat = gameParameters[4];

        // Create the empty gameboard
        board = GameBoard.create(size, empty);
    }

    /**
     * This method is the starting point at the game where the user determines what is the gameboard size, how long streak is needed for winning the game and which difficulty level the user wants to play with.
     * 
     * @return Returns an int[] array with four indexes including the parameters the user has given.
     */
    public static int[] askInfo() {
        int[] parameters = new int[5];
        Scanner input = new Scanner(System.in);
        try {
            // Set value/information for the operating system.
            System.out.println("Which operating system are you using? (1 for Windows, 2 for Mac/Linux, 3 for others)");
            int os = Integer.parseInt(input.nextLine());
            while (os !=1 && os !=2 && os !=3) {
                System.out.println("That is not a valid input. Please choose again which operating system are you using? (1 for Windows, 2 for Mac/Linux, 3 for others)");
                os = Integer.parseInt(input.nextLine());
            }
            boolean wantToContinue = true;
            if (os == 3) {
                wantToContinue = false;
                System.out.println("Your operating system might not be applicable for this version of the game. Do you still want to continue? (y/n)");
                String yesOrNo = input.nextLine();
                if (yesOrNo.equalsIgnoreCase("y")) {
                    wantToContinue = true;
                }
                else if (yesOrNo.equalsIgnoreCase("n")) {
                    wantToContinue = false;
                    endGame = true;
                }
                else {
                    boolean properInput = false;
                    while (!properInput) {
                        System.out.println("That is not a valid input. Please choose again if you want to continue. (y/n)");
                        yesOrNo = input.nextLine();
                        if (yesOrNo.equalsIgnoreCase("y")) {
                            properInput = true;
                            wantToContinue = true;
                        }
                        else if (yesOrNo.equalsIgnoreCase("n")) {
                            properInput = true;
                            wantToContinue = false;
                        }
                    }
                }
            }
            if (!wantToContinue) {
                System.exit(0);
            }
            parameters[0] = os;

            // Set value for the variable size
            System.out.println("What gameboard size would you like to play with?");
            int a = Integer.parseInt(input.nextLine());
            while (a < 3 || a > 20) {
                System.out.println("That is not a valid size for the gameboard. Please choose a value between 3 and 20.");
                a = Integer.parseInt(input.nextLine());    
            }
            parameters[1] = a;

            // Set value for the variable toWin
            int b = 0;
            if (a == 3) {
                b = 3;
            }
            else if (a >= 4 && a < 10) {
                System.out.println("How many consecutive characters in a streak will be needed to win? The minimum here is 3 and maximum is " + a + ".");
                b = Integer.parseInt(input.nextLine());
                while (b < 3 || b > a) {
                    System.out.println("That is not a valid input. You must choose a value between 4 and " + a + " here.");
                    b = Integer.parseInt(input.nextLine());
                }
            }
            else if (a >= 10) {
                System.out.println("How many consecutive characters in a streak will be needed to win? The minimum here is 5 and maximum is " + a + ".");
                b = Integer.parseInt(input.nextLine());
                while (b < 5 || b > a) {
                    System.out.println("That is not a valid input. You must choose a value between 5 and " + a + " here.");
                    b = Integer.parseInt(input.nextLine());
                }
            }
            parameters[2] = b;  

            // In the next rows we set a difficulty/intelligence level for the AI.
            int c = 0;
            if (a == 3 || b == 3) {
                c = 1;
            }
            else if (a >= 4 && b == 4) {
                System.out.println("Choose an intelligence level of 1 or 2 for your opponent, the machine.");
                c = Integer.parseInt(input.nextLine());
                while (c != 1 && c != 2) {
                    System.out.println("You must choose intelligence level 1 or 2.");
                    c = Integer.parseInt(input.nextLine());
                }
            }
            else if (a > 4 && b > 4) {
                System.out.println("Choose an intelligence level of 1, 2 or 3 for your opponent, the machine.");
                c = Integer.parseInt(input.nextLine());
                while (c < 1 || c > 3) {
                    System.out.println("That is not a valid input. You must choose an intelligence level of 1, 2 or 3.");
                    c = Integer.parseInt(input.nextLine());
                }
            }
            parameters[3] = c;

            // The idea here is that when difficulty/intelligence level is set to 1, the AI only reacts to a threat when human player only needs one more character in a streak to win the game.
            // When difficulty level is set to 2 (only possible with gameboard size and needed to win of 4 or more) the AI would react when human needs two more characters in a streak to win.
            // When difficulty level is set to 3 (only possible with a gameboard size and needed to win of 5 or more and) the AI would react when human needs three more characters in a streak to win.

            // Here we define the value for the variable threat aka how many characters in a streak is needed to win extracted by the difficulty/intelligence level.
            int d = (b-c);
            parameters[4] = d;

            // Finally return the defined parameters in an int[] array.
            return parameters;
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("You did not enter proper values there or just pressed enter while you shouldn't have. Try again with more precision please, I'm just an application you know.");
        }
    }

    /**
     * This method empties the command line or terminal screen each time things happen in the gameboard so it would look consistent.
     * 
     * @param opSys Which operating system (windows or mac/linux) the user has chose in the beginning.
     */
    public static void emptyScreen(String opSys) {
        if (opSys.equals("mac")) {
            System.out.print("\033[H\033[2J");  
            System.out.flush();    
        }
        else if (opSys.equals("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            catch (IOException e) {
                System.out.println("Something has gone wrong with choosing the operating system but the game might still work. Let's hope for the best.");
            }
            catch (InterruptedException e) {
                System.out.println("Something has gone wrong with choosing the operating system but the game might still work. Let's hope for the best.");
            }
        }
        else {
            System.out.println("WARNING! Operating system unknown! Application may not function properly.");
        }
    }    

    /** 
     * playTheGame method determines what happens during one round of the game. 
     */
    public static void playTheGame() {

        // Start the game by emptying the command line/terminal screen and print the empty gameboard in ASCII graphics.
        emptyScreen(opSys);
        GameBoard.print(board, width, height, size, player, machine, empty);

        while (!endGame) {

            // Define user moves for this round.
            int[] userInput = getUserInput(board, empty, size);
            int pRow = userInput[0];
            int pCol = userInput[1];
            board[pRow][pCol] = player;
            // System.out.println("Player was set to " + pRow + pCol); // This line is for testing purposes only.

            // Check current game situation
            situation = Checks.getSituation(board, size, player, machine, empty, toWin, threat);

            // If there is a winner or all the boxes are filled this prints the final gameboard and breaks out of the while loop.
            if (situation[0] != 0) {
                emptyScreen(opSys);
                GameBoard.print(board, width, height, size, player, machine, empty);
                endGame = true;
                break;
            }
            // This is an extra caution because the application does not always recognize the full gameboard because of some bugs in the AI or the Checks class.
            else if (Checks.boardFull(board, size, player, machine)) {
                endGame = true;
                emptyScreen(opSys);
                GameBoard.print(board, width, height, size, player, machine, empty);
                break;
            }
            // If there's no winner yet at this point, it's the machine's turn to move
            int[] machineInput = getMachineMove(board, empty, size, toWin, situation);
            int mrow = machineInput[0];
            int mcol = machineInput[1];
                
            // This is a backup caution and will get random empty index if the AI has been a dummy and trying to enter a non empty index on the gameboard array. Also theres another break out of loop if the gameboard is already full.
            while (!Checks.isThisEmpty(board, mrow, mcol, empty)) {
                int[] helperEmptyIndex = AI.getRandomEmptyForMachine(board, empty, size);
                mrow = helperEmptyIndex[0];
                mcol = helperEmptyIndex[1];
                if (Checks.boardFull(board, size, player, machine)) {
                    endGame = true;
                    break;
                }
            }
            // If everything is ok the machine places its mark on the gameboard.
            board[mrow][mcol] = machine;

            // Check current game situation
            situation = Checks.getSituation(board, size, player, machine, empty, toWin, threat);

            // If no winner yet, empty the screen and print the current gameboard after machine has moved:
            if (situation[0] == 0) {
                emptyScreen(opSys);
                GameBoard.print(board, width, height, size, player, machine, empty);    
            }
            // If there is a winner or all the boxes are filled this prints the final gameboard and breaks out of the while loop.
            else if (situation[0] != 0) {
                emptyScreen(opSys);
                GameBoard.print(board, width, height, size, player, machine, empty);
                endGame = true;
                break;  // Note! These loop breaks here are not mandatory but they are there for extra extra cautions so the game would admittedly end when the board is full or there is a winner found.
            }
            // This is a double extra caution because the application might not always recognize the full gameboard because of some minor bugs in the AI or the Checks class.
            else if (situation[0] == 0 && Checks.boardFull(board, size, player, machine)) {
                endGame = true;
                emptyScreen(opSys);
                GameBoard.print(board, width, height, size, player, machine, empty);
                break;  // Note! These loop breaks here are not mandatory but they are there for extra extra cautions so the game would admittedly end when the board is full or there is a winner found.
            }
        }
    }

    /**
     * This method is for testing purposes only. It will print out the indexes of the integer array of the current situation in the game.
     * 
     * @param situationRightNow the current situation that is sent to the method
     */
    public static void printTestSituationHere(int[] situationRightNow) {
        int[] printSituation = situationRightNow;
        System.out.println("ATTENTION! THIS IS JUST A TEST RUN AND SHOULD NOT BE VISIBLE!");
        System.out.println("The situation array length is " + printSituation.length);
        System.out.println("Printing the situation array index values now:");
        for (int i=0; i<printSituation.length; i++) {
            System.out.print("Index [" + i + "] currently includes the value: " + printSituation[i]);
            System.out.println();
        }
    }

    /**
     * getUserInput method asks the human player which box fill be filled with the human character on this round of the game.
     * 
     * The method takes into consideration that the user wont be able to give invalid inputs (including chosen index value out of the gameboard or board index is already filled).
     * 
     * @param board is the gameboard.
     * @param empty the empty character.
     * @param size is the gameboard size.
     * @return integer[] array with chosen row and column values
     */
    public static int[] getUserInput(char[][] board, char empty, int size) {
        Scanner input = new Scanner(System.in);
        int[] playersChoice = new int[2];
    
        try {
            System.out.println("Choose a row (horizontal " + 1 + " to " + size + "):");
            int a = Integer.parseInt(input.nextLine());
    
            System.out.println("Choose a column (vertical " + 1 + " to " + size + "):");
            int b = Integer.parseInt(input.nextLine());
    
            if (!Checks.isThisUserInputInsideTheBoard(a, b, size)) {
                System.out.println("That is not inside the gameboard.");
                while(!Checks.isThisUserInputInsideTheBoard(a, b, size)) {
                    System.out.println("You gave invalid values. Try again. First the row:");
                    a = Integer.parseInt(input.nextLine());
                    System.out.println("Now the column:");
                    b = Integer.parseInt(input.nextLine());
                }
            }
            else if (!Checks.isThisUserInputEmpty(board, a, b, empty)) {
                while (!Checks.isThisUserInputEmpty(board, a, b, empty)) {
                    System.out.println("It's not empty. Choose again you punk. First the row:");
                    a = Integer.parseInt(input.nextLine());
                    System.out.println("Now the column:");
                    b = Integer.parseInt(input.nextLine());
                }
            }
            if (Checks.isThisUserInputInsideTheBoard(a, b, size) && Checks.isThisUserInputEmpty(board, a, b, empty)) {
                a = a - 1;
                b = b - 1;    
            }
            // System.out.println("Player is choosing " + a + b); // This line is for testing purposes only.
            playersChoice[0] = a;
            playersChoice[1] = b;
            return playersChoice;        
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("You did not enter proper values there or just pressed enter while you shouldn't have. Try again with more precision please. I'm just a simple application you know.");
        }
    }

    /**
     * getMachineMove uses the current situation of the game and sends it to the AI to check where to put a marking next.
     * 
     * @param board is the gameboard.
     * @param empty is the empty character.
     * @param size is the size of the gameboard.
     * @param toWin is how many consecutive characters are needed in a streak to win the game.
     * @param situation is the int[] array holding the current situation of the game.
     * @return returns the AI's choice as an int[] array with 2 indexes where [0] is the row value and [1] is the column value.
     */
    public static int[] getMachineMove(char[][] board, char empty, int size, int toWin, int[] situation) {
        int[] machineChoice = new int[2];
        // System.out.println("Machine chooses now."); // This line is for testing purposes only.
        machineChoice = AI.decision(board, empty, size, toWin, situation);
        return machineChoice;
    }

    /** 
     * This method printSummary includes the final prints produced by the application and tells who has won the game and how.
     */
    public static void printSummary() {

        int resolution = situation[0];

        switch(resolution) {
            case 0:
                System.out.println("No one wins. Thank you and welcome again.");
                break;
            case 1:
                System.out.println("The human wins with a horizontal streak! Congratulations human!");
                break;
            case 2:
                System.out.println("The human wins with a vertical streak! Congratulations human!");
                break;
            case 3:
                System.out.println("The human wins with a diagonal streak from left to right! Congratulations human!");
                break;
            case 4:
                System.out.println("The human wins with a diagonal streak from right to left! Congratulations human!");
                break;
            case 5:
                System.out.println("The machine wins with a horizontal streak! All hail the machine!");
                break;
            case 6:
                System.out.println("The machine wins with a vertical streak! All hail the machine!");
                break;
            case 7:
                System.out.println("The machine wins with a diagonal streak from up left to down right! All hail the machine!");
                break;
            case 8:
                System.out.println("The machine wins with a diagonal streak from up right to down left! All hail the machine!");
                break;
            case 9:
                System.out.println("All the empty spots were filled. Thank you and welcome again.");
                break;
        }
    }

    /**
     * This is the main method of the game. It starts and ends the application.
     * 
     * @param args well this is the main args you know.
     */
    public static void main(final String[] args) {
        getStarted();
        playTheGame();
        printSummary();    
    }
}

/** 
 * GameBoard class includes methods to iterate the empty board and print the board while game is on. The board itself is a character array and it includes three kind of characters which are set in the main method. These characters are player (X by default), machine (O by default) and empty (empty character by default). These three characters are used all around the application to figure, check or print things.
 */
class GameBoard {

    /** 
     * This method prints the current situation of the game. It includes preset ASCII graphics for each character in 2-dimensional character arrays oneEmptyBox, oneBoxWithX and oneBoxWithO. The method uses several loops inside the others to print each row of each box. The gameboard itself is a character array inside the main method in the public class. This method only references that and uses it's own presets to create the graphical user interface for this game.
     * 
     * @param board is the gameboard itself which includes the current situation in the game.
     * @param width is to set width for one box in the ASCII graphics.
     * @param height is to set height for one box in the ASCII graphics.
     * @param size is the size of the actual gameboard aka how many boxes it includes (horizontal and vertical).
     * @param player is the character marking the human player. This is X by default.
     * @param machine is the character marking the computer. This is O by default.
     * @param empty is the character marking an empty box in the actual gameboard. Space bar (blank character) by default.
      */
    static void print (char[][] board, int width, int height, int size, char player, char machine, char empty) {
        // When using the "graphic" interface, here are the basic forms for empty, machine and player -character arrays

        final char[][] oneEmptyBox =    {
                                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                                        };

        final char[][] oneBoxWithX =    {
                                        {' ', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X', ' '},
                                        {' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X', ' ', ' ', ' '},
                                        {' ', ' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', ' ', ' '},
                                        {' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X', ' ', ' ', ' '},
                                        {' ', 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X', ' '}
                                        };

        final char[][] oneBoxWithO =    {
                                        {' ', ' ', ' ', 'O', ' ', 'O', ' ', 'O', ' ', ' ', ' '},
                                        {' ', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'O', ' '},
                                        {' ', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'O', ' '},
                                        {' ', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'O', ' '},
                                        {' ', ' ', ' ', 'O', ' ', 'O', ' ', 'O', ' ', ' ', ' '}
                                        };
                                        
        final char slash = '|';
        final char dash = '-';
        
        // Here the application starts to print the GUI gameboard
        // First row first character is a slash, followed by the width amount of dashes, creating one upper edge of a box. These are then looped for the amount of the actual gameboard size.
        for (int f=0; f<size; f++) {
            System.out.print(slash);
            for (int g=0; g<width; g++) {
                System.out.print(dash);
            }
        }
        // Final character (slash) of the first row is printed here, then going to the next row.
        System.out.print(slash);
        System.out.println();

        // Then we have nicely four consecutive for-loops.
        for (int i=0; i<size; i++) {                // This loops for the amount of boxes
            for (int j=0; j<height; j++) {          // This loops for the height of one box
                System.out.print(slash);            // Print out first character of second row
                for (int y=0; y<size; y++) {        // This loops the vertical axis for the amount of boxes
                    for (int x=0; x<width; x++) {   // This loops for the horizontal axis of the box

                        // Check the situation in the game from the board, if the index is empty, then use empty box template.
                        if (board[i][y] == empty) {
                            System.out.print(oneEmptyBox[j][x]);                    
                        }

                        // Check situation, if board index includes machine, print the insides of the machine box here.
                        else if (board[i][y] == machine) {
                            System.out.print(oneBoxWithO[j][x]);                    
                        }

                        // Check situation, if board index includes player, print the insides of the player box here.
                        else if (board[i][y] == player) {
                            System.out.print(oneBoxWithX[j][x]);                    
                        }
                    }
                    System.out.print(slash);
                }
                System.out.println();
            }
            for (int f=0; f<size; f++) {
                System.out.print(slash);
                for (int g=0; g<width; g++) {
                    System.out.print(dash);
                }
            }
            System.out.print(slash);
            System.out.println();
        }
    }

    /**
     * This method creates, iterates and returns the actual gameboard with empty characters. This is only used once in the beginning of the game when the game parameters have been set.
     * 
     * @param size is the size of the gameboard.
     * @param empty is the character marking an empty box in the gameboard.
     * @return returns the blank gameboard.
     */
    static char[][] create(int size, char empty) {
        char[][] board = new char[size][size];
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                board[i][j] = empty;
            }
        }
        return board;
    }
}

/** 
 * Checks class includes methods to check the situation of the game and other needed information during gameplay.
 */
class Checks {

    /**
     * <p>This method is a core element of the game. After each turn this method is used to return an int[] array that includes information about the current situation of the game. This returned array will then be used inside the game to figure if someone has won (and stop the game) and it's also used for the AI to decide which move to make according to the information provided here.
     * <p>The returned array will include 41 indexes and their contents and meanings are more explained within the code comments instead of this Javadoc comment.
     * 
     * @param board is the actual gameboard with current situation.
     * @param size is the size of the gameboard.
     * @param player is the character marking the human player.
     * @param machine is the character marking the computer (AI).
     * @param empty is the character marking an empty box in the gameboard.
     * @param toWin is how many consecutive characters a player needs to have in a streak to win.
     * @param threat is how many consecutive human player characters in a streak means a threat to the AI.
     * @return returns the information of the game situation in an int[] array with a size of 41 indexes.
     */
    public static int[] getSituation(char[][] board, int size, char player, char machine, char empty, int toWin, int threat) {

        int[] horizontal = new int[10];
        int[] vertical = new int[10];
        int[] diagonalLeft = new int[10];
        int[] diagonalRight = new int[10];

        int[] returnThisInfo = new int[41];

        // returnThisInfo int array will include these informations indexes about the current situation in the game:

        // WINNER INFO INCLUDED IN THE FIRST INDEX
        // [0]  0 in this index means there is no winner yet and the game can be continued.
        //      1 in this index means the human player has won with a horizontal streak.
        //      2 in this index means the human player has won with a vertical streak.
        //      3 in this index means the human player has won with a diagonal streak from upper left to down right.
        //      4 in this index means the human player has won with a diagonal streak from upper right to down left.
        //      5 in this index means the machine has won with a horizontal streak.
        //      6 in this index means the machine has won with a vertical streak.
        //      7 in this index means the machine has won with a diagonal streak from upper left to down right.
        //      8 in this index means the machine has won with a diagonal streak from upper right to down left.
        //      9 in this index means there is no winner & the gameboard is full so game ends.

        // HORIZONTAL CHECK INFO WILL BE INCLUDED IN THE FIRST 10 INDEXES
        // [1]  0 in this index means no one has won with a horizontal streak and there is no horizontal threat to the AI.
        //      1 in this index means the human player has won with a horizontal streak.
        //      2 in this index means the machine has won with a horizontal streak.
        //      3 in this index means there is a horizontal threat to the AI.
        // [2]  This index tells how long is the players horizontal streak if there is any.
        // [3]  This index includes the row index of the gameboard where the players horizontal threat streak ends.
        // [4]  This index includes the column index of the gameboard where the players horizontal threat streak ends.
        // [5]  This index tells if the horizontal threat streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.
        // [6]  This index tells if the machine has more than one markings in a horizontal streak. 1 means true, 0 means false.
        // [7]  If index[6] is true, this index tells how long is the horizontal streak.
        // [8]  If index[6] is true, this index tells the row index from the gameboard where the horizontal streak ends.
        // [9]  If index[6] is true, this index tells the column index from the gameboard where the horizontal streak ends.
        // [10] If index[6] is true, this index tells if the machines horizontal streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.

        // VERTICAL CHECK INFO WILL BE INCLUDED IN THESE 10 INDEXES
        // [11] 0 in this index means no one has won with a vertical streak and there is no vertical threat to the AI.
        //      1 in this index means the human player has won with a vertical streak.
        //      2 in this index means the machine has won with a vertical streak.
        //      3 in this index means there is a vertical threat to the AI.
        // [12] This index tells how long is the players vertical streak if there is any.
        // [13] This index includes the row index of the gameboard where the players vertical threat streak ends.
        // [14] This index includes the column index of the gameboard where the players vertical threat streak ends.
        // [15] This index tells if the vertical threat streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.
        // [16] This index tells if the machine has more than one markings in a vertical streak. 1 means true, 0 means false.
        // [17] If index[16] is true, this index tells how long is the vertical streak.
        // [18] If index[16] is true, this index tells the row index from the gameboard where the vertical streak ends.
        // [19] If index[16] is true, this index tells the column index from the gameboard where the vertical streak ends.
        // [20] If index[16] is true, this index tells if the machines vertical streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.

        // DIAGONAL UPPER LEFT TO DOWN RIGHT CHECK INFO WILL BE INCLUDED IN THESE 10 INDEXES
        // [21] 0 in this index means no one has won with a diagonal streak (from upper left to down right) and there is no diagonal (LeftToRight) threat to the AI.
        //      1 in this index means the human player has won with a diagonal (LtR) streak.
        //      2 in this index means the machine has won with a diagonal (LtR) streak.
        //      3 in this index means there is a diagonal (LtR) threat to the AI.
        // [22] This index tells how long is the players diagonal (LtR) streak if there is any.
        // [23] This index includes the row index of the gameboard where the players diagonal (LtR) threat streak ends.
        // [24] This index includes the column index of the gameboard where the players diagonal (LtR) threat streak ends.
        // [25] This index tells if the diagonal (LtR) threat streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.
        // [26] This index tells if the machine has more than one markings in a diagonal (LtR) streak. 1 means true, 0 means false.
        // [27] If index[26] is true, this index tells how long is the diagonal (LtR) streak.
        // [28] If index[26] is true, this index tells the row index from the gameboard where the diagonal (LtR) streak ends.
        // [29] If index[26] is true, this index tells the column index from the gameboard where the diagonal (LtR) streak ends.
        // [30] If index[26] is true, this index tells if the machines diagonal (LtR) streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.

        // DIAGONAL UPPER RIGHT TO DOWN LEFT CHECK INFO WILL BE INCLUDED IN THESE 10 INDEXES
        // [31] 0 in this index means no one has won with a diagonal streak (from upper right to down left) and there is no diagonal (RightToLeft) threat to the AI.
        //      1 in this index means the human player has won with a diagonal (RtL) streak.
        //      2 in this index means the machine has won with a diagonal (RtL) streak.
        //      3 in this index means there is a diagonal (RtL) threat to the AI.
        // [32] This index tells how long is the players diagonal (RtL) streak if there is any.
        // [33] This index includes the row index of the gameboard where the players diagonal (RtL) threat streak ends.
        // [34] This index includes the column index of the gameboard where the players diagonal (RtL) threat streak ends.
        // [35] This index tells if the diagonal (RtL) threat streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.
        // [36] This index tells if the machine has more than one markings in a diagonal (RtL) streak. 1 means true, 0 means false.
        // [37] If index[36] is true, this index tells how long is the diagonal (RtL) streak.
        // [38] If index[36] is true, this index tells the row index from the gameboard where the diagonal (RtL) streak ends.
        // [39] If index[36] is true, this index tells the column index from the gameboard where the diagonal (RtL) streak ends.
        // [40] If index[36] is true, this index tells if the machines diagonal (RtL) streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.

        // Lets check situation for each direction:
        horizontal = getHorizontal(board, size, player, machine, empty, toWin, threat);
        vertical = getVertical(board, size, player, machine, empty, toWin, threat);
        diagonalLeft = getDiagonalLeft(board, size, player, machine, empty, toWin, threat);
        diagonalRight = getDiagonalRight(board, size, player, machine, empty, toWin, threat);

        // After going through the checks in different directions, let's now set the info to the first index about who won the game and how (if anyone did win yet...): 
        if (horizontal[0] == 0 && vertical[0] == 0 && diagonalLeft[0] == 0 && diagonalRight[0] == 0) {
            returnThisInfo[0] = 0;      // 0 in this index means there is no winner yet and the game can be continued.
        }
        else if (horizontal[0] == 1) {
            returnThisInfo[0] = 1;      // 1 in this index means the human player has won with a horizontal streak.
        }
        else if (vertical[0] == 1 ) {
            returnThisInfo[0] = 2;      // 2 in this index means the human player has won with a vertical streak.
        }
        else if (diagonalLeft[0] == 1) {
            returnThisInfo[0] = 3;      // 3 in this index means the human player has won with a diagonal streak from upper left to down right.
        }
        else if (diagonalRight[0] == 1) {
            returnThisInfo[0] = 4;      // 4 in this index means the human player has won with a diagonal streak from upper right to down left.
        }
        else if (horizontal[0] == 2) {
            returnThisInfo[0] = 5;      // 5 in this index means the machine has won with a horizontal streak.
        }
        else if (vertical[0] == 2 ) {
            returnThisInfo[0] = 6;      // 6 in this index means the machine has won with a vertical streak.
        }
        else if (diagonalLeft[0] == 2) {
            returnThisInfo[0] = 7;      // 7 in this index means the machine has won with a diagonal streak from upper left to down right.
        }
        else if (diagonalRight[0] == 2) {
            returnThisInfo[0] = 8;      // 8 in this index means the machine has won with a diagonal streak from upper right to down left.
        }
        else if (boardFull(board, size, player, machine)) {
            returnThisInfo[0] = 9;      // 9 in this index means there is no winner & the gameboard is full so game ends.
        }

        // After checking the whole gameboard we iterate each information array to the main information array that will be returned from this method.
        for (int i=0; i<10; i++) {
            returnThisInfo[(i + 1)] = horizontal[i];
        }
        for (int i=0; i<10; i++) {
            returnThisInfo[(i + 11)] = vertical[i];
        }
        for (int i=0; i<10; i++) {
            returnThisInfo[(i + 21)] = diagonalLeft[i];
        }
        for (int i=0; i<10; i++) {
            returnThisInfo[(i + 31)] = diagonalRight[i];
        }
        return returnThisInfo;
    }

    /**
     * This boolean method checks if the gameboard is full with no empty boxes.
     * 
     * @param board is the gameboard.
     * @param size is the size of the gameboard.
     * @param player is the character for human player.
     * @param machine is the character for the machine.
     * @return return true if the gameboard is full of human and machine characters and no empty ones left. Returns false if there is at least one open box left.
     */
    public static boolean boardFull(char[][] board, int size, char player, char machine) { 

        // This will be increased everytime there is a character in the gameboard.
        int countNotEmpties = 0;

        // This is how many boxes there are in the gameboard in total.
        int countFull = size * size;

        // Go through the gameboard and count the non-empty indexes.
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (board[i][j] == player || board[i][j] == machine) {
                    countNotEmpties++;
                }
            }
        }
        if (countNotEmpties == countFull) {
            return true;
        }
        else {
            return false;
        }
    }

    /** 
     * getHorizontal method goes through the gameboard in horizontals and returns information about the situation to the main situation array in the getSituation method.
     * 
     * @param board is the gameboard.
     * @param size is the size of the gameboard.
     * @param player is the human player character.
     * @param machine is the machine character.
     * @param empty is the empty character.
     * @param toWin is how long the consecutive streak needs to be in order to win the game.
     * @param threat is how many consecutive human player characters in a streak means a threat to the AI.
     * @return returns the 10 index int[] array which includes the horizontal info.
     */
    public static int[] getHorizontal (char[][] board, int size, char player, char machine, char empty, int toWin, int threat) {

        // HORIZONTAL CHECK INFO ARRAY WILL INCLUDE THIS INFORMATION
        // [0]  0 in this index means no one has won with a horizontal streak and there is no horizontal threat to the AI.
        //      1 in this index means the human player has won with a horizontal streak.
        //      2 in this index means the machine has won with a horizontal streak.
        //      3 in this index means there is a horizontal threat to the AI.
        // [1]  This index tells how long is the players horizontal streak if there is any.
        // [2]  This index includes the row index of the gameboard where the players horizontal threat streak ends.
        // [3]  This index includes the column index of the gameboard where the players horizontal threat streak ends.
        // [4]  This index tells if the horizontal threat streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.
        // [5]  This index tells if the machine has more than one markings in a horizontal streak. 1 means true, 0 means false.
        // [6]  If index[5] is true, this index tells how long is the horizontal streak.
        // [7]  If index[5] is true, this index tells the row index from the gameboard where the horizontal streak ends.
        // [8]  If index[5] is true, this index tells the column index from the gameboard where the horizontal streak ends.
        // [9] If index[5] is true, this index tells if the machines horizontal streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.

        int[] horizontalCheck = new int[10];

        // Variable countPlayer is used to count players characters in a row
        int countPlayer = 0;
        int countMachine = 0;
        String direction = "horizontal";
        
        // Check for horizontal rows
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {

                // Inside the main loop now
                if (board[i][j] == player) {
                    countPlayer++;

                    // Check if player has reached toWin, if not then continue looping
                    if (countPlayer == toWin) {
                        horizontalCheck[0] = 1;
                        break;
                    }

                    // Send info about players moves to the AI if player is reaching the win
                    else if (countPlayer == threat) {
                        horizontalCheck[0] = 3; // Threat to AI
                        horizontalCheck[1] = countPlayer;
                        horizontalCheck[2] = i; // Send row
                        horizontalCheck[3] = j; // Send col
                        horizontalCheck[4] = isNextOrBeforeEmpty(board, i, j, empty, size, countPlayer, direction);
                    }
                }
                else if (board[i][j] == machine) {
                    countPlayer = 0;
                    countMachine++;
                    if (countMachine == toWin) {
                        horizontalCheck[0] = 2;
                        break;
                    }
                    else if (countMachine > 1) {
                        horizontalCheck[5] = 1; // AI has more than 1 in a row
                        horizontalCheck[6] = countMachine; // How many ai has
                        horizontalCheck[7] = i; // Send row
                        horizontalCheck[8] = j; // Send col
                        horizontalCheck[9] = isNextOrBeforeEmpty(board, i, j, empty, size, countMachine, direction);
                    }    
                }
                // If there is an empty box in the row, erase both counters because the characters need to be next to each other, no empty ones between
                else if (board[i][j] == empty) {
                    countPlayer = 0;
                    countMachine = 0;
                }
                // Main loop ends here
            }
            //After each row erase both and go to next row
            countPlayer = 0;
            countMachine = 0;
        }
        if (horizontalCheck[4] == 2 && horizontalCheck[0] != 1 && horizontalCheck[0] != 2) {
            horizontalCheck[0] = 0;
        }
        return horizontalCheck;
    }

    /** 
     * getVertical method goes through the gameboard in verticals and returns information about the situation to the main situation array in the getSituation method.
     * 
     * @param board is the gameboard.
     * @param size is the size of the gameboard.
     * @param player is the human player character.
     * @param machine is the machine character.
     * @param empty is the empty character.
     * @param toWin is how long the consecutive streak needs to be in order to win the game.
     * @param threat is how many consecutive human player characters in a streak means a threat to the AI.
     * @return returns the 10 index int[] array which includes the vertical info.
     */
    public static int[] getVertical (char[][] board, int size, char player, char machine, char empty, int toWin, int threat) {

        // VERTICAL CHECK ARRAY WILL INCLUDE THIS INFORMATION
        // [0] 0 in this index means no one has won with a vertical streak and there is no vertical threat to the AI.
        //     1 in this index means the human player has won with a vertical streak.
        //     2 in this index means the machine has won with a vertical streak.
        //     3 in this index means there is a vertical threat to the AI.
        // [1] This index tells how long is the players vertical streak if there is any.
        // [2] This index includes the row index of the gameboard where the players vertical threat streak ends.
        // [3] This index includes the column index of the gameboard where the players vertical threat streak ends.
        // [4] This index tells if the vertical threat streak has an empty box before or after the streak.
        //     0 in this index means no value has been set.
        //     1 in this index means that both before and after the streak there is an empty box.
        //     2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //     3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //     4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.
        // [5] This index tells if the machine has more than one markings in a vertical streak. 1 means true, 0 means false.
        // [6] If index[5] is true, this index tells how long is the vertical streak.
        // [7] If index[5] is true, this index tells the row index from the gameboard where the vertical streak ends.
        // [8] If index[5] is true, this index tells the column index from the gameboard where the vertical streak ends.
        // [9] If index[5] is true, this index tells if the machines vertical streak has an empty box before or after the streak.
        //      0 in this index means no value has been set.
        //      1 in this index means that both before and after the streak there is an empty box.
        //      2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //      3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //      4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.

        int[] verticalCheck = new int[10];

        // Variable countPlayer is used to count players characters in a row
        int countPlayer = 0;
        int countMachine = 0;
        String direction = "vertical";

        // Check for vertical rows
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {

                // Inside the main loop now
                int theRow = j;
                int theCol = i;

                if (board[theRow][theCol] == player) {
                    countPlayer++;
                    
                    // Check if player has reached toWin, if not then continue looping
                    if (countPlayer == toWin) {
                        verticalCheck[0] = 1;
                        break;
                    }

                    // Send info about players moves to the AI if player is reaching the win
                    else if (countPlayer == threat) {
                        verticalCheck[0] = 3; // Threat to AI
                        verticalCheck[1] = countPlayer;
                        verticalCheck[2] = theRow; // Send row
                        verticalCheck[3] = theCol; // Send col
                        verticalCheck[4] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countPlayer, direction);
                    }
                }
                else if (board[theRow][theCol] == machine) {
                    countPlayer = 0;
                    countMachine++;
                    if (countMachine == toWin) {
                        verticalCheck[0] = 2;
                        break;
                    }
                    else if (countMachine > 1) {
                        verticalCheck[5] = 1; // AI has more than 1 in a row
                        verticalCheck[6] = countMachine; // How many ai has
                        verticalCheck[7] = theRow; // Send row
                        verticalCheck[8] = theCol; // Send col
                        verticalCheck[9] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countMachine, direction);
                    }    
                }
                // If there is an empty box in the row, erase both counters because the characters need to be next to each other, no empty ones between
                else if (board[theRow][theCol] == empty) {
                    countPlayer = 0;
                    countMachine = 0;
                }
                // Main loop ends here
            }
            //After each row erase both and go to next row
            countPlayer = 0;
            countMachine = 0;
        }
        if (verticalCheck[4] == 2 && verticalCheck[0] != 1 && verticalCheck[0] != 2) {
            verticalCheck[0] = 0;
        }
        return verticalCheck;
    }

    /** 
     * getDiagonalLeft method goes through the gameboard in diagonals from upper left to down right and returns the information about the situation to the main situation array in the getSituation method.
     * 
     * @param board is the gameboard.
     * @param size is the size of the gameboard.
     * @param player is the human player character.
     * @param machine is the machine character.
     * @param empty is the empty character.
     * @param toWin is how long the consecutive streak needs to be in order to win the game.
     * @param threat is how many consecutive human player characters in a streak means a threat to the AI.
     * @return returns the 10 index int[] array which includes the diagonal left to right info.
     */
    public static int[] getDiagonalLeft(char[][] board, int size, char player, char machine, char empty, int toWin, int threat) {
    
        // DIAGONAL UPPER LEFT TO DOWN RIGHT CHECK ARRAY WILL INCLUDE THIS INFO
        // [0] 0 in this index means no one has won with a diagonal streak (from upper left to down right) and there is no diagonal (LeftToRight) threat to the AI.
        //     1 in this index means the human player has won with a diagonal (LtR) streak.
        //     2 in this index means the machine has won with a diagonal (LtR) streak.
        //     3 in this index means there is a diagonal (LtR) threat to the AI.
        // [1] This index tells how long is the players diagonal (LtR) threat streak if there is any.
        // [2] This index includes the row index of the gameboard where the players diagonal (LtR) threat streak ends.
        // [3] This index includes the column index of the gameboard where the players diagonal (LtR) threat streak ends.
        // [4] This index tells if the diagonal (LtR) threat streak has an empty box before or after the streak.
        //     0 in this index means no value has been set.
        //     1 in this index means that both before and after the streak there is an empty box.
        //     2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //     3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //     4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.
        // [5] This index tells if the machine has more than one markings in a diagonal (LtR) streak. 1 means true, 0 means false.
        // [6] If index[5] is true, this index tells how long is the diagonal (LtR) streak.
        // [7] If index[5] is true, this index tells the row index from the gameboard where the diagonal (LtR) streak ends.
        // [8] If index[5] is true, this index tells the column index from the gameboard where the diagonal (LtR) streak ends.
        // [9] If index[5] is true, this index tells if the machines diagonal (LtR) streak has an empty box before or after the streak.
        //     0 in this index means no value has been set.
        //     1 in this index means that both before and after the streak there is an empty box.
        //     2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //     3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //     4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.

        int[] diagonalLeftCheck = new int[10];

        // Variable countPlayer is used to count players characters in a row
        int countPlayer = 0;
        int countMachine = 0;
        int howManyLoops = (size + size) - 1;        
        int y = size-1;
        String direction = "diagleft";

        // Start the complex and buggy looping for diagonal left to right checks
        for (int round=1; round<=howManyLoops; round++) { // How many diagonal lines to check

            int x;

            if (round > 1 && round < size && round >= toWin) {
                int col = y-round;
                x = (0 + round);
                for (int row=0; row<x; row++) {
                    if (x>0) {
                        col = col + 1;
                    }

                    // Inside the main loop
                    int theRow = row;
                    int theCol = col;

                    if (board[theRow][theCol] == player) {
                        countPlayer++;
                        
                        // Check if player has reached toWin, if not then continue looping
                        if (countPlayer == toWin) {
                            diagonalLeftCheck[0] = 1;
                            break;
                        }
                        // Send info about players moves to the AI if player is reaching the win
                        else if (countPlayer == threat) {
                            diagonalLeftCheck[0] = 3; // Threat to AI
                            diagonalLeftCheck[1] = countPlayer;
                            diagonalLeftCheck[2] = theRow; // Send row
                            diagonalLeftCheck[3] = theCol; // Send col
                            diagonalLeftCheck[4] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countPlayer, direction);
                        }
                    }
                    else if (board[theRow][theCol] == machine) {
                        countPlayer = 0;
                        countMachine++;
                        if (countMachine == toWin) {
                            diagonalLeftCheck[0] = 2;
                            break;
                        }
                        else if (countMachine > 1) {
                            diagonalLeftCheck[5] = 1; // AI has more than 1 in a row
                            diagonalLeftCheck[6] = countMachine; // How many ai has
                            diagonalLeftCheck[7] = theRow; // Send row
                            diagonalLeftCheck[8] = theCol; // Send col
                            diagonalLeftCheck[9] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countMachine, direction);
                        }    
                    }
                    // If there is an empty box in the row, erase both counters because the characters need to be next to each other, no empty ones between
                    else if (board[theRow][theCol] == empty) {
                        countPlayer = 0;
                        countMachine = 0;
                    }
                    //Main loop ends here
                }
            }
            else if (round == size) {
                int col = 0;
                x = (0 + round);
                for (int row=0; row<x; row++) {

                    // Inside the main loop now
                    int theRow = row;
                    int theCol = col+row;

                    if (board[theRow][theCol] == player) {
                        countPlayer++;
                        
                        // Check if player has reached toWin, if not then continue looping
                        if (countPlayer == toWin) {
                            diagonalLeftCheck[0] = 1;
                            break;
                        }
                        // Send info about players moves to the AI if player is reaching the win
                        else if (countPlayer == threat) {
                            diagonalLeftCheck[0] = 3; // Threat to AI
                            diagonalLeftCheck[1] = countPlayer;
                            diagonalLeftCheck[2] = theRow; // Send row
                            diagonalLeftCheck[3] = theCol; // Send col
                            diagonalLeftCheck[4] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countPlayer, direction);
                        }
                    }
                    else if (board[theRow][theCol] == machine) {
                        countPlayer = 0;
                        countMachine++;
                        if (countMachine == toWin) {
                            diagonalLeftCheck[0] = 2;
                            break;
                        }
                        else if (countMachine > 1) {
                            diagonalLeftCheck[5] = 1; // AI has more than 1 in a row
                            diagonalLeftCheck[6] = countMachine; // How many ai has
                            diagonalLeftCheck[7] = theRow; // Send row
                            diagonalLeftCheck[8] = theCol; // Send col
                            diagonalLeftCheck[9] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countMachine, direction);
                        }    
                    }
                    // If there is an empty box in the row, erase both counters because the characters need to be next to each other, no empty ones between
                    else if (board[theRow][theCol] == empty) {
                        countPlayer = 0;
                        countMachine = 0;
                    }
                    //Main loop ends here
                }
            }
            else if (round > size) {
                int col = 0;
                x = size - (round-size);
                for (int row=0; row<x; row++) {
                    if (x>0) {
                        col = col + 1;
                    }
                    // Inside the main loop now
                    int theRow = row+size-x;
                    int theCol = col-1;

                    if (board[theRow][theCol] == player) {
                        countPlayer++;
                        
                        // Check if player has reached toWin, if not then continue looping
                        if (countPlayer == toWin) {
                            diagonalLeftCheck[0] = 1;
                            break;
                        }
                        // Send info about players moves to the AI if player is reaching the win
                        else if (countPlayer == threat) {
                            diagonalLeftCheck[0] = 3; // Threat to AI
                            diagonalLeftCheck[1] = countPlayer;
                            diagonalLeftCheck[2] = theRow; // Send row
                            diagonalLeftCheck[3] = theCol; // Send col
                            diagonalLeftCheck[4] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countPlayer, direction);
                        }
                    }
                    else if (board[theRow][theCol] == machine) {
                        countPlayer = 0;
                        countMachine++;
                        if (countMachine == toWin) {
                            diagonalLeftCheck[0] = 2;
                            break;
                        }
                        else if (countMachine > 1) {
                            diagonalLeftCheck[5] = 1; // AI has more than 1 in a row
                            diagonalLeftCheck[6] = countMachine; // How many ai has
                            diagonalLeftCheck[7] = theRow; // Send row
                            diagonalLeftCheck[8] = theCol; // Send col
                            diagonalLeftCheck[9] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countMachine, direction);
                        }    
                    }
                    // If there is an empty box in the row, erase both counters because the characters need to be next to each other, no empty ones between
                    else if (board[theRow][theCol] == empty) {
                        countPlayer = 0;
                        countMachine = 0;
                    }
                // Main loop ends here
                }
            }
        // After each round lets reset this thing and see where it gets us (EDIT 2019-12-12 10:50 - currently the application crashes within this check if size and toWin variables are not the same).
        countPlayer = 0;
        countMachine = 0;
        }
        if (diagonalLeftCheck[4] == 2 && diagonalLeftCheck[0] != 1 && diagonalLeftCheck[0] != 2) {
            diagonalLeftCheck[0] = 0;
        }
        return diagonalLeftCheck;
    }

    /** 
     * getDiagonalRight method goes through the gameboard in diagonals from upper right to down left and returns the information about the situation to the main situation array in the getSituation method.
     * 
     * @param board is the gameboard.
     * @param size is the size of the gameboard.
     * @param player is the human player character.
     * @param machine is the machine character.
     * @param empty is the empty character.
     * @param toWin is how long the consecutive streak needs to be in order to win the game.
     * @param threat is how many consecutive human player characters in a streak means a threat to the AI.
     * @return returns the 10 index int[] array which includes the diagonal right to left info.
     */
    public static int[] getDiagonalRight(char[][] board, int size, char player, char machine, char empty, int toWin, int threat) {

        // DIAGONAL UPPER RIGHT TO DOWN LEFT CHECK ARRAY WILL INCLUDE THIS INFORMATION
        // [0] 0 in this index means no one has won with a diagonal streak (from upper right to down left) and there is no diagonal (RightToLeft) threat to the AI.
        //     1 in this index means the human player has won with a diagonal (RtL) streak.
        //     2 in this index means the machine has won with a diagonal (RtL) streak.
        //     3 in this index means there is a diagonal (RtL) threat to the AI.
        // [1] This index tells how long is the players diagonal (RtL) streak if there is any.
        // [2] This index includes the row index of the gameboard where the players diagonal (RtL) threat streak ends.
        // [3] This index includes the column index of the gameboard where the players diagonal (RtL) threat streak ends.
        // [4] This index tells if the diagonal (RtL) threat streak has an empty box before or after the streak.
        //     0 in this index means no value has been set.
        //     1 in this index means that both before and after the streak there is an empty box.
        //     2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //     3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //     4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.
        // [5] This index tells if the machine has more than one markings in a diagonal (RtL) streak. 1 means true, 0 means false.
        // [6] If index[5] is true, this index tells how long is the diagonal (RtL) streak.
        // [7] If index[5] is true, this index tells the row index from the gameboard where the diagonal (RtL) streak ends.
        // [8] If index[5] is true, this index tells the column index from the gameboard where the diagonal (RtL) streak ends.
        // [9] If index[5] is true, this index tells if the machines diagonal (RtL) streak has an empty box before or after the streak.
        //     0 in this index means no value has been set.
        //     1 in this index means that both before and after the streak there is an empty box.
        //     2 in this index means that both indexes before and after the streak are already taken or there is a wall.
        //     3 in this index means the next gameboard index after the streak is empty, and the index before is taken.
        //     4 in this index means the next gameboard index after the streak is taken already, but the index before is empty.

        int[] diagonalRightCheck = new int[10];

        // Variable countPlayer is used to count players characters in a row
        int countPlayer = 0;
        int countMachine = 0;
        int howManyLoops = (size + size) - 1;
        String direction = "diagright";
        
        for (int round=1; round<=howManyLoops; round++) { // How many diagonal lines to check

            int x;

            if (round > 1 && round < size && round >= toWin) {
                int col = round;
                x = (0 + round);
                for (int row=0; row<x; row++) {

                    // Inside main loop
                    int theRow = row;
                    int theCol = col-1;

                    if (board[theRow][theCol] == player) {
                        countPlayer++;
                        
                        // Check if player has reached toWin, if not then continue looping
                        if (countPlayer == toWin) {
                            diagonalRightCheck[0] = 1;
                            break;
                        }
                        // Send info about players moves to the AI if player is reaching the win
                        else if (countPlayer == threat) {
                            diagonalRightCheck[0] = 3; // Threat to AI
                            diagonalRightCheck[1] = countPlayer;
                            diagonalRightCheck[2] = theRow; // Send row
                            diagonalRightCheck[3] = theCol; // Send col
                            diagonalRightCheck[4] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countPlayer, direction);
                        }
                    }
                    else if (board[theRow][theCol] == machine) {
                        countPlayer = 0;
                        countMachine++;
                        if (countMachine == toWin) {
                            diagonalRightCheck[0] = 2;
                            break;
                        }
                        else if (countMachine > 1) {
                            diagonalRightCheck[5] = 1; // AI has more than 1 in a row
                            diagonalRightCheck[6] = countMachine; // How many ai has
                            diagonalRightCheck[7] = theRow; // Send row
                            diagonalRightCheck[8] = theCol; // Send col
                            diagonalRightCheck[9] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countMachine, direction);
                        }    
                    }
                    // If there is an empty box in the row, erase both counters because the characters need to be next to each other, no empty ones between
                    else if (board[theRow][theCol] == empty) {
                        countPlayer = 0;
                        countMachine = 0;
                    }
                    // Inside main loop
                    col--;
                }
            }
            else if (round == size) {
                int col = size-1;
                x = (0 + round);
                for (int row=0; row<x; row++) {

                    // Inside main loop
                    int theRow = row;
                    int theCol = col;

                    if (board[theRow][theCol] == player) {
                        countPlayer++;

                        // Check if player has reached toWin, if not then continue looping
                        if (countPlayer == toWin) {
                            diagonalRightCheck[0] = 1;
                            break;
                        }
                        // Send info about players moves to the AI if player is reaching the win
                        else if (countPlayer == threat) {
                            diagonalRightCheck[0] = 3; // Threat to AI
                            diagonalRightCheck[1] = countPlayer;
                            diagonalRightCheck[2] = theRow; // Send row
                            diagonalRightCheck[3] = theCol; // Send col
                            diagonalRightCheck[4] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countPlayer, direction);
                        }
                    }
                    else if (board[theRow][theCol] == machine) {
                        countPlayer = 0;
                        countMachine++;
                        if (countMachine == toWin) {
                            diagonalRightCheck[0] = 2;
                            break;
                        }
                        else if (countMachine > 1) {
                            diagonalRightCheck[5] = 1; // AI has more than 1 in a row
                            diagonalRightCheck[6] = countMachine; // How many ai has
                            diagonalRightCheck[7] = theRow; // Send row
                            diagonalRightCheck[8] = theCol; // Send col
                            diagonalRightCheck[9] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countMachine, direction);
                        }    
                    }
                    // If there is an empty box in the row, erase both counters because the characters need to be next to each other, no empty ones between
                    else if (board[theRow][theCol] == empty) {
                        countPlayer = 0;
                        countMachine = 0;
                    }
                    col--;
                    // The main loop ends here
                }
            }
            else if (round > size) {
                int col = size-1;
                x = size - (round-size);
                for (int row=0; row<x; row++) {

                    // Inside main loop
                    int theRow = row+size-x;
                    int theCol = col;

                    if (board[theRow][theCol] == player) {
                        countPlayer++;
                        
                        // Check if player has reached toWin, if not then continue looping
                        if (countPlayer == toWin) {
                            diagonalRightCheck[0] = 1;
                            break;
                        }
                        // Send info about players moves to the AI if player is reaching the win
                        else if (countPlayer == threat) {
                            diagonalRightCheck[0] = 3; // Threat to AI
                            diagonalRightCheck[1] = countPlayer;
                            diagonalRightCheck[2] = theRow; // Send row
                            diagonalRightCheck[3] = theCol; // Send col
                            diagonalRightCheck[4] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countPlayer, direction);
                        }
                    }
                    else if (board[theRow][theCol] == machine) {
                        countPlayer = 0;
                        countMachine++;
                        if (countMachine == toWin) {
                            diagonalRightCheck[0] = 2;
                            break;
                        }
                        else if (countMachine > 1) {
                            diagonalRightCheck[5] = 1; // AI has more than 1 in a row
                            diagonalRightCheck[6] = countMachine; // How many ai has
                            diagonalRightCheck[7] = theRow; // Send row
                            diagonalRightCheck[8] = theCol; // Send col
                            diagonalRightCheck[9] = isNextOrBeforeEmpty(board, theRow, theCol, empty, size, countMachine, direction);
                        }    
                    }
                    // If there is an empty box in the row, erase both counters because the characters need to be next to each other, no empty ones between
                    else if (board[theRow][theCol] == empty) {
                        countPlayer = 0;
                        countMachine = 0;
                    }
                    col--;
                    // The main loop ends here
                }
            }
            countPlayer = 0;
            countMachine = 0;
        }
        if (diagonalRightCheck[4] == 2 && diagonalRightCheck[0] != 1 && diagonalRightCheck[0] != 2) {
            diagonalRightCheck[0] = 0;
        }
        return diagonalRightCheck;
    }

    /** 
     * isThisEmpty method returns a boolean value if the given gameboard index is empty or not.
     * This particular method is to check any input with proper index values.
     * 
     * @param board is the gameboard.
     * @param a is the given row index number.
     * @param b is the given column index number.
     * @param empty is the character marking an empty box in the gameboard.
     * @return Returns true if the given index is empty. Else returns false.
     */
    public static boolean isThisEmpty(char[][] board, int a, int b, char empty) {
        if (board[a][b] == empty) {
            return true;
        }
        else {
            return false;
        }
    }

    /** 
     * isThisUserInputEmpty method returns a boolean value if the given board index is empty or not.
     * This particular method is only for checking the user input which are given as + 1 compared to the index values.
     * 
     * @param board is the gameboard.
     * @param a is the given row index number.
     * @param b is the given column index number.
     * @param empty is the character marking an empty box in the gameboard.
     * @return returns true if the given index is empty, else false.
     */
    public static boolean isThisUserInputEmpty(char[][] board, int a, int b, char empty) {
        a = a - 1;
        b = b - 1;
        if (board[a][b] == empty) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * isNextOrBeforeEmpty returns an integer value which tells information about the given streak compared to the gameboard.
     * 
     * If the value is 0 it means no value has been set.
     * If the value is 1 it means both indexes before and after the streak are empty.
     * If the value is 2 it means both indexes are taken.
     * If the value is 3 it means the index after is empty but the index before is taken.
     * If the value is 4 it means the index before is empty but the index after is taken.
     * 
     * @param board is the gameboard.
     * @param a is the given row index value where streak ends.
     * @param b is the given column index value where streak ends.
     * @param empty is the empty character.
     * @param size is the size of the gameboard.
     * @param count is how long is the given consecutive streak.
     * @param direction is whether the streak is horizontal, vertical, diagleft, or diagright.
     * @return returns the integer value according to the given information.
     */
    public static int isNextOrBeforeEmpty (char[][] board, int a, int b, char empty, int size, int count, String direction) {
        int value = 0;
        if (direction.equals("horizontal")) {
            boolean nextEmpty = false;
            boolean beforeEmpty = false;
            // This streakStartRow variable is not used here but it's here as the authors own note.
            // int streakStartRow = a;
            int streakStartCol = b - (count-1);
            int nextCol = (b + 1);
            int beforeCol = (streakStartCol - 1);
            if (nextCol <= (size-1) && board[a][nextCol] == empty) {
                nextEmpty = true;
            }
            if (beforeCol >= 0 && board[a][beforeCol] == empty) {
                beforeEmpty = true;
            }
            if (nextEmpty && beforeEmpty) {
                value = 1;
            }
            else if (!nextEmpty && !beforeEmpty) {
                value = 2;
            }
            else if (nextEmpty && !beforeEmpty) {
                value = 3;
            }
            else if (!nextEmpty && beforeEmpty) {
                value = 4;
            }
        }
        else if (direction.equals("vertical")) {
            boolean nextEmpty = false;
            boolean beforeEmpty = false;
            int streakStartRow = a - (count - 1);
            // This streakStartCol variable is not used here but it's here as the authors own note.
            // int streakStartCol = b;
            int nextRow = (a + 1);
            int beforeRow = (streakStartRow - 1);
            if (nextRow <= (size-1) && board[nextRow][b] == empty) {
                nextEmpty = true;
            }
            if (beforeRow >= 0 && board[beforeRow][b] == empty) {
                beforeEmpty = true;
            }
            if (nextEmpty && beforeEmpty) {
                value = 1;
            }
            else if (!nextEmpty && !beforeEmpty) {
                value = 2;
            }
            else if (nextEmpty && !beforeEmpty) {
                value = 3;
            }
            else if (!nextEmpty && beforeEmpty) {
                value = 4;
            }
        }
        else if (direction.equals("diagleft")) {   
            boolean nextEmpty = false;
            boolean beforeEmpty = false;
            int streakStartRow = a - (count-1);
            int streakStartCol = b - (count-1);
            int nextRow = (a + 1);
            int nextCol = (b + 1);
            int beforeRow = (streakStartRow - 1);
            int beforeCol = (streakStartCol - 1);
            if (nextRow <= (size-1) && nextCol <= (size-1) && board[nextRow][nextCol] == empty) {
                nextEmpty = true;
            }
            if (beforeRow >= 0 && beforeCol >= 0 && board[beforeRow][beforeCol] == empty) {
                beforeEmpty = true;
            }
            if (nextEmpty && beforeEmpty) {
                value = 1;
            }
            else if (!nextEmpty && !beforeEmpty) {
                value = 2;
            }
            else if (nextEmpty && !beforeEmpty) {
                value = 3;
            }
            else if (!nextEmpty && beforeEmpty) {
                value = 4;
            }
        }
        else if (direction.equals("diagright")) {
            boolean nextEmpty = false;
            boolean beforeEmpty = false;
            int streakStartRow = a + (count-1);
            int streakStartCol = b - (count-1);
            int nextRow = (a - 1);
            int nextCol = (b + 1);
            int beforeRow = (streakStartRow - 1);
            int beforeCol = (streakStartCol + 1);
            if (nextRow <= (size-1) && nextCol <= (size-1) && board[nextRow][nextCol] == empty) {
                nextEmpty = true;
            }
            if (beforeRow >= 0 && beforeCol >= 0 && board[beforeRow][beforeCol] == empty) {
                beforeEmpty = true;
            }
            if (nextEmpty && beforeEmpty) {
                value = 1;
            }
            else if (!nextEmpty && !beforeEmpty) {
                value = 2;
            }
            else if (nextEmpty && !beforeEmpty) {
                value = 3;
            }
            else if (!nextEmpty && beforeEmpty) {
                value = 4;
            }
        }    
        return value;
    }

    /**
     * This method checks if the given gameboard index fits inside the board
     * 
     * @param a row index
     * @param b column index
     * @param size size of the board
     * @return true if the given 2d array index is inside the array
     */
    public static boolean isThisUserInputInsideTheBoard (int a, int b, int size) {
        a = a - 1;
        b = b - 1;
        int c = size - 1;
        if (a >= 0 && a <= c && b >= 0  && b <= c) {
            return true;
        }
        else {
            return false;
        }
    }
}

/** 
 * The AI class includes methods to determine which would be the best move for the machine.
 */
class AI {

    /**
     * <p>The decision method (when fully functional) compares the gameboard situation with these priorities:
     * <p>1. If there is only one left for the machine to complete a streak, it does this in order to win the game in the current round. 
     * <p>2. If there is a threat, which means in difficulty level 1 that the human player is missing only one characters from a streak and there's empty boxes around the current streak, the machine tries to prevent the human from winning by blocking the way
     * <p>3. If there is no current threat the machine finds for the longest streak (more than 1 character) of machine characters and continues this streak according to the information given.
     * 
     * <p>With a higher threat level, the machine would prevent the humans streak even earlier.
     * 
     * <p>At this moment (2019-12-12 10:56) this method is working somewhat properly but there are still bugs in it and when the game goes further the accuracy of the AI decreases and it will more often not recognize threats but use random values instead.
     * 
     * @param board is the gameboard.
     * @param empty is the character for empty box.
     * @param size is the size of the gameboard.
     * @param toWin is the value of how many consecutive characters are needed in a streak to win the game.
     * @param situation is the int[] array which holds the current situation of the game. 
     * @return returns the decision as an int[] array with 2 indexes, first is the row index and second is the column index.
     */
    public static int[] decision(char[][] board, char empty, int size, int toWin, int[] situation) {

        int[] outcome = new int[2];
        int almostThere = (toWin - 1);

        // Here starts the actual AI part of the code
        // The AI tries to work with these principles:
        //
        // 1. If the AI has almost won (only one mark missing) it pushes to find this missing empty place to win.
        // 2. If the AI cannot win at current round of the game, it prevents the threat (almost winning) caused by the human player. This takes consideration the intelligence/difficulty level.
        // 3. If the AI cannot win at current round and there is no immediate threats, AI will try to find it's own character streaks and continue the longest of them.
        // 4. If none of the above is true the AI will randomly choose an empty index inside the gameboard.
        
        // AI Principle 1 starts here:

        // This means if machines horizontal streak is almost a win and there is empty places next to it
        if (situation[7] == almostThere && situation[10] != 0 && situation[10] != 2) {
            int streakRow = situation[8];
            int streakEndCol = situation[9];
            int streakStartCol = situation[9] - situation[7] + 1;

            // This means if there is both empty or next empty we return outcome to choose the next one
            if (situation[10] == 1 || situation[10] == 3) {
                int chosenRow = streakRow;
                int chosenCol = streakEndCol + 1;
                outcome[0] = chosenRow;
                outcome[1] = chosenCol;
                return outcome;
            }
            // Else if last empty we return outcome to choose the before one
            else if (situation[10] == 4) {
                int chosenRow = streakRow;
                int chosenCol = streakStartCol - 1;
                outcome[0] = chosenRow;
                outcome[1] = chosenCol;
                return outcome;
            }
        }
        // This means if machines vertical streak is almost a win and there is empty places next to it
        if (situation[17] == almostThere && situation[20] != 0 && situation[20] != 2) {
            int streakEndRow = situation[18];
            int streakCol = situation[19];
            int streakStartRow = situation[18] - situation[17] + 1;

            // This means if there is both empty or next empty we return outcome to choose the next one
            if (situation[20] == 1 || situation[20] == 3) {
                int chosenRow = streakEndRow + 1;
                int chosenCol = streakCol;
                outcome[0] = chosenRow;
                outcome[1] = chosenCol;
                return outcome;
            }
            // Else if last empty we return outcome to choose the before one
            else if (situation[20] == 4) {
                int chosenRow = streakStartRow - 1;
                int chosenCol = streakCol;
                outcome[0] = chosenRow;
                outcome[1] = chosenCol;
                return outcome;
            }
        }
        // This means if machines diagonalLeft streak is almost a win and there is empty places next to it
        if (situation[27] == almostThere && situation[30] != 0 && situation[30] != 2) {
            int streakSize = situation[27];
            int streakEndRow = situation[28];
            int streakEndCol = situation[29];
            int streakStartRow = streakEndRow - (streakSize - 1);
            int streakStartCol = streakEndCol - (streakSize - 1);

            // This means if there is both empty or next empty we return outcome to choose the next one
            if (situation[30] == 1 || situation[30] == 3) {
                int chosenRow = streakEndRow + 1;
                int chosenCol = streakEndCol + 1;
                outcome[0] = chosenRow;
                outcome[1] = chosenCol;
                return outcome;
            }
            // Else if last empty we return outcome to choose the before one
            else if (situation[30] == 4) {
                int chosenRow = streakStartRow - 1;
                int chosenCol = streakStartCol - 1;
                outcome[0] = chosenRow;
                outcome[1] = chosenCol;
                return outcome;
            }
        }
        // This means if machines diagonalRight streak is almost a win and there is empty places next to it
        if (situation[37] == almostThere && situation[40] != 0 && situation[40] != 2) {
            int streakSize = situation[37];
            int streakEndRow = situation[38];
            int streakEndCol = situation[39];
            int streakStartRow = streakEndRow - (streakSize + 1);
            int streakStartCol = streakEndCol + (streakSize + 1);

            // This means if there is both empty or next empty we return outcome to choose the next one
            if (situation[40] == 1 || situation[40] == 3) {
                int chosenRow = streakEndRow - 1;
                int chosenCol = streakEndCol - 1;
                outcome[0] = chosenRow;
                outcome[1] = chosenCol;
                return outcome;
            }
            // Else if last empty we return outcome to choose the before one
            else if (situation[40] == 4) {
                int chosenRow = streakStartRow + 1;
                int chosenCol = streakStartCol - 1;
                outcome[0] = chosenRow;
                outcome[1] = chosenCol;
                return outcome;
            }
        }

        // AI Principle 2 starts here:

        // If there was no almostThere for AI, we check if there is a threat
        if (situation[1] == 3 || situation[11] == 3 || situation[21] == 3 || situation[31] == 3) {
            // System.out.println("Machine has noticed a threat."); // This line is for testing purposes only.

            // If threat is horizontal
            if (situation[1] == 3) {
                // System.out.println("Threat is horizontal");  // This line is for testing purposes only.

                if (situation[5] == 3 || situation[5] == 1) {           // Threat is next or both, if both, too bad then
                    // System.out.println("Player has empty spot after horizontal streak.");    // This line is for testing purposes only.   
                    // int streakSize = situation[2]; // this variable is not used here but it's left here for testing purposes.
                    int chosenRow = situation[3];
                    int chosenCol = situation[4];
                    chosenCol = chosenCol + 1;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    // System.out.println("Machine chose " + outcome[0] + outcome[1]); // This line is for testing purposes only.
                    return outcome;    
                } 
                else if (situation[5] == 4) { // Threat is before
                    // System.out.println("Player has empty spot before horizontal streak.");   // This line is for testing purposes only.
                    int streakSize = situation[2];
                    int chosenRow = situation[3];
                    int chosenCol = situation[4];
                    chosenCol = chosenCol - streakSize;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    // System.out.println("Machine chose " + outcome[0] + outcome[1]); // This line is for testing purposes only.
                    return outcome;    
                }
            }
            // If threat is vertical
            else if (situation[11] == 3) {
                // System.out.println("Threat is vertical");    // This line is for testing purposes only.
                if (situation[15] == 3 || situation[15] == 1) { // Threat is next or both, if both, too bad then.
                    // System.out.println("Player has empty spot after vertical streak.");  // This line is for testing purposes only.
                    // int streakSize = situation[12]; // This variable is not used, it's left here for testing purposes.
                    int chosenRow = situation[13];
                    int chosenCol = situation[14];
                    chosenRow = chosenRow + 1;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    // System.out.println("Machine chose " + outcome[0] + outcome[1]);  // This line is for testing purposes only.
                    return outcome;    
                } 
                else if (situation[15] == 4) { // Threat is before
                    // System.out.println("Player has empty spot before vertical streak."); // This line is for testing purposes only.
                    int streakSize = situation[12];
                    int chosenRow = situation[13];
                    int chosenCol = situation[14];
                    chosenRow = chosenRow - streakSize;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    // System.out.println("Machine chose " + outcome[0] + outcome[1]);  // This line is for testing purposes only.
                    return outcome;    
                }
            }
            // If threat is diagonal left
            else if (situation[21] == 3) {
                // System.out.println("Threat is diagonalLeft");    // This line is for testing purposes only.
                if (situation[25] == 3 || situation[25] == 1) { // Threat is next or both, if both, too bad then for the machine.
                    // System.out.println("Player has empty spot after diagonalLeft streak."); // This line is for testing purposes only.
                    // int streakSize = situation[22]; // This variable is not used here. Testing purposes only.
                    int chosenRow = situation[23];
                    int chosenCol = situation[24];
                    chosenRow = chosenRow + 1;
                    chosenCol = chosenCol + 1;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    // System.out.println("Machine chose " + outcome[0] + outcome[1]); // This line is for testing purposes only.
                    return outcome;    
                } 
                else if (situation[25] == 4) { // Threat is before
                    // System.out.println("Player has empty spot before diagonalLeft streak."); // This line is for testing purposes only.
                    int streakSize = situation[22];
                    int chosenRow = situation[23];
                    int chosenCol = situation[24];
                    chosenRow = chosenRow - streakSize;
                    chosenCol = chosenCol - streakSize;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    // System.out.println("Machine chose " + outcome[0] + outcome[1]); // This line is for testing purposes only.
                    return outcome;    
                }
            }
            // If threat is diagonal right
            else if (situation[31] == 3) {
                // System.out.println("Threat is diagonalRight");   // This line is for testing purposes only.
                if (situation[35] == 3 || situation[35] == 1) { // Threat is next or both, if both, too bad then
                    // System.out.println("Player has empty spot after diagonalRight streak."); // This line is for testing purposes only.
                    // int streakSize = situation[32]; // This variable is not used, it's for testing purposes only.
                    int chosenRow = situation[33];
                    int chosenCol = situation[34];
                    chosenRow = chosenRow - 1;
                    chosenCol = chosenCol - 1;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    // System.out.println("Machine chose " + outcome[0] + outcome[1]); // This line is for testing purposes only.
                    return outcome;    
                } 
                else if (situation[35] == 4) { // Threat is before
                    // System.out.println("Player has empty spot before diagonalRight streak."); // This line is for testing purposes only.
                    int streakSize = situation[32];
                    int chosenRow = situation[33];
                    int chosenCol = situation[34];
                    chosenRow = chosenRow + streakSize;
                    chosenCol = chosenCol + streakSize;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    // System.out.println("Machine chose " + outcome[0] + outcome[1]); // This line is for testing purposes only.
                    return outcome;    
                }
            }
        }

        // AI Principle 3 starts here:
        /*
        // EDIT: 2019-12-12 11:08 THIS SEQUENCE OF THE AI IS NOT WORKING PROPERLY AT THE MOMENT SO IT'S CLOSED INSIDE COMMENTS.
        // However the principle here is that if the machine has at least one or more streaks (longer than 1 character) this part checks which one of the streaks is the longest and tries to continue that one.

        // If there was no almostThere for AI and no threats, we check if theres at least something with machine
        if (situation[6] == 1 || situation[16] == 1 || situation[26] == 1 || situation[36] == 1) {

            // Lets see which way is the biggest streak at the moment
            String whichOneHighest = getHighestNumberToString(situation[6], situation[16], situation[26], situation[36]);

            System.out.println(whichOneHighest); // This line is for testing purposes only.

            // If situation is horizontal
            if (whichOneHighest.equals("horizontal")) {
                int streakRow = situation[8];
                int streakEndCol = situation[9];
                int streakStartCol = situation[9] - situation[7] + 1;

                // This means if there is both empty or next empty we return outcome to choose the next one
                if (situation[10] == 1 || situation[10] == 3) {
                    int chosenRow = streakRow;
                    int chosenCol = streakEndCol + 1;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    return outcome;
                }
                // Else if last empty we return outcome to choose the before one
                else if (situation[10] == 4) {
                    int chosenRow = streakRow;
                    int chosenCol = streakStartCol - 1;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    return outcome;
                }
            }
            // If situation is vertical
            else if (whichOneHighest.equals("vertical")) {
                int streakEndRow = situation[18];
                int streakCol = situation[19];
                int streakStartRow = situation[18] - situation[17] + 1;

                // This means if there is both empty or next empty we return outcome to choose the next one
                if (situation[20] == 1 || situation[20] == 3) {
                    int chosenRow = streakEndRow + 1;
                    int chosenCol = streakCol;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    return outcome;
                }
                // Else if last empty we return outcome to choose the before one
                else if (situation[20] == 4) {
                    int chosenRow = streakStartRow - 1;
                    int chosenCol = streakCol;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    return outcome;
                }
            }
            // If situation is diagonal left 
            else if (whichOneHighest.equals("diagleft")) {
                int streakSize = situation[27];
                int streakEndRow = situation[28];
                int streakEndCol = situation[29];
                int streakStartRow = streakEndRow - (streakSize - 1);
                int streakStartCol = streakEndCol - (streakSize - 1);

                // This means if there is both empty or next empty we return outcome to choose the next one
                if (situation[30] == 1 || situation[30] == 3) {
                    int chosenRow = streakEndRow + 1;
                    int chosenCol = streakEndCol + 1;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    return outcome;
                }
                // Else if last empty we return outcome to choose the before one
                else if (situation[30] == 4) {
                    int chosenRow = streakStartRow - 1;
                    int chosenCol = streakStartCol - 1;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    return outcome;
                }
            }
            // This means if machines diagonalRight streak is almost a win and there is empty places next to it
            else if (whichOneHighest.equals("diagright")) {
                int streakSize = situation[37];
                int streakEndRow = situation[38];
                int streakEndCol = situation[39];
                int streakStartRow = streakEndRow - (streakSize + 1);
                int streakStartCol = streakEndCol + (streakSize + 1);

                // This means if there is both empty or next empty we return outcome to choose the next one
                if (situation[40] == 1 || situation[40] == 3) {
                    int chosenRow = streakEndRow - 1;
                    int chosenCol = streakEndCol - 1;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    return outcome;
                }
                // Else if last empty we return outcome to choose the before one
                else if (situation[40] == 4) {
                    int chosenRow = streakStartRow + 1;
                    int chosenCol = streakStartCol - 1;
                    outcome[0] = chosenRow;
                    outcome[1] = chosenCol;
                    return outcome;
                }
            }
        }
        // CLOSED AI PRINCIPLE 3 COMMENTS ENDS HERE!
        */

        // AI Principle 4 starts here:

        // And if there is no threat and no machine streaks just take random
        else { 
            // System.out.println("There seems to be no immidiate threats to aI so we pick random"); // This line is for testing purposes only.
            int ma = getRandomInsideTable(0, size);
            int mb = getRandomInsideTable(0, size);
            // System.out.println("Machine chose randomly " + a + b); // This line is for testing purposes only.
            // System.out.println("is it empty " + Checks.isThisEmpty(board, a, b, empty)); // This line is for testing purposes only.
            while (!Checks.isThisEmpty(board, ma, mb, empty)) {
                ma = getRandomInsideTable(0, size);
                mb = getRandomInsideTable(0, size);
                // System.out.println("Machine chose again" + a + b); // This line is for testing purposes only.
                // System.out.println("is it empty " + Checks.isThisEmpty(board, a, b, empty)); // This line is for testing purposes only.
            }
            outcome[0] = ma;
            outcome[1] = mb;
        }
        return outcome;
    }

    /**
     * This is a last minute additional helper method for the AI because sometimes it tries to enter its mark on an already filled gameboard box. This method is used to pick new empty one.
     * 
     * @param board is the gameboard.
     * @param empty is the empty characted.
     * @param size is the size of the gameboard.
     * @return returns two integers which point at an empty index on the gameboard.
     */
    public static int[] getRandomEmptyForMachine (char[][] board, char empty, int size) {
            int[] emptyArrayIndex = new int[2];
            int maca = getRandomInsideTable(0, size);
            int macb = getRandomInsideTable(0, size);
            while (!Checks.isThisEmpty(board, maca, macb, empty)) {
                maca = getRandomInsideTable(0, size);
                macb = getRandomInsideTable(0, size);
            }
            return emptyArrayIndex;
    }
    
    /**
     * This method returns a random index number for a row or a column, inside an array of a given size.
     * 
     * @param min is the minimum value this method would return (0).
     * @param max is the maximum value this method would return (size of the gameboard).
     * @return int value to return.
     */
    public static int getRandomInsideTable(int min, int max) {
        int result = (int) (Math.random() * (max)) + min;
        return result;
    }

    /**
     * <p>This method is helping the AI to determine which of the current checks (horizontal, vertical, diagleft, diagright) include the longest streak for the human or the machine in order to help decide which streak to prevent or continue.
     * 
     * <p>This method is only used within the AI decision principle 3.
     * 
     * <p>NOTE 2019-12-12 11:21 This method is currently not used as the principle 3 is not functioning properly and is turned off.
     * 
     * @param num1 First comparable number (horizontal streak length).
     * @param num2 Second comparable number (vertical streak length).
     * @param num3 Third number to compare (diagonal streak from left to right length).
     * @param num4 Fourth number to compare (diagonal streak from right to left length).
     * @return Returns the String representation of which direction has the longest streak.
     */
    public static String getHighestNumberToString(int num1, int num2, int num3, int num4) {
        String result = "";
        if (num1 >= num2 && num1 >= num3 && num1 >= num4) {
            result += "horizontal";
        } else if (num2 >= num1 && num2 >= num3 && num2 >= num4) {
            result += "vertical";
        } else if (num3 >= num1 && num3 >= num2 && num3 >= num4) {
            result += "diagleft";
        } else if (num4 >= num1 && num4 >= num2 && num4 >= num3) {
            result += "diagright";
        }
        return result;
    }
}
