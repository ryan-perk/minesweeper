import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Scanner;

/** @author ryanperkins */

public class Minesweeper {
	
	private static int numMines, row, col, round;
	private static final int MAX = 10, MIN = 0;
	private static boolean gameInProg;
	private static File file;
	private static String[][] board;
	private static boolean[][] mines;
	
	/** 
	 * constructs a game of size r x c from the users input
	 * 
	 * @param r - # of rows 
	 * @param c - # of columns
	 */
	public Minesweeper(int r, int c) {
		row = r; col = c;
		
		// makes sure the board is at most 10x10
		if(row > MIN && row <= MAX && col > MIN && col <= MAX) {
			board = new String[r][c];
			mines = new boolean[r][c];
			numMines = (row * col) / 3;
			
			// makes game board
			for(int y = 0; y < row; y++) {
				for(int x = 0; x < col; x++) {
					board[y][x] = " -  "; 
				}
			}
			
			// makes mine field
			while(numMines > 0) {
				int mineR = (int)(Math.random() * row), mineC = (int)(Math.random() * col);
				
				if(numMines > 0 && !mines[mineR][mineC]) {
					mines[mineR][mineC] = true;
					numMines--;
				}
			
				else
					mines[mineR][mineC] = false;
			}
			// resets mines
			numMines = ((row * col) / 3);
		}
		
		// 
		else
			message('f', 3);
	}
    
	/**
	 * constructs a game from a seed text file 
	 * 
	 * @param file - text file formatted to give size, # mines, and mine locations 
	 */
	public Minesweeper(File file) {
		int lineNum = 0;
		
		// attempts to read the file; otherwise exception is caught
		try(BufferedReader in = new BufferedReader(new FileReader(file))) {
			
			// reads the file
			while(in.ready()) {
				String line = in.readLine();
				String[] tokens = line.split("[ ]+");
				
				// if valid first line, board is made
				if(lineNum == 0 && tokens.length == 2 && isInt(tokens[0]) && isInt(tokens[1])) {
					row = Integer.valueOf(tokens[0]);
					col = Integer.valueOf(tokens[1]);
					
					if(row > MAX || row <= MIN || col > MAX || col <= MIN)
							message('f', 4);
					
					board = new String[row][col];
					mines = new boolean[row][col];
					
					for(int y = 0; y < row; y++) {
						for(int x = 0; x < col; x++) {
							board[y][x] = " -  ";
						}
					} 
					
					lineNum++;
				}
				
				// if valid second line, # of mines is created
				else if(lineNum == 1 && tokens.length == 1 && isInt(tokens[0])) {
					numMines = Integer.valueOf(tokens[0]);
					lineNum++;
				}
				
				// if # of mines = # of coordinates, mines are placed
				else if(lineNum < numMines + 2 && tokens.length == 2 && isInt(tokens[0]) && isInt(tokens[1])) {
					if(isInBounds(Integer.valueOf(tokens[0]),(Integer.valueOf(tokens[1]))))
						mines[Integer.valueOf(tokens[0])][Integer.valueOf(tokens[1])] = true;
					else 
						message('f', 0);
					
					lineNum++;
				} 
				
				// invalid file
				else {
					message('f', 2);
				}
				
			}
			
			// if not enough mines, invalid file
			if(numMines != lineNum - 2) {
				message('f', 1);
			}
			
		} catch(IOException e) { }
	} 
	
