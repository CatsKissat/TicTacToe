import java.util.Scanner;

class TicTacToe
{
    // Debug
    static boolean fastDebug = false;
    static boolean disableClearScreen = false;
    static int debugBoardSize = 3;
    static int debugTokensToWin = 3;

    // Board
    static char[][] gameBoard = new char[0][0];
    static int boardMin = 3;
    static int boardMax = 12;
    static int movesLeft = 0;

    // Board characters
    static char emptySpace = ' ';
    static char xPosAsChar = 'A';
    static char playerToken = 'X';
    static char cpuToken = 'O';

    // Other
    static Scanner input = new Scanner(System.in);
    static boolean playerWon = false;
    static boolean aiWon = false;
    static AI aiPlayer = new AI();

    // Gameplay texts
    static String incorrectInputMsg = "Incorrect input detected! Use integer numbers only!";
    static String coordsInfoMsg = "You are able to set your next token to the board by giving coordinates for example, A1.";
    static String askCoordsMsg = "Please enter coordinates where you would like to set your next token:";
    static String youWinMsg = "You are victorious!";
    static String drawMsg = "The gameboard run out of empty spaces. It's draw!";
    static String youLoseMsg = "AI dominated you like a maggot!";

    public static void main(String[] args) throws Exception
    {
        int tokensNeedToWin = 0;
        boolean gameEnded = false;
        boolean playMore = true;

        while(playMore)
        {
            tokensNeedToWin = initializeGameBoard();
            setGameBoardEmpty();
            while(true)
            {
                printGraphics();
                takeInput();
                gameEnded = checkEndConditions(tokensNeedToWin);
                if(gameEnded)
                {
                    break;
                }
                aiTurn(tokensNeedToWin);
                gameEnded = checkEndConditions(tokensNeedToWin);
                if(gameEnded)
                {
                    break;
                }
            }
            printGraphics();
            gameOver();
            playMore = checkIfRematch();
        }

    }

    private static void setGameBoardEmpty()
    {
        // Set emptySpaces to all gameboard slots
        for(int y = 0; y < gameBoard.length; y++)
        {
            for(int x = 0; x < gameBoard.length; x++)
            {
                gameBoard[y][x] = emptySpace;
            }
        }
    }

    private static void aiTurn(int tokensNeedToWin)
    {
        // int[] for AI moves [y, x]
        int[] tokenPos = new int[2];
        // Get AI move
        tokenPos = aiPlayer.generateMove(gameBoard, emptySpace, cpuToken, tokensNeedToWin);
        // This is for advanced minimaxmove
        // tokenPos = aiPlayer.minimaxMove(gameBoard, emptySpace, cpuToken, playerToken, tokensNeedToWin);
        // Set AI move to gameBoard[y][x]
        gameBoard[tokenPos[0]][tokenPos[1]] = cpuToken;
        // Decrease amount how many moves aka spaces are left
        movesLeft--;
    }

    public static void clearScreen()
    {
        if(!disableClearScreen)
        {
            System.out.print("\033[H\033[2J");
        }
    }

