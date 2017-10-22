import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

public class Alice {

	public static ArrayList<ArrayList<GamePiece>> gameBoard;
	public static final int MAX_DEPTH = 4;
	public static final int MAX_WIDTH = 7;
	public static final int MAX_HEIGHT = 8;
	public static int currentPlayer;
	public static int turnCounter;
	public static ArrayList<String> playerLegal;
	public static ArrayList<String> comLegal;
	public static ArrayList<GamePiece> comLegalPieces;

	public static void gameLoop() {
		Scanner in = new Scanner(System.in);
		System.out
				.println("HI-YA! is a two player game developed for Sacramento States fall 2017 " + "CSC 180 class.\n");
		System.out.println("Familiarize yourself with the game before playing.");
		System.out.println("Which player should go first? (1 = Human, 2 = Computer)");
		int initPlayer = in.nextInt();
		in.nextLine(); // clearBuffer
		init(initPlayer);
		String textboard = boardToString(gameBoard);
		System.out.println(textboard);
		boolean gameOn = true;
		int[][] translated = new int[2][2];
		Hashtable<String, Integer> piecesMoves;
		movesForPieces(gameBoard);
		while (gameOn) {
			turnCounter++;
			System.out.println("Turn " + turnCounter);
			String[] moveInput = new String[2];
			do {
				if (currentPlayer == 1) {
					System.out.println(playerLegal);
					System.out.print("Enter HUMAN move (initial destination): ");
					moveInput[0] = in.next();
					moveInput[1] = in.next();
					in.nextLine(); //buffer clear
					//moveInput = playerGenerateMove();// Random human generated moves
					translated = translateMove(moveInput);
				} else {
					translated = generateSmartMove();
				}
				GamePiece selected = gameBoard.get(translated[0][0]).get(translated[0][1]);
				if (selected != null && selected.player == currentPlayer) {
					piecesMoves = selected.getMoves();
				} else {
					System.out.println("No Piece exists at that point or that is not your piece.");
					piecesMoves = new Hashtable<>();
				}
			} while (!piecesMoves.containsKey("" + translated[1][0] + translated[1][1]));
			updateBoard(gameBoard, translated[0][0], translated[0][1], translated[1][0], translated[1][1]);
			movesForPieces(gameBoard);
			gameOn = checkTie();
			textboard = boardToString(gameBoard);
			System.out.println(textboard);
			if (currentPlayer == 1) {
				currentPlayer++;
			} else {
				String compMove = reverseTranslate(translated[0][0], translated[0][1]) + " -> " + reverseTranslate(translated[1][0], translated[1][1]);
				String reverseMove = inverse(translated[0][0], translated[0][1]) + " -> " + inverse(translated[1][0], translated[1][1]);
				System.out.println("Alice moves: " + compMove);
				System.out.println("Inverse moved: " + reverseMove);
				currentPlayer--;
			}
		}
		System.out.println("Tie game! Hit enter to close!");
		in.nextLine();
	}

