package part7;

import java.util.Scanner;
import java.util.Random;
 
//main class that runs the game
class Game {

    //private instance variables
    private Board board;
    private Player player1;
    private Player player2;
    //adding private variables for the size and the symbol counter for the win checker
    private int size;
    private int symbolCounter;


    public static void main(String[] args){

        //this creates a new game instance and then starts the game
        Game game = new Game(); 
        game.start();

    }

    //the constructor for the Game class, initializing the game board
    public Game(){

        Scanner input = new Scanner(System.in);

        System.out.println("Enter the grid size you want to play on.");
        size = input.nextInt();

        System.out.println("Enter the winning length (original game used 3)");
        symbolCounter = input.nextInt();

        this.board = new Board(size);

        input.close();
    }
    
    //public method to start the game
    public void start(){
    
    Scanner input = new Scanner(System.in);
    
    int restart;
    System.out.println("Tic-Tac-Toe");
        //based on the mode that is selected, start the game, then start the game loop
        
        while(true){
        //starts the game by setting up the players and starts the main game loop
        setupPlayers();
        play();

        System.out.println("Would you like to restart? 1 = yes 2 = no");
        restart = input.nextInt();

        //if the answer is no then it will break the loop and end the game
        if(restart == 2){
            break;
        }

        //starting a new board for a new game
        this.board = new Board(size);
        }

        input.close();

    }

    private void setupPlayers(){

        int choice;

        //getting the users choice from scanner
        Scanner input = new Scanner(System.in);
        System.out.println("Choose game mode:");
        System.out.println("1. Human vs Human");
        System.out.println("2. Human vs Computer");
        System.out.println("3. Computer vs Computer");

        choice = input.nextInt();

        //initialize the players based on the choice of the user
        //this uses a switch and case statement
        switch (choice) {
            case 1 -> {
                //player one and two wtih x and o
                player1 = new HumanPlayer('X');
                player2 = new HumanPlayer('O'); 
            }
            case 2 -> {
                //human plays against a computer
                player1 = new HumanPlayer('X');
                player2 = new ComputerPlayer('O'); 
            }
            case 3 -> {
                //computer plays against a computer
                player1 = new ComputerPlayer('X'); 
                player2 = new ComputerPlayer('O'); 
            }
            default -> {
                //if a choice that was invalid was made, then it just makes the game a human vs human
                System.out.println("Invalid choice. Human vs Human selected.");
                player1 = new HumanPlayer('X');
                player2 = new HumanPlayer('O');
            }
        }
    
        input.close();

    }

    private void play(){

        //player 1 starts with a game
        Player currentPlayer = player1;

        while(true){
            //updates the current board
            //then the current player makes a move
            board.display();
            System.out.println(currentPlayer.getSymbol() + "'s turn");
            currentPlayer.makeMove(board);

            //if the current player has won display that
            if(board.checkWin(currentPlayer.getSymbol(), symbolCounter)){
                board.display();
                System.out.println(currentPlayer.getSymbol() + " wins!");
                //end the game right after
                break;
            }

            //this conditional just checks if the game was a draw.
            else if(board.isFull()){
                board.display();
                System.out.println("It is a draw!");
                break;
            }

            //switches to the other player
            if (currentPlayer == player1){
                currentPlayer = player2;
            } 
            else{
                currentPlayer = player1;
            }
        }
    }
}

//this class is for the player
class Player{

    //this is for the symbol either x or o
    protected char symbol; 

    //this is the constructor for this class, to initialize the symbol
    public Player(char symbol){
        this.symbol = symbol;
    }

    //getter for the symbol
    public char getSymbol(){
        return symbol;
    }

    //public method to make a move, this will be overridden
    public void makeMove(Board board){
    }
}

//this is a class for a human player
class HumanPlayer extends Player{

    //this is just the constructor for this class, reference to the super class
    public HumanPlayer(char symbol){
        super(symbol);
    }   