    public static int initializeGameBoard()
    {
        int attempts = 0;
        int boardSize = 0;
        int tokensMin = 3;
        int tokensMax = 5;
        int tokensNeedToWin = 0;
        boolean incorrectInput = false;
        String[] tokensAvailableArrayMsg = {"3", "3, 4", "3, 4, 5"};
        String tokensAvailableMsg = "";
        String welcomeMsg = "Welcome to Tic-Tac-Toe.";
        String gameIntroductionMsg = "This is a game where you have to connect certain amount of your tokens to win the game.";
        String boardMinMsg = "The minimun size for the gameboard is " + boardMin + "x" + boardMin + ".";
        String boardMaxMsg = "The maximum size for the gameboard is " + boardMax + "x" + boardMax + ".";
        String enterSizeMsg = "Please enter the size of the gameboard:";
        String incorrectSizeMsg = "An incorrect size entered!";
        String tokensInfoMsg = "The game will end if there are 3, 4, or 5 tokens connected.";
        String tokensRangeAvailableMsg = "The gameboard you choose will support total connected tokens of: ";
        String tokensRangeMsg = "From how many tokens winner will be decided?";

        // Debug
        if(fastDebug)
        {
            boardSize = debugBoardSize;
            tokensNeedToWin = debugTokensToWin;
        }
        // End debug

        // Get input for board size
        while(boardSize < boardMin || boardSize > boardMax)
        {
            clearScreen();
            System.out.println(welcomeMsg);
            System.out.println(gameIntroductionMsg);
            System.out.println(boardMinMsg);
            System.out.println(boardMaxMsg);
            // Check if user has tried to enter board size before
            if(attempts > 0)
            {
                if(incorrectInput)
                {
                    System.out.println();
                    System.out.println(incorrectInputMsg);
                    incorrectInput = false;
                }
                // Input under or over minimum
                else
                {
                    System.out.println();
                    System.out.println(incorrectSizeMsg);
                }
            }
            System.out.println(enterSizeMsg);
            // Check is input valid
            try
            {
                boardSize = Integer.parseInt(input.nextLine().trim().replaceAll("\\s",""));
            }
            catch(Exception e)
            {
                incorrectInput = true;
            }
            attempts++;
        }

        // Set board size
        gameBoard = new char[boardSize][boardSize];

        // Set movesLeft for situations where empty spaces run out.
        movesLeft = gameBoard.length * gameBoard.length;

        // Check how many tokens-to-connect-for-victory available (for a message below)
        if(gameBoard.length == 3)
        {
            tokensAvailableMsg = tokensAvailableArrayMsg[0];
        }
        else if(gameBoard.length == 4)
        {
            tokensAvailableMsg = tokensAvailableArrayMsg[1];
        }
        else
        {
            tokensAvailableMsg = tokensAvailableArrayMsg[2];
        }

        // Get input for how many tokens should be connected for victory
        attempts = 0;
        while(tokensNeedToWin < tokensMin || tokensNeedToWin > tokensMax || tokensNeedToWin > gameBoard.length)
        {
            clearScreen();
            System.out.println(tokensInfoMsg);
            System.out.println(tokensRangeAvailableMsg + tokensAvailableMsg);
            // Check if user has tried to enter token range before
            if(attempts > 0)
            {
                if(incorrectInput)
                {
                    System.out.println();
                    // Change to correct error
                    System.out.println(incorrectInputMsg);
                    incorrectInput = false;
                }
                // Input under or over minimum
                else
                {
                    System.out.println();
                    // Change to correct error
                    System.out.println(incorrectSizeMsg);
                }
            }
            System.out.println(tokensRangeMsg);
            try
            {
                tokensNeedToWin = Integer.parseInt(input.nextLine().trim());
            }
            catch(Exception e)
            {
                incorrectInput = true;
            }
            attempts++;
        }
        return tokensNeedToWin;
    }

    public static void printGraphics()
    {
        int alphabetOffset = 4;

        clearScreen();
        // Print empty space to the upper left corner
        for(int j = 0; j < alphabetOffset; j++)
        {
            System.out.print(emptySpace);
        }
        // Print alphabets for x coordinates
        for(int i = 0; i < gameBoard.length; i++)
        {
            System.out.print(emptySpace);
            System.out.print((char)((xPosAsChar + i)));
            System.out.print(emptySpace);
        }
        System.out.println();

        // Print Board
        for(int y = 0; y < gameBoard.length; y++)
        {
            for(int x = 0; x < gameBoard.length; x++)
            {
                if(x == 0)
                {
                    // Print numbers for y coordinates
                    if(y < 9)
                    {
                        System.out.print(" " + (y + 1) + ". ");
                    }
                    else
                    {
                        System.out.print((y + 1) + ". ");
                    }
                    printGameboardSlot(y, x);
                }
                // Print slots for tokens and empty spaces
                else
                {
                    printGameboardSlot(y, x);
                }
            }
            System.out.println();
        }
    }

    private static void printGameboardSlot(int y, int x)
    {
        char squareBracketOpen = '[';
        char squareBracketClose = ']';

        // Prints the gameboard slots and their values for example, [ ][ ][X]
        System.out.print(squareBracketOpen);
        System.out.print(gameBoard[y][x]);
        System.out.print(squareBracketClose);
    }

