import java.util.Scanner;

public class Alice {
	
	public static char[][] board;
	public static final int MAX_DEPTH = 5;
	public static final int MAX_WIDTH = 7;
	public static final int MAX_HEIGHT = 8;
	public static final char SMALL_SAMURAI = 's';
	public static final char SMALL_NINJA = 'n';
	public static final char SAMURAI = 'S';
	public static final char NINJA = 'N';
	public static final char KING = 'K';
	public static int currentPlayer;
	public static int turnCounter;
	
	
	public static void gameLoop() {
		Scanner in = new Scanner(System.in);
		System.out.println("HI-YA! is a two player game developed for Sacramento States fall 2017 "
				+ "CSC 180 class.\n");
		System.out.println("Familiarize yourself with the game before playing.");
		System.out.println("Which player should go first? (1 = Human, 2 = Computer)");
		int initPlayer = in.nextInt();
		in.nextLine(); //clearBuffer
		init(initPlayer);
		String textboard = boardToString();
		System.out.println(textboard);
		boolean gameOn = true;
		int[][] translated = new int[2][2];
		while(gameOn) {
			turnCounter++;
			System.out.println("Turn " + turnCounter);
			String[] moveInput = new String[2];
			do {
				if(currentPlayer % 2 != 0) {
					System.out.print("Enter HUMAN move (initial destination): ");
					moveInput[0] = in.next();
					moveInput[1] = in.next();
					in.nextLine(); //buffer clear
				} else {
					moveInput = generateMove();
				}
				translated = translateMove(moveInput);
			} while(!checkIfLegal(translated[0][0], translated[0][1], translated[1][0], translated[1][1]));
			updateBoard(translated[0][0], translated[0][1], translated[1][0], translated[1][1]);
			gameOn = checkGameOver();
			textboard = boardToString();
			System.out.println(textboard);
			currentPlayer++;
		}
		if(currentPlayer == 1) {
			System.out.println("HUMAN WINS");
		} else {
			System.out.println("ALICE WINS!");
		}
	}
	
	public static String boardToString() {
		StringBuilder s = new StringBuilder();
		s.append("  --------------- ALICE\n");
		for(int i = 0; i < MAX_HEIGHT; i++) {
			s.append(MAX_HEIGHT - i);
			s.append(' ');
			for(int j = 0; j < MAX_WIDTH; j++) {
				s.append('|');
				if(board[i][j] != SMALL_SAMURAI && board[i][j] != SMALL_NINJA 
						&& board[i][j] != SAMURAI && board[i][j] != KING
						&& board[i][j] != NINJA ) {
					s.append(' ');
				} else {
					s.append(board[i][j]);
				}
			}
			s.append('|');
			s.append('\n');
			
		}
		s.append("  --------------- HUMAN\n");
		s.append(' ');
		s.append(' ');
		for(int c = 65; c < 65 + MAX_WIDTH; c++) {
			s.append(' ');
			s.append((char)c);
		}
		return s.toString();
	}
	
	public static boolean checkIfLegal(int initR, int initC, int destR, int destC) {

		if(board[initR][initC] == NINJA) {
			//This is gonna mess with you later when you attemp the only move backwards thing
			if(Math.abs(destR - initR) != Math.abs(destC - initC) || initR - destR < 0) {
				return false;
			}
			//todo
			//cannot jump over other pieces
			//can only move backwards if attacking
		} else if(board[initR][initC] == SAMURAI) {
			if(initC - destC != 0 && initR - destR != 0) {
				return false;
			}
			if(destR - initR < 0) {
				return false;
			}
			//todo
			//cannot jump over pieces
			//never moves backwards
			//can only attack in the foward direction
		} else if(board[initR][initC] == SMALL_NINJA) {
			if(Math.abs(destR - initR) != 1 && Math.abs(destC - initC) != 1) {
				return false;
			}
			//adhere to small ninja rules
		} else if(board[initR][initC] == SMALL_SAMURAI) {
			if(initC - destC != 0 && initR - destR != 0) {
				return false;
			}
			if(destR - initR < 0) {
				return false;
			}
			//adhere to small samurai rules
		} else if(board[initR][initC] == KING) {
			return false;
		}
		
		return true;
	}
	
	public static boolean checkGameOver() {
		return true;
	}
	
	public static String[] generateMove() {
		return new String[] {"A0", "B6"};
	}
	
	public static int[][] translateMove(String[] s) {
		int[][] trans = new int[2][2];
		
		trans[0][1] = s[0].charAt(0) - 65;
		trans[0][0] = 8 - (s[0].charAt(1) - '0');
		
		trans[1][1] = s[1].charAt(0) - 65; //Column
		trans[1][0] = 8 - (s[1].charAt(1) - '0'); //row
		
		return trans;
	}
	
	public static void updateBoard(int initR, int initC, int destR, int destC) {
		char piece = board[initR][initC];
		board[destR][destC] = piece;
		board[initR][initC] = ' ';
	}
	
	public static void attack(int r, int c) {
		
	}
	public static void init(int player) {
		turnCounter = 0;
		board = new char[MAX_HEIGHT][MAX_WIDTH];
		currentPlayer = player;
		//Topside
		board[0][3] = KING;
		for(int i = 0; i < 3; i++) {
			board[1][i] = NINJA;
		}
		for(int i = 4; i < MAX_WIDTH; i++) {
			board[1][i] = SAMURAI;
		}
		for(int i = 0; i < 3; i++) {
			board[2][i] = SMALL_SAMURAI;
		}
		for(int i = 4; i < MAX_WIDTH; i++) {
			board[2][i] = SMALL_NINJA;
		}
		
		//Bottomside
		board[7][3] = KING;
		for(int i = 0; i < 3; i++) {
			board[6][i] = SAMURAI;
		}
		for(int i = 4; i < MAX_WIDTH; i++) {
			board[6][i] = NINJA;
		}
		for(int i = 0; i < 3; i++) {
			board[5][i] = SMALL_NINJA;
		}
		for(int i = 4; i < MAX_WIDTH; i++) {
			board[5][i] = SMALL_SAMURAI;
		}
	}
	
	public static void minMax() {
		
	}
	
	public static void main(String[] args) {
		gameLoop();
	}
}