	public static String boardToString(ArrayList<ArrayList<GamePiece>> board) {
		StringBuilder s = new StringBuilder();
		s.append("  --------------- ALICE\n");
		for (int i = 0; i < MAX_HEIGHT; i++) {
			s.append(MAX_HEIGHT - i);
			s.append(' ');
			for (int j = 0; j < MAX_WIDTH; j++) {
				s.append('|');
				if (board.get(i).get(j) == null) {
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
		for (int c = 65; c < 65 + MAX_WIDTH; c++) {
			s.append(' ');
			s.append((char) c);
		}
		return s.toString();
	}

	public static boolean checkTie() {
		if (playerLegal.isEmpty() && comLegal.isEmpty()) {
			return false;
		} else if (playerLegal.isEmpty()) {
			gameOver(gameBoard, "No legal moves for HUMAN");
		} else if (comLegal.isEmpty()) {
			gameOver(gameBoard, "No legal moves for ALICE");
		}
		return true;
	}

	public static void gameOver(ArrayList<ArrayList<GamePiece>> board, String additional) {
		if (board.equals(gameBoard)) {
			String textboard = boardToString(gameBoard);
			System.out.println(textboard);
			System.out.println(additional);
			if (currentPlayer == 1) {
				System.out.println("Human WINS!");
			} else {
				System.out.println("Alice WINS!");
			}
			System.out.println("Hit enter to close");
			Scanner in = new Scanner(System.in);
			in.nextLine();
			System.exit(1);
		}
	}

	public static String[] generateMove() {
		// Random Generation
		Random rand = new Random();
		int piece = rand.nextInt(comLegal.size());

		String[] move = new String[2];
		move[0] = comLegal.get(piece).substring(0, 2);
		move[1] = comLegal.get(piece).substring(6, 8);

		return move;
	}

	// For Quick Testing Purposes
	public static String[] playerGenerateMove() {
		// Random Generation
		Random rand = new Random();
		int piece = rand.nextInt(playerLegal.size());

		String[] move = new String[2];
		move[0] = playerLegal.get(piece).substring(0, 2);
		move[1] = playerLegal.get(piece).substring(6, 8);

		return move;
	}

	public static int[][] generateSmartMove() {
		int[][] bestMove = new int[2][2];
		int bestScore = Integer.MIN_VALUE;
		ArrayList<ArrayList<GamePiece>> copyBoard = cloneBoard(gameBoard); //Copy board for moving pieces
		getNewMovesForCloned(copyBoard); //Generate all possible moves available for pieces.
		for(int i = 0; i < MAX_HEIGHT; i++) {
			for(int j = 0; j < MAX_WIDTH; j++) {
				//Only interested in comPieces at this time. Using gameBoard currently for bug dectection on copy board
				if(gameBoard.get(i).get(j) != null && !(gameBoard.get(i).get(j) instanceof King) 
						&& gameBoard.get(i).get(j).player == 2) {
					GamePiece piece = gameBoard.get(i).get(j);
					int originalR = piece.row;
					int originalC = piece.col;
					Hashtable<String, Integer> moves = piece.getMoves();
					if(!moves.isEmpty()) {
						for(String key: moves.keySet()) {
							int destR = Character.getNumericValue(key.charAt(0));
							int destC = Character.getNumericValue(key.charAt(1));
							if(destR == 6 && destC == 3) {
								bestMove[0][0] = originalR;
								bestMove[0][1] = originalC;
								bestMove[1][0] = destR;
								bestMove[1][1] = destC;
								bestScore = moves.get(key);
								return bestMove;
							}
							makeMoveOnCloned(copyBoard, piece, destR, destC, 2);
							
							int score = min(copyBoard, 1, bestScore); //begin minmax
							//Change to best scoring move
							if(score > bestScore) {
								bestMove[0][0] = originalR;
								bestMove[0][1] = originalC;
								bestMove[1][0] = destR;
								bestMove[1][1] = destC;
								bestScore = score;
							}
							if(originalR == destR && destC == originalC) {
								System.out.println();
							}
							//board retract move
							piece.row = originalR;
							piece.col = originalC;
							copyBoard.get(piece.row).set(piece.col, piece);
							copyBoard.get(destR).set(destC, null);
						}
					}
				}
			}
		}
		
		return bestMove;
	}

	// Max is COMPUTERS MOVE
	public static int max(ArrayList<ArrayList<GamePiece>> board, int depth, int beta) {
		ArrayList<ArrayList<GamePiece>> copyBoard = cloneBoard(board);
		getNewMovesForCloned(copyBoard); //Generate all possible moves available for the current state.
		if(depth > MAX_DEPTH) {	//At max depth, iterate through possible moves on board
			int score = 0;
			score = getBoardState(copyBoard);
			if(score > beta) {
				return beta;
			}		
			return score;
		} else if(copyBoard.get(6).get(4) != null && copyBoard.get(6).get(4).player == 2) {
			return Integer.MAX_VALUE - depth;
		} else {
			int bestScore = Integer.MIN_VALUE;
			for(int i = 0; i < MAX_HEIGHT; i++) {
				for(int j = 0; j < MAX_WIDTH; j++) {
					if(copyBoard.get(i).get(j) != null && copyBoard.get(i).get(j).player == 2
							&& !(copyBoard.get(i).get(j) instanceof King)) {
						GamePiece piece = copyBoard.get(i).get(j);
						int originalR = piece.row;
						int originalC = piece.col;
						Hashtable<String, Integer> moves = piece.getMoves();
						for(String key: moves.keySet()) {
							int destR = Character.getNumericValue(key.charAt(0));
							int destC = Character.getNumericValue(key.charAt(1));
							makeMoveOnCloned(copyBoard, piece, destR, destC, 2);
							
							int score = min(copyBoard, depth + 1, bestScore);
							if(score > bestScore) {
								bestScore = score;
							}
							if(bestScore > beta) {
								return beta;
							}
							piece.row = originalR;
							piece.col = originalC;
							copyBoard.get(piece.row).set(piece.col, piece);
							copyBoard.get(destR).set(destC, null);
						}
					}
				}
			}
			
			return bestScore;
		}
	}

	//Min is PLAYERS MOVE
	public static int min(ArrayList<ArrayList<GamePiece>> board, int depth, int alpha) {
		ArrayList<ArrayList<GamePiece>> copyBoard = cloneBoard(board);
		getNewMovesForCloned(copyBoard); //Generate all possible moves available for the current state.
		if(depth > MAX_DEPTH) { //If as deep as possible, iterate through all possible moves
			int score = 0;
			score = getBoardState(copyBoard);
			if(score < alpha) {
				return alpha;
			}		
			return score;
		} else if(copyBoard.get(1).get(4) != null && copyBoard.get(1).get(4).player == 1) { //Check if player has finishing move
			return Integer.MIN_VALUE + depth;
		} else { //Iterate through possible moves and call MAX
			int worstScore = Integer.MAX_VALUE;
			for(int i = 0; i < MAX_HEIGHT; i++) {
				for(int j = 0; j < MAX_WIDTH; j++) {
					if(copyBoard.get(i).get(j) != null && copyBoard.get(i).get(j).player == 1
							&& !(copyBoard.get(i).get(j) instanceof King)) {
						GamePiece piece = copyBoard.get(i).get(j);
						int originalR = piece.row;
						int originalC = piece.col;
						Hashtable<String, Integer> moves = piece.getMoves();
						for(String key: moves.keySet()) {
							int destR = Character.getNumericValue(key.charAt(0));
							int destC = Character.getNumericValue(key.charAt(1));
							makeMoveOnCloned(copyBoard, piece, destR, destC, 1);
							
							int score = max(copyBoard, depth + 1, worstScore);
							if(score < worstScore) {
								worstScore = score;
							}
							if(worstScore < alpha) {
								return alpha;
							}
							//retract move on copyBoard
							piece.row = originalR;
							piece.col = originalC;
							copyBoard.get(piece.row).set(piece.col, piece);
							copyBoard.get(destR).set(destC, null);
						}
					}
				}
			}
			
			return worstScore;
		}
	}
	
	public static int getBoardState(ArrayList<ArrayList<GamePiece>> board) {
		int sum = 0;
		for(int i = 0; i < MAX_HEIGHT; i++) {
			for(int j = 0; j < MAX_WIDTH; j++) {
				if(board.get(i).get(j) != null) {
					sum += board.get(i).get(j).getAttack();
				}
			}
		}
		
		return sum;
	}

	public static void makeMoveOnCloned(ArrayList<ArrayList<GamePiece>> b, GamePiece p, int destR, int destC, int player) {
		int initR = p.row;
		int initC = p.col;
		b.get(destR).set(destC, p);
		p.row = destR;
		p.col = destC;
		b.get(initR).set(initC, null);
		if(player == 1 && checkPlayerAttack(b, destR, destC)) {
			GamePiece victim = b.get(destR - 1).get(destC);
			if (victim instanceof King) {
				b.get(destR - 1).set(destC, null);
			} else if (victim instanceof Ninja) {
				MiniNinja newPiece = new MiniNinja(2, destR - 1, destC);
				b.get(destR - 1).set(destC, newPiece);
			} else if (victim instanceof Samurai) {
				MiniSamurai newPiece = new MiniSamurai(2, destR - 1, destC);
				b.get(destR - 1).set(destC, newPiece);
			} else if (victim instanceof MiniNinja) {
				b.get(destR - 1).set(destC, null);
			} else if (victim instanceof MiniSamurai) {
				b.get(destR - 1).set(destC, null);
			}
		} else if(player == 2 && checkComAttack(b, destR, destC)) {
			GamePiece victim = b.get(destR + 1).get(destC);
			if (victim instanceof King) {
				b.get(destR + 1).set(destC, null);
			} else if (victim instanceof Ninja) {
				MiniNinja newPiece = new MiniNinja(2, destR + 1, destC);
				b.get(destR + 1).set(destC, newPiece);
			} else if (victim instanceof Samurai) {
				MiniSamurai newPiece = new MiniSamurai(2, destR + 1, destC);
				b.get(destR + 1).set(destC, newPiece);
			} else if (victim instanceof MiniNinja) {
				b.get(destR + 1).set(destC, null);
			} else if (victim instanceof MiniSamurai) {
				b.get(destR + 1).set(destC, null);
			}
		}
	}
	
	public static void getNewMovesForCloned(ArrayList<ArrayList<GamePiece>> board) {
		for(int i = 0; i < MAX_HEIGHT; i++) {
			for(int j = 0; j < MAX_WIDTH; j++) {
				if(board.get(i).get(j) != null) {
					GamePiece piece = board.get(i).get(j);
					piece.generateMoves(board);
				}
			}
		}
	}

	public static ArrayList<ArrayList<GamePiece>> cloneBoard(ArrayList<ArrayList<GamePiece>> b) {
		ArrayList<ArrayList<GamePiece>> cloneBoard = new ArrayList<>();
		for (int i = 0; i < MAX_HEIGHT; i++) {
			cloneBoard.add(new ArrayList<>());
			for (int j = 0; j < MAX_WIDTH; j++) {
				GamePiece piece = b.get(i).get(j);
				if (piece != null) {
					if (piece instanceof King) {
						King temp = new King(piece.player, piece.row, piece.col);
						cloneBoard.get(i).add(temp);
					} else if (piece instanceof Ninja) {
						Ninja temp = new Ninja(piece.player, piece.row, piece.col);
						cloneBoard.get(i).add(temp);
					} else if (piece instanceof Samurai) {
						Samurai temp = new Samurai(piece.player, piece.row, piece.col);
						cloneBoard.get(i).add(temp);
					} else if (piece instanceof MiniNinja) {
						MiniNinja temp = new MiniNinja(piece.player, piece.row, piece.col);
						cloneBoard.get(i).add(temp);
					} else if (piece instanceof MiniSamurai) {
						MiniSamurai temp = new MiniSamurai(piece.player, piece.row, piece.col);
						cloneBoard.get(i).add(temp);
					}
				} else {
					cloneBoard.get(i).add(null);
				}
			}
		}
		return cloneBoard;
	}

	public static int[][] translateMove(String[] s) {
		int[][] trans = new int[2][2];

		trans[0][1] = s[0].charAt(0) - 65;
		trans[0][0] = 8 - (s[0].charAt(1) - '0');

		trans[1][1] = s[1].charAt(0) - 65; // Column
		trans[1][0] = 8 - (s[1].charAt(1) - '0'); // row

		return trans;
	}

	public static String reverseTranslate(int row, int col) {
		StringBuilder build = new StringBuilder();
		char colC = (char) (col + 65);
		build.append(colC);
		build.append(8 - (row));
		return build.toString();
	}
	
	public static String inverse(int row, int col) {
		StringBuilder build = new StringBuilder();
		char colC = (char) (71 - col);
		build.append(colC);
		build.append(row + 1);
		return build.toString();
	}

	public static void updateBoard(ArrayList<ArrayList<GamePiece>> board, int initR, int initC, int destR, int destC) {
		GamePiece piece = board.get(initR).get(initC);
		board.get(destR).set(destC, piece);
		board.get(initR).set(initC, null);
		piece.col = destC;
		piece.row = destR;
		if (currentPlayer == 1 && checkPlayerAttack(board, destR, destC)) {
			System.out.println("HIYA!");
			GamePiece victim = board.get(destR - 1).get(destC);
			if (victim instanceof King) {
				board.get(destR - 1).set(destC, null);
				gameOver(board, "COM KING SLAIN!");
			} else if (victim instanceof Ninja) {
				MiniNinja newPiece = new MiniNinja(2, destR - 1, destC);
				board.get(destR - 1).set(destC, newPiece);
			} else if (victim instanceof Samurai) {
				MiniSamurai newPiece = new MiniSamurai(2, destR - 1, destC);
				board.get(destR - 1).set(destC, newPiece);
			} else if (victim instanceof MiniNinja) {
				board.get(destR - 1).set(destC, null);
			} else if (victim instanceof MiniSamurai) {
				board.get(destR - 1).set(destC, null);
			}
		} else if (currentPlayer == 2 && checkComAttack(board, destR, destC)) {
			System.out.println("HIYA!");
			GamePiece victim = board.get(destR + 1).get(destC);
			if (victim instanceof King) {
				board.get(destR + 1).set(destC, null);
				gameOver(board, "HUMAN KING SLAIN!");
			} else if (victim instanceof Ninja) {
				MiniNinja newPiece = new MiniNinja(1, destR + 1, destC);
				board.get(destR + 1).set(destC, newPiece);
			} else if (victim instanceof Samurai) {
				MiniSamurai newPiece = new MiniSamurai(1, destR + 1, destC);
				board.get(destR + 1).set(destC, newPiece);
			} else if (victim instanceof MiniNinja) {
				board.get(destR + 1).set(destC, null);
			} else if (victim instanceof MiniSamurai) {
				board.get(destR + 1).set(destC, null);
			}
		}
	}

	public static boolean checkPlayerAttack(ArrayList<ArrayList<GamePiece>> board, int destR, int destC) {
		return currentPlayer == 1 && destR - 1 >= 0 && board.get(destR - 1).get(destC) != null
				&& board.get(destR - 1).get(destC).player == 2;
	}

	public static boolean checkComAttack(ArrayList<ArrayList<GamePiece>> board, int destR, int destC) {
		return currentPlayer == 2 && destR + 1 < MAX_HEIGHT && board.get(destR + 1).get(destC) != null
				&& board.get(destR + 1).get(destC).player == 1;
	}

	public static void init(int player) {
		turnCounter = 0;
		gameBoard = new ArrayList<>();
		playerLegal = new ArrayList<>();
		comLegal = new ArrayList<>();
		comLegalPieces = new ArrayList<>();
		for (int i = 0; i < MAX_HEIGHT; i++) {
			gameBoard.add(new ArrayList<GamePiece>());
			for (int j = 0; j < MAX_WIDTH; j++) {
				gameBoard.get(i).add(null);
			}
		}
		currentPlayer = player;
		// Topside
		gameBoard.get(0).set(3, new King(2, 0, 3));
		for (int i = 0; i < 3; i++) {
			GamePiece piece = new Ninja(2, 1, i);
			gameBoard.get(1).set(i, piece);
		}
		for (int i = 4; i < MAX_WIDTH; i++) {
			GamePiece piece = new Samurai(2, 1, i);
			gameBoard.get(1).set(i, piece);
		}
		for (int i = 0; i < 3; i++) {
			GamePiece piece = new MiniSamurai(2, 2, i);
			gameBoard.get(2).set(i, piece);
		}
		for (int i = 4; i < MAX_WIDTH; i++) {
			GamePiece piece = new MiniNinja(2, 2, i);
			gameBoard.get(2).set(i, piece);
		}

		// Bottomside
		gameBoard.get(7).set(3, new King(1, 7, 3));
		for (int i = 0; i < 3; i++) {
			GamePiece piece = new Samurai(1, 6, i);
			gameBoard.get(6).set(i, piece);
		}
		for (int i = 4; i < MAX_WIDTH; i++) {
			GamePiece piece = new Ninja(1, 6, i);
			gameBoard.get(6).set(i, piece);
		}
		for (int i = 0; i < 3; i++) {
			GamePiece piece = new MiniNinja(1, 5, i);
			gameBoard.get(5).set(i, piece);
		}
		for (int i = 4; i < MAX_WIDTH; i++) {
			GamePiece piece = new MiniSamurai(1, 5, i);
			gameBoard.get(5).set(i, piece);
		}
	}

	public static void movesForPieces(ArrayList<ArrayList<GamePiece>> board) {
		playerLegal.clear();
		comLegal.clear();
		comLegalPieces.clear();
		for (int i = 0; i < MAX_HEIGHT; i++) {
			for (int j = 0; j < MAX_WIDTH; j++) {
				if (board.get(i).get(j) != null) {
					GamePiece piece = board.get(i).get(j);
					piece.generateMoves(board);
					if (piece.player == 1 && !(piece instanceof King)) {
						Hashtable<String, Integer> moves = piece.getMoves();
						if (!moves.isEmpty()) {
							StringBuilder build = new StringBuilder();
							build.append(reverseTranslate(piece.row, piece.col));
							build.append(" -> ");
							for (String s : moves.keySet()) {
								int potentialR = Character.getNumericValue(s.charAt(0));
								int potentialC = Character.getNumericValue(s.charAt(1));
								build.append(reverseTranslate(potentialR, potentialC));
								build.append(" ");
							}
							playerLegal.add(build.toString());
						}
					} else if (!(piece instanceof King)) {
						Hashtable<String, Integer> moves = piece.getMoves();
						comLegalPieces.add(piece);
						if (!moves.isEmpty()) {
							StringBuilder build = new StringBuilder();
							build.append(reverseTranslate(piece.row, piece.col));
							build.append(" -> ");
							for (String s : moves.keySet()) {
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