    public static void takeInput()
    {
        int inputTimes = 0;
        int charToIntCorrection = 64;
        String coords = "";
        String errorMsg = "";
        String newCoordsMsg = " Choose new coordinates please.";
        String spaceNotEmptyMsg = "The space is already occupied." + newCoordsMsg;
        String yNotInRangeMsg = "The number coordinate is out of range." + newCoordsMsg;
        String xNotInRangeMsg = "The alphabet coordinate is out of range." + newCoordsMsg;

        // Print info how to use coordinates
        printCoordsInfo();
        while(true)
        {
            // Check if user has already tried an input
            if(inputTimes >= 1)
            {
                printGraphics();
                printCoordsInfo();
                System.out.println();
                // A general error message string that changes depending on an user input errors
                System.out.println(errorMsg);
            }
            try
            {
                coords = input.nextLine().trim().toUpperCase().replaceAll("\\s","");
                // Convert the first char on an input string to int
                int xCoord = (int)coords.charAt(0);
                // Check is the first char of input in range of x coordinate alphabets for example, A to C
                if(xCoord >= (int)xPosAsChar && xCoord <= ((int)xPosAsChar + (gameBoard.length - 1)))
                {
                    // Convert x int alphabet coordinate (for example, A is int 65) to more logical coordinate 0, 1, 2, etc. values
                    int xPos = (xCoord - charToIntCorrection);

                    int yPos = 0;
                    // Convert the next 1-2 chars of input to int
                    String tempString = new String();
                    for(int i = 1; i < coords.length(); i++)
                    {
                        tempString += coords.charAt(i);
                    }
                    yPos = Integer.parseInt(tempString);

                    // Check is Y coordinate in range
                    if(yPos >= 1 && yPos <= gameBoard.length)
                    {
                        // Check is token slot empty
                        if(gameBoard[yPos - 1][xPos - 1] == emptySpace)
                        {
                            gameBoard[yPos - 1][xPos - 1] = playerToken;
                            break;
                        }
                        else
                        {
                            // Incorrect input
                            // Space not empty
                            errorMsg = spaceNotEmptyMsg;
                        }
                    }
                    else
                    {
                        // Incorrect input
                        // Y int not in range
                        errorMsg = yNotInRangeMsg;
                    }
                }
                else
                {
                    // Incorrect input
                    // X (int)input not in range
                    errorMsg = xNotInRangeMsg;
                }
            }
            catch(Exception e)
            {
                // An incorrect input
                // Don't crash the game
                errorMsg = incorrectInputMsg;
            }
            // Increase inputTimes that the game willl show error message after a screenclear.
            inputTimes++;
        }
        // Set coordinate String to empty for next
        coords = "";
        // Decrease amount how many moves aka spaces are left on the gameboard
        movesLeft--;
    }

    private static void printCoordsInfo()
    {
        System.out.println(coordsInfoMsg);
        System.out.println(askCoordsMsg);
    }

