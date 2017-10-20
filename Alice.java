import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

public class Alice {

	public static ArrayList<ArrayList<GamePiece>> gameBoard;
	public static final int MAX_DEPTH = 3;
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
		String textboard = boardToString();
		System.out.println(textboard);
		boolean gameOn = true;
		int[][] translated = new int[2][2];
		Hashtable<String, Integer> piecesMoves;
		movesForPieces(gameBoard);
		generateSmartMove();
		while (gameOn) {
			turnCounter++;
			System.out.println("Turn " + turnCounter);
			String[] moveInput = new String[2];
			do {
				if (currentPlayer == 1) {
					textboard = boardToString();
					System.out.println(textboard);
					System.out.println(playerLegal);
					// System.out.print("Enter HUMAN move (initial destination): ");
					// moveInput[0] = in.next();
					// moveInput[1] = in.next();
					// in.nextLine(); //buffer clear
					moveInput = playerGenerateMove();
				} else {
					moveInput = generateMove();
				}
				translated = translateMove(moveInput);
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
			if (currentPlayer == 1) {
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
		for (int i = 0; i < MAX_HEIGHT; i++) {
			s.append(MAX_HEIGHT - i);
			s.append(' ');
			for (int j = 0; j < MAX_WIDTH; j++) {
				s.append('|');
				if (gameBoard.get(i).get(j) == null) {
					s.append(' ');
				} else {
					s.append(gameBoard.get(i).get(j));
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
			String textboard = boardToString();
			System.out.println(textboard);
			System.out.println(additional);
			if (currentPlayer == 1) {
				System.out.println("Human WINS!");
			} else {
				System.out.println("Alice WINS!");
			}
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
		int best = Integer.MIN_VALUE;
		int[][] bestMove = new int[2][2]; // Maybe make this a string for easy integration
		for (int i = 0; i < comLegalPieces.size(); i++) {
			GamePiece current = comLegalPieces.get(i);
			ArrayList<ArrayList<GamePiece>> cloned = cloneBoard(gameBoard);
			Hashtable<String, Integer> moves = current.getMoves();
			GamePiece moved = cloned.get(current.row).get(current.col);
			for (String key : moves.keySet()) {
				int destR = Character.getNumericValue(key.charAt(0));
				int destC = Character.getNumericValue(key.charAt(1));
				makeMoveOnCloned(cloned, cloned.get(moved.row).get(moved.col), destR, destC);
				movesForPieces(cloned);
				int score = min(cloned, 1);
				if(score > best) {
					bestMove[0][0] = current.row;
					bestMove[0][1] = current.col;
					bestMove[1][0] = moved.row;
					bestMove[1][1] = moved.col;
					best = score;
				}
				moved.row = current.row;
				moved.col = current.col;
				cloned.get(moved.row).set(moved.col, moved);
				cloned.get(destR).set(destC, null);
			}
		}
		return bestMove;
	}

	// Maybe return gameMove?
	public static int max(ArrayList<ArrayList<GamePiece>> b, int depth) {
		if (depth >= MAX_DEPTH) {
			int high = Integer.MIN_VALUE;
			for (int i = 0; i < MAX_HEIGHT; i++) {
				for (int j = 0; j < MAX_WIDTH; j++) {
					if (b.get(i).get(j) != null && b.get(i).get(j).player == 2) {
						GamePiece currentPiece = b.get(i).get(j);
						Hashtable<String, Integer> moves = currentPiece.getMoves();
						for (String key : moves.keySet()) {
							if (moves.get(key) > high) {
								high = moves.get(key);
							}
						}
					}
				}
			}
			return high - depth;
		} else if (b.get(6).get(4) != null && b.get(6).get(4).player == 2) {
			return Integer.MAX_VALUE - depth;
		}
		int bestScore = Integer.MIN_VALUE;
		int score = 0;
		for (int i = 0; i < MAX_HEIGHT; i++) {
			for (int j = 0; j < MAX_WIDTH; j++) {
				if (b.get(i).get(j) != null && b.get(i).get(j).player == 2) {
					GamePiece current = b.get(i).get(j);
					ArrayList<ArrayList<GamePiece>> cloned = cloneBoard(b);
					Hashtable<String, Integer> moves = current.getMoves();
					GamePiece moved = cloned.get(current.row).get(current.col);
					for (String key : moves.keySet()) {
						int destR = Character.getNumericValue(key.charAt(0));
						int destC = Character.getNumericValue(key.charAt(1));
						makeMoveOnCloned(cloned, cloned.get(moved.row).get(moved.col), destR, destC);
						movesForPieces(cloned);
						score = min(cloned, depth + 1);
						if (score > bestScore) {
							bestScore = score;
						}
						// undo move
						moved.row = current.row;
						moved.col = current.col;
						cloned.get(moved.row).set(moved.col, moved);
						cloned.get(destR).set(destC, null);
					}
				}
			}
		}
		return bestScore - depth;
	}

	public static int min(ArrayList<ArrayList<GamePiece>> b, int depth) {
		if (depth > MAX_DEPTH) {
			int low = Integer.MAX_VALUE;
			for (int i = 0; i < MAX_HEIGHT; i++) {
				for (int j = 0; j < MAX_WIDTH; j++) {
					if (b.get(i).get(j) != null && b.get(i).get(j).player == 1) {
						GamePiece currentPiece = b.get(i).get(j);
						Hashtable<String, Integer> moves = currentPiece.getMoves();
						for (String key : moves.keySet()) {
							if (moves.get(key) < low) {
								low = moves.get(key);
							}
						}
					}
				}
			}
			return low + depth;
		} else if (b.get(1).get(4) != null && b.get(1).get(4).player == 1) {
			return Integer.MIN_VALUE - depth;
		}
		int worstScore = Integer.MIN_VALUE;
		int score = 0;
		for (int i = 0; i < MAX_HEIGHT; i++) {
			for (int j = 0; j < MAX_WIDTH; j++) {
				if (b.get(i).get(j) != null && b.get(i).get(j).player == 1) {
					GamePiece current = b.get(i).get(j);
					ArrayList<ArrayList<GamePiece>> cloned = cloneBoard(b);
					Hashtable<String, Integer> moves = current.getMoves();
					GamePiece moved = cloned.get(current.row).get(current.col);
					for (String key : moves.keySet()) {
						int destR = Character.getNumericValue(key.charAt(0));
						int destC = Character.getNumericValue(key.charAt(1));
						makeMoveOnCloned(cloned, cloned.get(moved.row).get(moved.col), destR, destC);
						movesForPieces(cloned);
						score = max(cloned, depth + 1);
						if (score < worstScore) {
							worstScore = score;
						}
						// undo move
						moved.row = current.row;
						moved.col = current.col;
						cloned.get(moved.row).set(moved.col, moved);
						cloned.get(destR).set(destC, null);
					}
				}
			}
		}
		return worstScore + depth;
	}

	public static void makeMoveOnCloned(ArrayList<ArrayList<GamePiece>> b, GamePiece p, int destR, int destC) {
		int initR = p.row;
		int initC = p.col;
		b.get(destR).set(destC, p);
		p.row = destR;
		p.col = destC;
		b.get(initR).set(initC, null);
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
