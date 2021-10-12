import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


class Game {
    int rows = 6;
    int columns = 7;
    String[][] grid = new String[rows][columns];
    boolean game_over = false;
    int turn = 0;
    int difficulty;

    Game() {

        Scanner scan = new Scanner(System.in);
        System.out.print("pick a difficulty in the range of 1-3: ");
        difficulty = scan.nextInt();
        if (difficulty == 1) {
            System.out.println("You picked difficulty: Easy");
        } else if (difficulty == 2) {
            System.out.println("You picked difficulty: Normal");
        } else if (difficulty == 3) {
            System.out.println("You picked difficulty: Hard");
        }
        System.out.println();


        while (game_over != true) {

            if (turn == 0) {
                System.out.println();
                System.out.println();


                System.out.print("Player 1 pick a number between 0-6: ");
                int selection = scan.nextInt();

                if (selection >= columns) {                         //if human selects a column that doesn't exist, their turn gets skipped
                    System.out.println("Column doesn't exist.");
                    turn = turn + 2;
                } else {
                    if (valid_insert(grid, selection)) {
                        System.out.println();
                        System.out.println("Player 1 selected: " + selection);
                        int row = avaiable_row(grid, selection);
                        drop(grid, row, selection, "x");
                        if (find_win(grid, "x")) {
                            System.out.println();
                            System.out.println("Player 1 wins!");
                            game_over = true;
                        } else {
                            if (find_win(grid, "x") != true && find_tie(grid) == true) {
                                System.out.println();
                                System.out.println("Draw!");
                                game_over = true;
                            }
                        }


                    } else {          //if human selects a column that is full, their turn gets skipped

                        System.out.println();
                        System.out.println("Player 1 picked a full column!");
                        turn = turn + 2;


                    }
                }


            } else {
                int selection = 0;
                System.out.println();
                System.out.println();


                //selection = minimax_decision(grid, "o");
                selection = minimax(grid, difficulty, true)[0];

                if (valid_insert(grid, selection)) {
                    System.out.println();
                    System.out.println("AI selected: " + selection);
                    int row = avaiable_row(grid, selection);
                    drop(grid, row, selection, "o");

                    if (find_win(grid, "o")) {
                        System.out.println();
                        System.out.println("AI wins!");
                        game_over = true;
                    } else {
                        if (find_win(grid, "x") != true && find_tie(grid)) {
                            System.out.println();
                            System.out.println("Draw!");
                            game_over = true;
                        }
                    }


                }

            }

            turn = turn + 1;
            turn = turn % 2;
            print(flip(grid));
            System.out.println();
        }

    }

