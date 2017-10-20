import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

public class Alice {
	
	public static ArrayList<ArrayList<GamePiece>> board;
	public static final int MAX_DEPTH = 5;
	public static final int MAX_WIDTH = 7;
	public static final int MAX_HEIGHT = 8;
	public static int currentPlayer;
	public static int turnCounter;
	public static ArrayList<String> playerLegal;
	public static ArrayList<String> comLegal;
	public static ArrayList<GamePiece> comLegalPieces;
	
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
		Hashtable<String, Integer> piecesMoves;
		MovesForPieces();
		while(gameOn) {
			turnCounter++;
			System.out.println("Turn " + turnCounter);
			String[] moveInput = new String[2];
			do {
				if(currentPlayer == 1) {
					System.out.println(playerLegal);
					System.out.print("Enter HUMAN move (initial destination): ");
					moveInput[0] = in.next();
					moveInput[1] = in.next();
					in.nextLine(); //buffer clear
				} else {
					moveInput = generateMove();
				}
				translated = translateMove(moveInput);
				GamePiece selected = board.get(translated[0][0]).get(translated[0][1]);
				if(selected != null && selected.player == currentPlayer) {
					piecesMoves = selected.getMoves();
				} else {
					System.out.println("No Piece exists at that point or that is not your piece.");
					piecesMoves = new Hashtable<>();
				}
			} while(!piecesMoves.containsKey("" + translated[1][0] + translated[1][1]));
			updateBoard(translated[0][0], translated[0][1], translated[1][0], translated[1][1]);
			textboard = boardToString();
			System.out.println(textboard);
			MovesForPieces();
			gameOn = checkTie();
			if(currentPlayer == 1) {
				currentPlayer++;
			} else {
				currentPlayer--;
			}
		}
		System.out.println("Tie game!");
	}
	
	public static String boardToString() {
		StringBuilder s = new StringBuilder();
		s.append("  --------------- ALICE\n");
		for(int i = 0; i < MAX_HEIGHT; i++) {
			s.append(MAX_HEIGHT - i);
			s.append(' ');
			for(int j = 0; j < MAX_WIDTH; j++) {
				s.append('|');
				if(board.get(i).get(j) == null) {
					s.append(' ');
				} else {
					s.append(board.get(i).get(j));
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
	
	public static boolean checkTie() {
		if(playerLegal.isEmpty() && comLegal.isEmpty()) {
			return false;
		} else if(playerLegal.isEmpty()) {
			gameOver("No legal moves for HUMAN");
		} else if(comLegal.isEmpty()) {
			gameOver("No legal moves for ALICE");
		}
		return true;
	}
	
	public static void gameOver(String additional) {
		String textboard = boardToString();
		System.out.println(textboard);
		System.out.println(additional);
		if(currentPlayer == 1) {
			System.out.println("Human WINS!");
		} else {
			System.out.println("Alice WINS!");
		}
		System.exit(1);
	}
	
	public static String[] generateMove() {
		//Random Generation
		Random rand = new Random();
		int piece = rand.nextInt(comLegal.size());

		String[] move = new String[2];
		move[0] = comLegal.get(piece).substring(0, 2);
		move[1] = comLegal.get(piece).substring(6, 8);
		
		return move;
	}
	
	public static void generateSmartMove() {
		
	}
	
	public static int[][] translateMove(String[] s) {
		int[][] trans = new int[2][2];
		
		trans[0][1] = s[0].charAt(0) - 65;
		trans[0][0] = 8 - (s[0].charAt(1) - '0');
		
		trans[1][1] = s[1].charAt(0) - 65; //Column
		trans[1][0] = 8 - (s[1].charAt(1) - '0'); //row
		
		return trans;
	}
	
	public static String reverseTranslate(int row, int col) {
		StringBuilder build = new StringBuilder();
		char colC = (char)(col + 65);
		build.append(colC);
		build.append(8 - (row));
		return build.toString();
	}
	
	public static void updateBoard(int initR, int initC, int destR, int destC) {
		GamePiece piece = board.get(initR).get(initC);
		board.get(destR).set(destC, piece);
		board.get(initR).set(initC, null);
		piece.col = destC;
		piece.row = destR;
		if(currentPlayer == 1 && checkPlayerAttack(destR, destC)) {
			System.out.println("HIYA!");
			GamePiece victim = board.get(destR - 1).get(destC);
			if(victim instanceof King) {
				board.get(destR - 1).set(destC, null);
				gameOver("COM KING SLAIN!");
			} else if(victim instanceof Ninja) {
				MiniNinja newPiece = new MiniNinja(2, destR - 1, destC);
				board.get(destR - 1).set(destC, newPiece);
			} else if(victim instanceof Samurai) {
				MiniSamurai newPiece = new MiniSamurai(2, destR - 1, destC);
				board.get(destR - 1).set(destC, newPiece);
			} else if(victim instanceof MiniNinja) {
				board.get(destR - 1).set(destC, null);
			} else if(victim instanceof MiniSamurai) {
				board.get(destR - 1).set(destC, null);
			}
		} else if(currentPlayer == 2 && checkComAttack(destR, destC)){
			System.out.println("HIYA!");
			GamePiece victim = board.get(destR + 1).get(destC);
			if(victim instanceof King) {
				board.get(destR + 1).set(destC, null);
				gameOver("HUMAN KING SLAIN!");
			} else if(victim instanceof Ninja) {
				MiniNinja newPiece = new MiniNinja(1, destR + 1, destC);
				board.get(destR + 1).set(destC, newPiece);
			} else if(victim instanceof Samurai) {
				MiniSamurai newPiece = new MiniSamurai(1, destR + 1, destC);
				board.get(destR + 1).set(destC, newPiece);
			} else if(victim instanceof MiniNinja) {
				board.get(destR + 1).set(destC, null);
			} else if(victim instanceof MiniSamurai) {
				board.get(destR + 1).set(destC, null);
			}
		}
	}
	
	public static boolean checkPlayerAttack(int destR, int destC) {
		return currentPlayer == 1 && destR - 1 >= 0 && board.get(destR - 1).get(destC) != null 
				&& board.get(destR - 1).get(destC).player == 2;
	}
	
	public static boolean checkComAttack(int destR, int destC) {
		return currentPlayer == 2 && destR + 1 < MAX_HEIGHT && board.get(destR + 1).get(destC) != null
				&& board.get(destR + 1).get(destC).player == 1;
	}
	
	public static void init(int player) {
		turnCounter = 0;
		board = new ArrayList<>();
		playerLegal = new ArrayList<>();
		comLegal = new ArrayList<>();
		comLegalPieces = new ArrayList<>();
		for(int i = 0; i < MAX_HEIGHT; i++) {
			board.add(new ArrayList<GamePiece>());
			for(int j = 0; j < MAX_WIDTH; j++) {
				board.get(i).add(null);
			}
		}
		currentPlayer = player;
		//Topside
		board.get(0).set(3, new King(2, 0, 3));
		for(int i = 0; i < 3; i++) {
			GamePiece piece = new Ninja(2, 1, i);
			board.get(1).set(i, piece);
		}
		for(int i = 4; i < MAX_WIDTH; i++) {
			GamePiece piece = new Samurai(2, 1, i);
			board.get(1).set(i, piece);
		}
		for(int i = 0; i < 3; i++) {
			GamePiece piece = new MiniSamurai(2, 2, i);
			board.get(2).set(i, piece);
		}
		for(int i = 4; i < MAX_WIDTH; i++) {
			GamePiece piece = new MiniNinja(2, 2, i);
			board.get(2).set(i, piece);
		}
		
		//Bottomside
		board.get(7).set(3, new King(1, 7, 3));
		for(int i = 0; i < 3; i++) {
			GamePiece piece = new Samurai(1, 6, i);
			board.get(6).set(i, piece);
		}
		for(int i = 4; i < MAX_WIDTH; i++) {
			GamePiece piece = new Ninja(1, 6, i);
			board.get(6).set(i, piece);
		}
		for(int i = 0; i < 3; i++) {
			GamePiece piece = new MiniNinja(1, 5, i);
			board.get(5).set(i, piece);
		}
		for(int i = 4; i < MAX_WIDTH; i++) {
			GamePiece piece = new MiniSamurai(1, 5, i);
			board.get(5).set(i, piece);
		}
	}
	
	public static void MovesForPieces() {
		playerLegal.clear();
		comLegal.clear();
		comLegalPieces.clear();
		for(int i = 0; i < MAX_HEIGHT; i++) {
			for(int j = 0; j < MAX_WIDTH; j++) {
				if(board.get(i).get(j) != null) {
					GamePiece piece = board.get(i).get(j);
					piece.generateMoves(board);
					if(piece.player == 1 && !(piece instanceof King)) {
						Hashtable<String, Integer> moves = piece.getMoves();
						if(!moves.isEmpty()) {
							StringBuilder build = new StringBuilder();
							build.append(reverseTranslate(piece.row, piece.col));
							build.append(" -> ");
							for(String s : moves.keySet()) {
								int potentialR = Character.getNumericValue(s.charAt(0));
								int potentialC = Character.getNumericValue(s.charAt(1));
								build.append(reverseTranslate(potentialR, potentialC));
								build.append(" ");
							}
							playerLegal.add(build.toString());
						}
					} else if(!(piece instanceof King)){
						Hashtable<String, Integer> moves = piece.getMoves();
						comLegalPieces.add(piece);
						if(!moves.isEmpty()) {
							StringBuilder build = new StringBuilder();
							build.append(reverseTranslate(piece.row, piece.col));
							build.append(" -> ");
							for(String s : moves.keySet()) {
								int potentialR = Character.getNumericValue(s.charAt(0));
								int potentialC = Character.getNumericValue(s.charAt(1));
								build.append(reverseTranslate(potentialR, potentialC));
								build.append(" ");
							}
							comLegal.add(build.toString());
						}
					}
					
				}
			}
		}
	}
	
	public static void main(String[] args) {
		gameLoop();
	}
}