    public void makeMove(Board board){
        
        int row;
        int col;

        Scanner input = new Scanner(System.in);

        do{
            System.out.println("Enter the Row and Column you wish to make your move in (row col): ");
            row = input.nextInt() - 1;
            col = input.nextInt() - 1;
        }
        while(!board.placeMarker(row, col, symbol));

        input.close();

    }
}

//class for computer players
class ComputerPlayer extends Player{

    //this is a variable to hold the random generator that makes moves
    private Random random;

    public ComputerPlayer(char symbol){

        super(symbol);
        random = new Random();

    }

    public void makeMove(Board board){

        int row;
        int col;

        do{
            row = random.nextInt(3);
            col = random.nextInt(3);
        }
        while(!board.placeMarker(row, col, symbol));
        System.out.println("Computer made a move at (" + (row + 1) + ", " + (col + 1) + ")");
    }
}

//this class makes the board
class Board{

    //a 2d array for the 3 by 3 grid
    private char[][] grid;
    //for the size of the grid
    private int size;

    //initializes an empty board
    public Board(int size){

        this.size = size;

        grid = new char[size][size];

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                //to make the board empty it fills it with spaces
                grid[i][j] = ' '; 
            }
        }
    }

    //this method displays the current board, it basically updates it
    public void display(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){

                System.out.print(" " + grid[i][j] + " ");

                if(j < size - 1){
                    System.out.print("|");
                } 

            }

            System.out.println();

            if(i < size - 1){

                for(int j = 0; j <= size - 1; j++){
                    System.out.print("--- ");
                }

                System.out.println();

            }

        }
    }

    //this method places the marker
    public boolean placeMarker(int row, int col, char symbol) {
        //this checks if the position is able to be played, empty or valid
        if(row < 0 || row >= size || col < 0 || col >= size || grid[row][col] != ' ') {
            System.out.println("Invalid move. Try again.");
            return false;
        }
        //places a symbol
        grid[row][col] = symbol; 
        return true;
    }

    //this method checks if a player has won or not
    public boolean checkWin(char symbol, int symbolCounter){

        //checks all cases for a win
        //this is rows, for horizontal, vertical and diagonal
        for(int i = 0; i < symbolCounter; i++){
            for(int j = 0; j <= size - symbolCounter; j++){
                boolean win = true;
                for(int k = 0; k < symbolCounter; k++){
                    if(grid[i][j + k] != symbol){
                        win = false;
                        break;
                    }
                }
                if(win){
                    return true;
                } 
            }
        }
        //Vertical check
        for(int i = 0; i <= size - symbolCounter; i++){
            for (int j = 0; j < size; j++) {
                boolean win = true;
                for (int k = 0; k < symbolCounter; k++) {
                    if (grid[i + k][j] != symbol) {
                        win = false;
                        break;
                    }
                }
                if(win){
                    return true;
                } 
            }
        }

        //Diagonal check left to right
        for (int i = 0; i <= size - symbolCounter; i++) {
            for (int j = 0; j <= size - symbolCounter; j++) {
                boolean win = true;
                for (int k = 0; k < symbolCounter; k++) {
                    if (grid[i + k][j + k] != symbol) {
                        win = false;
                        break;
                    }
                }
                if(win){
                    return true;
                }
            }
        }

        //Diagonal check right to left
        for(int i = 0; i <= size - symbolCounter; i++){
            for (int j = symbolCounter - 1; j < size; j++) {
                boolean win = true;
                for (int k = 0; k < symbolCounter; k++) {
                    if (grid[i + k][j - k] != symbol) {
                        win = false;
                        break;
                    }
                }
                if(win){
                    return true;
                } 
            }
        }

        return false;
    }

    //this method checks if the board is full or not, in which case it calls for a tie 
    public boolean isFull(){
        //this for loop checks for any empty space
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                if(grid[row][col] == ' '){
                    return false;
                }
            }
        }

        return true;

    }
}