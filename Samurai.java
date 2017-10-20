import java.util.ArrayList;
import java.util.Hashtable;

public class Samurai extends GamePiece implements Cloneable {


	public char character;
	public int attackValue;
	
	private Hashtable<String, Integer> legalMoves;
	
	public Samurai(int player, int row, int col) {
		super.player = player;
		super.row = row;
		super.col = col;
		if(player == 1) {
			this.character = 'S';
			this.attackValue = 2000;
		} else {
			this.character = 'A';
			this.attackValue = -2000;
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
		int newRow = super.row;
		int newCol = super.col;
		this.legalMoves.clear();
		if(player == 1) {
			//Check UP direction
			while(newRow - 1 >= 0) {
				newRow--;
				if(board.get(newRow).get(newCol) != null) {
					if(board.get(newRow).get(newCol).player == 2) {
						GamePiece piece = board.get(newRow).get(newCol);
						legalMoves.put("" + (newRow + 1) + newCol, piece.getAttack());
					}
					break;
				} else {
					String temp = "" + newRow + newCol;
					legalMoves.put(temp, 0);
				}
			}
			//Check <- Attacks
			newRow = super.row;
			while(newCol - 1 >= 0 && newRow - 1 >= 0) {
				newCol--;
				if(board.get(newRow).get(newCol) == null && board.get(newRow - 1).get(newCol) != null && board.get(newRow - 1).get(newCol).player == 2) {
					String temp = "" + newRow + newCol;
					GamePiece piece = board.get(newRow - 1).get(newCol);
					legalMoves.put(temp, piece.getAttack());
				} else if(board.get(newRow).get(newCol) != null) {
					break;
				}
			}
			//Check -> Attacks
			newCol = super.col;
			while(newCol + 1 < MAX_WIDTH && newRow - 1 >= 0) {
				newCol++;
				if(board.get(newRow).get(newCol) == null && board.get(newRow - 1).get(newCol) != null && board.get(newRow - 1).get(newCol).player == 2) {
					String temp = "" + newRow + newCol;
					GamePiece piece = board.get(newRow - 1).get(newCol);
					legalMoves.put(temp, piece.getAttack());
				} else if(board.get(newRow).get(newCol) != null) {
					break;
				}
			}
		} else {
			//Check DOWN direction
			while(newRow + 1 < MAX_HEIGHT) {
				newRow++;
				if(board.get(newRow).get(newCol) != null) {
					if(board.get(newRow).get(newCol).player == 1) {
						GamePiece piece = board.get(newRow).get(newCol);
						legalMoves.put("" + (newRow - 1) + newCol, piece.getAttack());
					}
					break;
				} else {
					String temp = "" + newRow + newCol;
					legalMoves.put(temp, 0);
				}
			}
			//Check <- Attacks
			newRow = super.row;
			while(newCol - 1 >= 0 && newRow + 1 < MAX_HEIGHT) {
				newCol--;
				if(board.get(newRow).get(newCol) == null && board.get(newRow + 1).get(newCol) != null && board.get(newRow + 1).get(newCol).player == 1) {
					String temp = "" + newRow + newCol;
					GamePiece piece = board.get(newRow + 1).get(newCol);
					legalMoves.put(temp, piece.getAttack());
				} else if(board.get(newRow).get(newCol) != null) {
					break;
				}
			}
			//Check -> Attacks
			newCol = super.col;
			while(newCol + 1 < MAX_WIDTH && newRow + 1 < MAX_HEIGHT) {
				newCol++;
				if(board.get(newRow).get(newCol) == null && board.get(newRow + 1).get(newCol) != null && board.get(newRow + 1).get(newCol).player == 1) {
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
		Samurai p = new Samurai(super.player, super.row, super.col);
		return p;
	}
	
	public String toString() {
		return "" + character;
	}
}
