import java.util.ArrayList;
import java.util.Hashtable;

public class Ninja extends GamePiece implements Cloneable {
	
	public char character;
	public int attackValue;
	
	//Potentially use a hashtable to track both legal moves AND the value behind them?
	private Hashtable<String, Integer> legalMoves;
	
	public Ninja(int player, int row, int col) {
		super.player = player;
		super.row = row;
		super.col = col;
		if(player == 1) {
			this.character = 'N';
			this.attackValue = 1002;
		} else {
			this.character = 'I';
			this.attackValue = -1000;
		}
		
		legalMoves = new Hashtable<>();
	}
	
	
	@Override
	public void move(int[] move) {
		super.row = move[0];
		super.col = move[1];
	}

	@Override
	public Hashtable<String, Integer> getMoves() {
		return this.legalMoves;
	}

	@Override
	public int getAttack() {
		return this.attackValue;
	}

	@Override
	public void generateMoves(ArrayList<ArrayList<GamePiece>> board) {
		this.legalMoves.clear();
		int newRow = super.row;
		int newCol = super.col;
		if(player == 1) {
			//Check /^
			while(newRow - 1 >= 0 && newCol + 1 < MAX_WIDTH) {
				newRow--;
				newCol++;
				//if a piece is there, you can't go on.
				if(board.get(newRow).get(newCol) != null) {
					break;
				} else {
					String temp = "" + newRow + newCol;
					if(newRow - 1 >= 0 && board.get(newRow - 1).get(newCol) != null
							&& board.get(newRow - 1).get(newCol).player == 2) {
						GamePiece piece = board.get(newRow - 1).get(newCol);
						legalMoves.put(temp, piece.getAttack());
					} else {
						legalMoves.put(temp, 0);
					}
				}
			}
			//Check ^\
			newRow = super.row;
			newCol = super.col;
			while(newRow - 1 >= 0 && newCol - 1 >= 0) {
				newRow--;
				newCol--;
				if(board.get(newRow).get(newCol) != null) {
					break;
				} else {
					String temp = "" + newRow + newCol;
					if(newRow - 1 >= 0 && board.get(newRow - 1).get(newCol) != null
							&& board.get(newRow - 1).get(newCol).player == 2) {
						GamePiece piece = board.get(newRow - 1).get(newCol);
						legalMoves.put(temp, piece.getAttack());
					} else {
						legalMoves.put(temp, 0);
					}
				}
			}
			
			//Attack cases \v
			newRow = super.row;
			newCol = super.col;
			while(newRow + 1 < MAX_HEIGHT && newCol + 1 < MAX_WIDTH) {
				newRow++;
				newCol++;
				if(board.get(newRow).get(newCol) == null && board.get(newRow - 1).get(newCol) != null
						&& board.get(newRow - 1).get(newCol).player == 2) {
					String temp = "" + newRow + newCol;
					GamePiece piece = board.get(newRow - 1).get(newCol);
					legalMoves.put(temp, piece.getAttack());
				} else if(board.get(newRow).get(newCol) != null) {
					break;
				}
			}
			// Check Attack v/
			newRow = super.row;
			newCol = super.col;
			while(newRow + 1 < MAX_HEIGHT && newCol - 1 >= 0) {
				newRow++;
				newCol--;
				if(board.get(newRow).get(newCol) == null && board.get(newRow - 1).get(newCol) != null
						&& board.get(newRow - 1).get(newCol).player == 2) {
					String temp = "" + newRow + newCol;
					GamePiece piece = board.get(newRow - 1).get(newCol);
					legalMoves.put(temp, piece.getAttack());
				} else if(board.get(newRow).get(newCol) != null) {
					break;
				}
			}
			
			
		} else {
			//Check \v
			while(newRow + 1 < MAX_HEIGHT && newCol + 1 < MAX_WIDTH) {
				newRow++;
				newCol++;
				//if a piece is there, you can't go on.
				if(board.get(newRow).get(newCol) != null) {
					break;
				} else {
					String temp = "" + newRow + newCol;
					if(newRow + 1 < MAX_HEIGHT && board.get(newRow + 1).get(newCol) != null
							&& board.get(newRow + 1).get(newCol).player == 1) {
						GamePiece piece = board.get(newRow + 1).get(newCol);
						legalMoves.put(temp, piece.getAttack());
					} else {
						legalMoves.put(temp, 0);
					}
				}
			}
			newRow = super.row;
			newCol = super.col;
			//Check v/
			while(newRow + 1 < MAX_HEIGHT && newCol - 1 >= 0) {
				newRow++;
				newCol--;
				if(board.get(newRow).get(newCol) != null) {
					break;
				} else {
					String temp = "" + newRow + newCol;
					if(newRow + 1 < MAX_HEIGHT && board.get(newRow + 1).get(newCol) != null
							&& board.get(newRow + 1).get(newCol).player == 1) {
						GamePiece piece = board.get(newRow + 1).get(newCol);
						legalMoves.put(temp, piece.getAttack());
					} else {
						legalMoves.put(temp, 0);
					}
				}
			}
			
			//Attack cases ^\
			newRow = super.row;
			newCol = super.col;
			while(newRow - 1 >= 0 && newCol - 1 >= 0) {
				newRow--;
				newCol--;
				if(board.get(newRow).get(newCol) == null && board.get(newRow + 1).get(newCol) != null
						&& board.get(newRow + 1).get(newCol).player == 1) {
					String temp = "" + newRow + newCol;
					GamePiece piece = board.get(newRow + 1).get(newCol);
					legalMoves.put(temp, piece.getAttack());
				} else if(board.get(newRow).get(newCol) != null) {
					break;
				}
			}
			// Check /^
			newRow = super.row;
			newCol = super.col;
			while(newRow - 1 >= 0 && newCol + 1 < MAX_WIDTH) {
				newRow--;
				newCol++;
				if(board.get(newRow).get(newCol) == null && board.get(newRow + 1).get(newCol) != null
						&& board.get(newRow + 1).get(newCol).player == 1) {
					String temp = "" + newRow + newCol;
					GamePiece piece = board.get(newRow + 1).get(newCol);
					legalMoves.put(temp, piece.getAttack());
				} else if(board.get(newRow).get(newCol) != null) {
					break;
				}
			}
		}
		
	}

	@Override
	public Object clone() {
		Ninja p = new Ninja(super.player, super.row, super.col);
		return p;
	}
	
	public String toString() {
		return "" + character;
	}
}