    public static boolean checkEndConditions(int tokensNeedToWin)
    {
        int lengthToXWall = 0;
        int lengthToYWall = 0;
        int sameTokens = 1;

        // Check is there enough space for tokens for victory
        for(int y = 0; y < gameBoard.length; y++)
        {
            for(int x = 0; x < gameBoard[y].length; x++)
            {
                // Checking horizontal tokens
                lengthToXWall = gameBoard.length - x;
                if(lengthToXWall >= tokensNeedToWin && gameBoard[y][x] != emptySpace)
                {
                    for(int i = 1; i < tokensNeedToWin; i++)
                    {
                        // Token is different than next token
                        if(gameBoard[y][x] != gameBoard[y][x + i])
                        {
                            sameTokens = 1;
                            break;
                        }
                        // Token is the same than the next one
                        else if(gameBoard[y][x] == gameBoard[y][x + i])
                        {
                            sameTokens++;
                            // The right amount of tokens next to each other to the victory
                            if(sameTokens == tokensNeedToWin)
                            {
                                checkWinner(y, x);
                                return true;
                            }
                        }
                    }
                }
                // End of horizontal check

                // Checking vertical tokens
                lengthToYWall = gameBoard.length - y;
                if(lengthToYWall >= tokensNeedToWin && gameBoard[y][x] != emptySpace)
                {
                    for(int i = 1; i < tokensNeedToWin; i++)
                    {
                        // Token is different than token underneath
                        if(gameBoard[y][x] != gameBoard[y + i][x])
                        {
                            sameTokens = 1;
                            break;
                        }
                        // Token is the same than the one underneath
                        else if(gameBoard[y][x] == gameBoard[y + i][x])
                        {
                            sameTokens++;
                            // The right amount of tokens vertically to each other to the victory
                            if(sameTokens == tokensNeedToWin)
                            {
                                checkWinner(y, x);
                                return true;
                            }
                        }
                    }
                }
                // End of vertical check

                // Checking \ diagonal tokens
                lengthToXWall = gameBoard.length - x;
                lengthToYWall = gameBoard.length - y;
                if(lengthToXWall >= tokensNeedToWin && lengthToYWall >= tokensNeedToWin && gameBoard[y][x] != emptySpace)
                {
                    for(int i = 1; i < tokensNeedToWin; i++)
                    {
                        // Token is different than token \ diagonally
                        if(gameBoard[y][x] != gameBoard[y + i][x + i])
                        {
                            sameTokens = 1;
                            break;
                        }
                        // Token is the same than the one \ diagonally
                        else if(gameBoard[y][x] == gameBoard[y + i][x + i])
                        {
                            sameTokens++;
                            // The right amount of tokens diagonally to each other to the victory
                            if(sameTokens == tokensNeedToWin)
                            {
                                checkWinner(y, x);
                                return true;
                            }
                        }
                    }
                }
                // End of \ diagonal check

                // Checking / diagonal tokens
                lengthToXWall = x + 1;
                lengthToYWall = gameBoard.length - y;
                if(lengthToXWall >= tokensNeedToWin && lengthToYWall >= tokensNeedToWin && gameBoard[y][x] != emptySpace)
                {
                    for(int i = 1; i < tokensNeedToWin; i++)
                    {
                        // Token is different than token / diagonally
                        if(gameBoard[y][x] != gameBoard[y + i][x - i])
                        {
                            sameTokens = 1;
                            break;
                        }
                        // Token is the same than the one / diagonally
                        else if(gameBoard[y][x] == gameBoard[y + i][x - i])
                        {
                            sameTokens++;
                            // The right amount of tokens diagonally to each other to the victory
                            if(sameTokens == tokensNeedToWin)
                            {
                                checkWinner(y, x);
                                return true;
                            }
                        }
                    }
                }
                // End of \ diagonal check
            }
        }

        // Check are there any moves left, if not end the game
        if(movesLeft <= 0)
        {
            return true;
        }

        return false;
    }

    private static void checkWinner(int y, int x)
    {
        char victoryToken = ' ';

        // Check which token was on the winners spot where winner check was made
        victoryToken = gameBoard[y][x];
        if(victoryToken == playerToken)
        {
            playerWon = true;
        }
        else
        {
            aiWon = true;
        }
    }

    public static void gameOver()
    {
        if(playerWon)
        {
            System.out.println(youWinMsg);
        }
        else if(aiWon)
        {
            System.out.println(youLoseMsg);
        }
        else
        {
            System.out.println(drawMsg);
        }
    }

    public static boolean checkIfRematch()
    {
        char yesNoInput = ' ';
        char yes = 'Y';
        char no = 'N';
        int tries = 0;
        String incorrectYesNoMsg = "Incorrect input detected! Use Y or N only please.";
        String playAgainMsg = "Do you want to play again? (Y)es or (N)o?";

        while(yesNoInput != yes && yesNoInput != no)
        {
            if(tries > 0)
            {
                clearScreen();
                printGraphics();
                System.out.println(incorrectYesNoMsg);
            }
            try
            {
                System.out.println(playAgainMsg);
                yesNoInput = input.nextLine().trim().toUpperCase().replaceAll("\\s","").charAt(0);
            }
            catch(Exception e)
            {
                // Don't crash if wrong input detected.
            }
            tries++;
        }

        if(yesNoInput == yes)
        {
            return true;
        }
        else if(yesNoInput == no)
        {
            return false;
        }
        else
        {
            printError();
            return false;
        }
    }

    // Debug methods
    // For faster debugging only
    public static void printError()
    {
        System.out.println("Uh oh, something went wrong. This should not have happened.");
    }

    public static void printSuccess()
    {
        System.out.println("It is working!");
    }
    // Debug methods end
}