	/**
	 * prints the various game messages
	 * 
	 * @param mes - requested message
	 * @param ver - if multiple versions of the message
	 */
	private void message(char mes, int ver) {
		
		// coordinate has already been selected
		if(mes == 'a')
			System.out.println("\nYou have already selected that coordinate.");
		
		// beginning of the game
		else if(mes == 'b')
			System.out.println("        _\n"
					+ "  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __\n"
					+ " /    \\| | \'_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ \'_ \\ / _ \\ \'__|\n"
					+ "/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |\n"
					+ "\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|\n"
					+ "                                     ALPHA |_| EDITION");
		
		// invalid coordinates 
		else if(mes == 'c')
			System.out.println("\nInvalid coordinates (Row: 0-" + (row - 1) 
					+ " Col: 0-" + (col - 1) + ")");
		
		// game over visual  
		else if(mes == 'e')
			System.out.println("Oh no... You revealed a mine!\n"
					+ "  __ _  __ _ _ __ ___   ___    _____   _____ _ __ \n"
					+ " / _` |/ _` | \'_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ \'__|\n"
					+ "| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |\n"
					+ " \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|\n"
					+ " |___/\n");
		
		// invalid file
		else if(mes == 'f') {
			if(ver == 0)
				System.out.println("Cannot create game with " + file.getName() + ", because it is not formatted correctly (mines out of bounds)");
				System.out.println("Cannot create game with " + file.getName() + ", because it is not forma
                                   else if(ver == 1)
                                   System.out.println("Cannot create game with " + file.getName() + ", because it is not formatted correctly (not enough mines)");
                                   else if(ver == 2)tted correctly (too many mines or invalid format)");
			else if(ver == 3)
				System.out.println("Cannot create game with " + row + " rows & " + col + " cols because it is too large (10x10 max)");
			else if(ver == 4)
				System.out.println("Cannot create game with " + file.getName() + " cols because it is too large (invalid board size)");
			System.exit(0);
				
		}
		
		// help
		else if(mes == 'h')
			System.out.println("Avaiable Commands:\n"
					+ " Reveal:  r/reveal     row col\n"
					+ " Mark:    m/mark       row col\n"
					+ " Guess:   g/guess      row col\n"
					+ " Help:    h/help\n"
					+ " Quit:    q/quit\n");
		
		// invalid command
		else if(mes == 'i')
			System.out.println("\nInvalid command. Type help or h for help.");
		
		
		// quit
		else if(mes == 'q')
			System.out.println("\nY U NO PLAY MORE?\nBye!");
		
		// round #
		else if(mes == 'r')
			System.out.println("\nRound: " + round + "\n");	
		
		// score
		else if(mes == 's')
			System.out.println("\nCONGRATULATIONS!\nYOU HAVE WON!\nSCORE: " + ((row * col) - numMines - round));
	}
	
	/**
	 * runs the game
	 */
    private void run() {
       	gameInProg = true;
		Scanner sc = new Scanner(System.in);
		message('b', 0);
    	
		// game loop
    	while(gameInProg) {
    		printBoard();
    		round++;
    		String input = sc.nextLine().trim();
    		String[] tokens = input.split(" +");
    			
    		if(tokens.length == 3) {
    			
    			// calls reveal command
    			if(tokens[0].equalsIgnoreCase("reveal") || tokens[0].equalsIgnoreCase("r") && isInt(tokens[1]) && isInt(tokens[2])) 
    				reveal(Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2]));
    		
    			// calls mark command
    			else if(tokens[0].equalsIgnoreCase("mark") || tokens[0].equalsIgnoreCase("m") && isInt(tokens[1]) && isInt(tokens[2])) 
    				mark(Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2]));
    		