    /**
     * Checks if specified column is full
     *
     * @param arr the grid
     * @param col the column we want to check
     * @return
     */
    private boolean valid_insert(String[][] arr, int col) {

        if (arr[5][col] == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the next avaliable row in a specified column
     *
     * @param arr the grid
     * @param col the specified column
     * @return
     */
    private int avaiable_row(String[][] arr, int col) {
        int r = 0;
        int k = 0;

        while (k < rows)
            if (arr[k][col] == null) {
                r = k;
                break;
            } else {
                k = k + 1;
            }

        return r;
    }

    private void drop(String[][] arr, int row, int col, String piece) {
        arr[row][col] = piece;
    }

    /**
     * Prints out the board
     *
     * @param arr the grid in which the game takes place on
     */
    private void print(String[][] arr) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (arr[i][j] == null) {
                    if (j == 0) {
                        System.out.print("|");
                        System.out.print("   |");
                    } else {
                        System.out.print("   |");
                    }

                } else {
                    if (j == 0) {
                        System.out.print("|");
                        System.out.print(" " + arr[i][j] + " |");
                    } else {
                        System.out.print(" " + arr[i][j] + " |");
                    }
                }


            }
            System.out.println();
        }
    }


    /**
     * Flips the entire grid horizontally and returns the grid in a desired orientation
     *
     * @param arr The grid
     */
    private String[][] flip(String[][] arr) {
        String[][] temp = new String[rows][columns];
        int k = 5;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                temp[k][j] = arr[i][j];
            }
            if (k > 0) {
                k = k - 1;
            }
        }

        return temp;
    }


    /**
     * Determines if the game is a tie
     *
     * @return false or true
     */
    private boolean find_tie(String[][] arr) {

        int total = 0;
        int slots = 42;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (arr[i][j] != null) {
                    total = total + 1;
                }

            }
        }

        if (total == 42 && game_over != true) {
            return true;
        }

        return false;
    }


    /**
     * Finds for a win in all possible directions
     *
     * @param arr   grid array that represents the board
     * @param piece the Strings used to represent playr 1 and player 2
     * @return
     */
    private boolean find_win(String[][] arr, String piece) {


        //horizontal search
        for (int i = 0; i < columns - 3; i++) {
            for (int j = 0; j < rows; j++) {
                if (arr[j][i] == piece && arr[j][i + 1] == piece && arr[j][i + 2] == piece && arr[j][i + 3] == piece) {
                    return true;
                }
            }

        }

        //vertical search
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows - 3; j++) {
                if (arr[j][i] == piece && arr[j + 1][i] == piece && arr[j + 2][i] == piece && arr[j + 3][i] == piece) {
                    return true;
                }
            }

        }

        //positive diagonal search
        for (int i = 0; i < columns - 3; i++) {
            for (int j = 0; j < rows - 3; j++) {
                if (arr[j][i] == piece && arr[j + 1][i + 1] == piece && arr[j + 2][i + 2] == piece && arr[j + 3][i + 3] == piece) {
                    return true;
                }
            }

        }

        //negative diagonal search
        for (int i = 0; i < columns - 3; i++) {
            for (int j = 3; j < rows; j++) {
                if (arr[j][i] == piece && arr[j - 1][i + 1] == piece && arr[j - 2][i + 2] == piece && arr[j - 3][i + 3] == piece) {
                    return true;
                }
            }

        }

        return false;
    }


    /**
     * This method returns a score that represents the probability of a potential win by looking at all different possibilities and assigning them a score
     *
     * @param arr   the grid we want to evaluation
     * @param piece the player we want to evaluate
     * @return
     */
    private int score(String[][] arr, String piece) {
        int total = 0;


        //horizontal scoring
        for (int i = 0; i < columns - 3; i++) {
            for (int j = 0; j < rows; j++) {
                if (arr[j][i] == piece && arr[j][i + 1] == piece && arr[j][i + 2] == piece && arr[j][i + 3] == piece) {
                    total = total + (int) Float.POSITIVE_INFINITY;
                } else if (arr[j][i] == piece && arr[j][i + 1] == piece && arr[j][i + 2] == piece && arr[j][i + 3] == null) {
                    total = total + 150;
                } else if (arr[j][i] == null && arr[j][i + 1] == piece && arr[j][i + 2] == piece && arr[j][i + 3] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j][i + 1] == null && arr[j][i + 2] == piece && arr[j][i + 3] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j][i + 1] == piece && arr[j][i + 2] == null && arr[j][i + 3] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j][i + 1] == piece && arr[j][i + 2] == null && arr[j][i + 3] == null) {
                    total = total + 50;
                } else if (arr[j][i] == null && arr[j][i + 1] == null && arr[j][i + 2] == piece && arr[j][i + 3] == piece) {
                    total = total + 50;
                } else if (arr[j][i] == piece && arr[j][i + 1] == null && arr[j][i + 2] == null && arr[j][i + 3] == piece) {
                    total = total + 50;
                } else if (arr[j][i] == null && arr[j][i + 1] == piece && arr[j][i + 2] == piece && arr[j][i + 3] == null) {
                    total = total + 50;
                } else if (arr[j][i] == piece && arr[j][i + 1] == null && arr[j][i + 2] == null && arr[j][i + 3] == null) {
                    total = total + 1;
                } else if (arr[j][i] == null && arr[j][i + 1] == null && arr[j][i + 2] == null && arr[j][i + 3] == piece) {
                    total = total + 1;
                } else if (arr[j][i] == null && arr[j][i + 1] == null && arr[j][i + 2] == piece && arr[j][i + 3] == null) {
                    total = total + 1;
                } else if (arr[j][i] == null && arr[j][i + 1] == piece && arr[j][i + 2] == null && arr[j][i + 3] == null) {
                    total = total + 1;
                }
            }

        }

        //vertical scoring
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows - 3; j++) {
                if (arr[j][i] == piece && arr[j + 1][i] == piece && arr[j + 2][i] == piece && arr[j + 3][i] == piece) {
                    total = total + (int) Float.POSITIVE_INFINITY;
                } else if (arr[j][i] == piece && arr[j + 1][i] == piece && arr[j + 2][i] == piece && arr[j + 3][i] == null) {
                    total = total + 150;
                } else if (arr[j][i] == null && arr[j + 1][i] == piece && arr[j + 2][i] == piece && arr[j + 3][i] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j + 1][i] == null && arr[j + 2][i] == piece && arr[j + 3][i] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j + 1][i] == piece && arr[j + 2][i] == null && arr[j + 3][i] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j + 1][i] == piece && arr[j + 2][i] == null && arr[j + 3][i] == null) {
                    total = total + 50;
                } else if (arr[j][i] == null && arr[j + 1][i] == null && arr[j + 2][i] == piece && arr[j + 3][i] == piece) {
                    total = total + 50;
                } else if (arr[j][i] == piece && arr[j + 1][i] == null && arr[j + 2][i] == null && arr[j + 3][i] == piece) {
                    total = total + 50;
                } else if (arr[j][i] == null && arr[j + 1][i] == piece && arr[j + 2][i] == piece && arr[j + 3][i] == null) {
                    total = total + 50;
                } else if (arr[j][i] == piece && arr[j + 1][i] == null && arr[j + 2][i] == null && arr[j + 3][i] == null) {
                    total = total + 15;
                } else if (arr[j][i] == null && arr[j + 1][i] == null && arr[j + 2][i] == null && arr[j + 3][i] == piece) {
                    total = total + 15;
                } else if (arr[j][i] == null && arr[j + 1][i] == null && arr[j + 2][i] == piece && arr[j + 3][i] == null) {
                    total = total + 15;
                } else if (arr[j][i] == null && arr[j + 1][i] == piece && arr[j + 2][i] == null && arr[j + 3][i] == null) {
                    total = total + 15;
                }


            }

        }

        //positive diagonal scoring
        for (int i = 0; i < columns - 3; i++) {
            for (int j = 0; j < rows - 3; j++) {
                if (arr[j][i] == piece && arr[j + 1][i + 1] == piece && arr[j + 2][i + 2] == piece && arr[j + 3][i + 3] == piece) {
                    total = total + (int)Float.POSITIVE_INFINITY;
                } else if (arr[j][i] == piece && arr[j + 1][i + 1] == piece && arr[j + 2][i + 2] == piece && arr[j + 3][i + 3] == null) {
                    total = total + 150;
                } else if (arr[j][i] == null && arr[j + 1][i + 1] == piece && arr[j + 2][i + 2] == piece && arr[j + 3][i + 3] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j + 1][i + 1] == null && arr[j + 2][i + 2] == piece && arr[j + 3][i + 3] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j + 1][i + 1] == piece && arr[j + 2][i + 2] == null && arr[j + 3][i + 3] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j + 1][i + 1] == piece && arr[j + 2][i + 2] == null && arr[j + 3][i + 3] == null) {
                    total = total + 50;
                } else if (arr[j][i] == null && arr[j + 1][i + 1] == null && arr[j + 2][i + 2] == piece && arr[j + 3][i + 3] == piece) {
                    total = total + 50;
                } else if (arr[j][i] == piece && arr[j + 1][i + 1] == null && arr[j + 2][i + 2] == null && arr[j + 3][i + 3] == piece) {
                    total = total + 50;
                } else if (arr[j][i] == null && arr[j + 1][i + 1] == piece && arr[j + 2][i + 2] == piece && arr[j + 3][i + 3] == null) {
                    total = total + 50;
                } else if (arr[j][i] == piece && arr[j + 1][i + 1] == null && arr[j + 2][i + 2] == null && arr[j + 3][i + 3] == null) {
                    total = total + 1;
                } else if (arr[j][i] == null && arr[j + 1][i + 1] == null && arr[j + 2][i + 2] == null && arr[j + 3][i + 3] == piece) {
                    total = total + 1;
                } else if (arr[j][i] == null && arr[j + 1][i + 1] == piece && arr[j + 2][i + 2] == null && arr[j + 3][i + 3] == null) {
                    total = total + 1;
                } else if (arr[j][i] == null && arr[j + 1][i + 1] == null && arr[j + 2][i + 2] == piece && arr[j + 3][i + 3] == null) {
                    total = total + 1;
                }
            }

        }

        //negative diagonal scoring
        for (int i = 0; i < columns - 3; i++) {
            for (int j = 3; j < rows; j++) {
                if (arr[j][i] == piece && arr[j - 1][i + 1] == piece && arr[j - 2][i + 2] == piece && arr[j - 3][i + 3] == piece) {
                    total = total + (int)Float.POSITIVE_INFINITY;
                } else if (arr[j][i] == piece && arr[j - 1][i + 1] == piece && arr[j - 2][i + 2] == piece && arr[j - 3][i + 3] == null) {
                    total = total + 150;
                } else if (arr[j][i] == null && arr[j - 1][i + 1] == piece && arr[j - 2][i + 2] == piece && arr[j - 3][i + 3] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j - 1][i + 1] == null && arr[j - 2][i + 2] == piece && arr[j - 3][i + 3] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j - 1][i + 1] == piece && arr[j - 2][i + 2] == null && arr[j - 3][i + 3] == piece) {
                    total = total + 150;
                } else if (arr[j][i] == piece && arr[j - 1][i + 1] == piece && arr[j - 2][i + 2] == null && arr[j - 3][i + 3] == null) {
                    total = total + 50;
                } else if (arr[j][i] == null && arr[j - 1][i + 1] == null && arr[j - 2][i + 2] == piece && arr[j - 3][i + 3] == piece) {
                    total = total + 50;
                } else if (arr[j][i] == piece && arr[j - 1][i + 1] == null && arr[j - 2][i + 2] == null && arr[j - 3][i + 3] == piece) {
                    total = total + 50;
                } else if (arr[j][i] == null && arr[j - 1][i + 1] == piece && arr[j - 2][i + 2] == piece && arr[j - 3][i + 3] == null) {
                    total = total + 50;
                } else if (arr[j][i] == piece && arr[j - 1][i + 1] == null && arr[j - 2][i + 2] == null && arr[j - 3][i + 3] == null) {
                    total = total + 1;
                } else if (arr[j][i] == null && arr[j - 1][i + 1] == null && arr[j - 2][i + 2] == null && arr[j - 3][i + 3] == piece) {
                    total = total + 1;
                } else if (arr[j][i] ==null && arr[j - 1][i + 1] == piece && arr[j - 2][i + 2] == null && arr[j - 3][i + 3] == null) {
                    total = total + 1;
                } else if (arr[j][i] == null && arr[j - 1][i + 1] == null && arr[j - 2][i + 2] == piece && arr[j - 3][i + 3] == null) {
                    total = total + 1;
                }
            }

        }


        return total;

    }


    /**
     * This evaluation function determines which player has the best chance of winning by calculating the difference of the total amount of possible wins: This evaluation function was taken from: https://stackoverflow.com/questions/10985000/how-should-i-design-a-good-evaluation-function-for-connect-4
     *
     * @param symbol1 the maximizing player
     * @param symbol2 the minimizing player
     * @param arr     the grid we want to evaluate
     * @return
     */
    private int evaluation(String symbol1, String symbol2, String[][] arr) {
        return (100) * (score(arr, symbol1) - score(arr, symbol2));
    }


    /**
     * returns a copy of a specified array
     *
     * @param arr the array we want ot copy
     * @return the copy
     */
    private String[][] copyOf(String[][] arr) {
        String[][] temp = new String[columns + 1][rows + 1];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                temp[i][j] = arr[i][j];
            }

        }
        return temp;

    }

    /**
     * Returns an array of avaliable positions the AI can take
     *
     * @param arr the grid
     * @return array of avaliable positions
     */
    private int[] potential_moves(String[][] arr) {
        int[] cols = new int[columns];
        for (int i = 0; i < columns; i++) {
            if (valid_insert(arr, i) == true) {
                cols[i] = i;
            }
        }
        return cols;
    }

    /**
     * checks for win or tie to determine if the game is finished
     *
     * @param arr   the grid
     * @param piece the piece of the player
     * @return false or ture
     */
    private boolean terminal_test(String[][] arr, String piece) {
        if (find_win(arr, piece)) {
            return true;
        }
        return false;
    }

    /**
     * Minimax implementation based of pseudocode taken from: https://en.wikipedia.org/wiki/Minimax
     *
     * @param arr              the grid
     * @param depth            the difficulty, the human player wishes to play at
     * @param maximizingPlayer
     * @return array containing the column to insert and value of the evaluation
     */
    private int[] minimax(String[][] arr, int depth, boolean maximizingPlayer) {
        int[] returns = new int[2];
        int score;
        String[][] temp;
        float value;
        int insert = 0;


        if (depth == 0 || terminal_test(arr, "o")) {
            if (terminal_test(arr, "o")) {
                if (find_win(arr, "o")) {

                    returns[1] = 1000000000;
                    return returns;
                } else if (find_win(arr, "x")) {
                    returns[1] = -1000000000;
                    return returns;
                } else {                                //if there is a tie
                    returns[1] = 0;
                    return returns;
                }
            } else {
                returns[1] = evaluation( "o","x",arr);
                return returns;
            }
        }

        if (maximizingPlayer) {
            value = Float.NEGATIVE_INFINITY;
            int[] validColumns = potential_moves(arr);
            for (int col : validColumns) {
                int row = avaiable_row(arr, col);
                temp = copyOf(arr);           ////////might be an issue. Oh well, we will see!
                drop(temp, row, col, "o");
                score = minimax(temp, depth - 1, false)[1];
                if (score > value) {
                    value = score;
                    insert = col;
                }
            }

            returns[0] = insert;
            returns[1] = (int)value;
            return returns;


        } else {
            value = Float.POSITIVE_INFINITY;
            int[] validColumns = potential_moves(arr);
            for (int col : validColumns) {
                int row = avaiable_row(arr, col);
                temp = copyOf(arr);           ////////might be an issue. Oh well, we will see!
                drop(temp, row, col, "X");
                score = minimax(temp, depth - 1, true)[1];
                if (score < value) {
                    value = score;
                    insert = col;
                }

            }

            returns[0] = insert;
            returns[1] = (int) value;
            return returns;

        }
    }


    public static void main(String[] args) {
        new Game();
    }
}