    			// calls guess command
    			else if(tokens[0].equalsIgnoreCase("guess") || tokens[0].equalsIgnoreCase("g") && isInt(tokens[1]) && isInt(tokens[2]))
    				guess(Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2]));
    			
    			// invalid command
    			else
    				message('i', 0);
    		}
    		
    		// quits
    		else if(input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
    			gameInProg = false;
    			message('q', 0);
    		}
    		
    		// help
    		else if(input.equalsIgnoreCase("help") || input.equalsIgnoreCase("h"))
    			message('h', 0);
    		
    		// invalid command
    		else
        		message('i', 0);
    		
    		// checks to see if there is a winner
    		if(winner() == 0) {
    			printBoard();
    			message('s', 0);
    			gameInProg = false;
    		}
    		
    	}
    	
    	// closes scanner
    	sc.close();
    }
    
    /**
     * reveal command
     * 
     * @param r - y coordinate to be revealed
     * @param c - x coordinate to be revealed 
     */
    public void reveal(int r, int c) {
    	
    	// checks if there is a mine
    	if(mines[r][c]) {
    		gameInProg = false;
    		message('e', 0);
    	}
    	
    	// reveals index 
    	else if(board[r][c].equals(" -  ") || board[r][c].equals(" F  ") || board[r][c].equals(" ?  ")) {
    		if(getAdjMines(r, c) == 0)
    			board[r][c] = "    ";
    		else
    			board[r][c] = " " + getAdjMines(r, c) + "  ";
    	}
    	
    	// invalid coordinates
    	else
    		message('a', 0);
    }
    
    /**
     * mark command
     * 
     * @param r - y coordinate to be marked
     * @param c - x coordinate to be marked
     */
    public void mark(int r, int c) {
    	if(board[r][c].equals(" -  ") || board[r][c].equals(" ?  "))
    		board[r][c] = " F  ";
    	else if(board[r][c].equals(" F  "))
    		board[r][c] = " -  ";
    	else 
    		message('a', 0);
    }
 
    /**
     * guess command
     * 
     * @param r - y coordinate to be guessed 
     * @param c - x coordinate to be guessed
     */
    public void guess(int r, int c) {
    	if(board[r][c].equals(" -  ") || board[r][c].equals(" F  "))
    		board[r][c] = " ?  ";
    	else if(board[r][c].equals(" ?  "))
    		board[r][c] = " -  ";
    	else 
    		message('a', 0);
    }
    
    /**
     * checks to see if there is a winner
     * 
     * @return - returns the number of spaces not revealed yet
     */
    private int winner() {
    	int indexLeft = 0;
    	
    	// counts number of spaces not revealed that are not mines
    	for(int y = 0; y < row; y++) {
    		for(int x = 0; x < col; x++) {
    			if(mines[y][x] && !board[y][x].equals(" F  "))
    				indexLeft++;
    			if(!mines[y][x] && (board[y][x].equals(" -  ") || board[y][x].equals(" ?  "))) {
    				indexLeft++;
    			}
    		}
    	}
   
    	return indexLeft;
    }
    
    /** 
     * checks if the given string contains an integer 
     * 
     * @param s - string to check 
     * @return - whether of not the string is an integer
     */
    private boolean isInt(String s) {
        boolean isInt = false;
        
        // tries to find integer and catches exception if it is not
        try {
        	Integer.parseInt(s);
        	isInt = true;
        } catch(NumberFormatException ex) { }
   
        return isInt;
     }
    
    /**
     * checks to see if the coordinates are in bounds
     * 
     * @param row - y coordinate
     * @param col - x coordinate
     * @return - whether or not the coordinates are in bounds
     */
    private boolean isInBounds(int r, int c) {
    	if((r > row - 1 || r < 0) || (c > col - 1 || c < 0))
    		return false;
    	return true;
    }
    
    /**
     * counts how many surrounding mines there are to a revealed space
     * 
     * @param row - y coordinate
     * @param col - x coordinate
     * @return - # adjacent mines
     */
    private int getAdjMines(int r, int c) {
    	int adjMines = 0;
    	
    	// top left
    	if(isInBounds(r - 1, c - 1) && mines[r - 1][c - 1])
    		adjMines++;
    	
    	// top middle
    	if(isInBounds(r - 1, c) && mines[r - 1][c]) 
    		adjMines++;
    	
    	// top right
   		if(isInBounds(r - 1, c + 1) && mines[r - 1][c + 1])
    		adjMines++;
   		
   		// left
   		if(isInBounds(r, c - 1) && mines[r][c - 1])
    		adjMines++;
   
   		// right
   		if(isInBounds(r, c + 1) && mines[r][c + 1])
   			adjMines++;
   	
   		// bottom left
   		if(isInBounds(r + 1, c - 1) && mines[r + 1][c - 1])
   			adjMines++;
   		
   		// bottom middle
    	if(isInBounds(r + 1, c) && mines[r + 1][c])
   			adjMines++;
    	
    	// bottom right
    	if(isInBounds(r + 1, c + 1) && mines[r + 1][c + 1])
   			adjMines++;
    	
    	return adjMines;
    }
         
    /**
     * prints the game board
     */
    private void printBoard() {
    	
    	// round number
    	message('r', 0);
    	
    	// prints board
    	for(int y = 0; y <= row; y++) {
    		for(int x = 0; x < col; x++) {
    			if(y == row)
    				System.out.print("   " + x);
    			else if(x == 0)
    				System.out.print(y + " " + board[y][x]);
    			else if(x == col - 1)
    				System.out.println(board[y][x]);
    			else 
    				System.out.print(board[y][x]);
    		} 
    	}
    	System.out.println("");
    }
      
    /**
     * main method
     * 
     * @param args - seed file or given board size
     */
    public static void main(String[] args) {
    	Minesweeper game = null;
    	
    	switch (args.length) {
    	case 2: 

	    int r, c;

	    // tries to make a custom game board or use a see file
	    try {
	    	r = Integer.parseInt(args[0]);
	    	c = Integer.parseInt(args[1]);
	    	game = new Minesweeper(r, c);	
	    	break;
	    } catch (NumberFormatException nfe) { }
	    
	    case 1: 
	    	String filename = args[0];
	    	file = new File(filename);
	    	if (file.isFile()) {
	    		game = new Minesweeper(file);
	    		break;
	    	}
	    	
	    	default:
	    		System.out.println("Usage: java Minesweeper [FILE]");
	    		System.out.println("Usage: java Minesweeper [ROWS] [COLS]");
	    		System.exit(0);
    	}
    	
    	game.run();
    }
}
