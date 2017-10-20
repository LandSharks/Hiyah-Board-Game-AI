import java.util.ArrayList;
import java.util.Hashtable;

public class MiniNinja extends GamePiece implements Cloneable {
	
	public char character;
	public int attackValue;
	
	private Hashtable<String, Integer> legalMoves;
	
	public MiniNinja(int player, int row, int col) {
		super.player = player;
		super.row = row;
		super.col = col;
		if(player == 1) {
			this.character = 'n';
			this.attackValue = 3000;
		} else {
			this.character = 'i';
			this.attackValue = -3000;
		}
		legalMoves = new Hashtable<>();
	}
	
	
	@Override
	public void move(int[] move) {
		this.row = move[0];
		this.col = move[1];
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
		//Potential out of bounds issues, come back later
		if(player == 1) {
			//Check /^
			if(newRow - 1 >= 0 && newCol + 1 < MAX_WIDTH && board.get(newRow - 1).get(newCol + 1) == null) {
				String temp = "" + (newRow - 1) + (newCol + 1);
				if(newRow - 2 >= 0 && board.get(newRow - 2).get(newCol + 1) != null 
						&& board.get(newRow - 2).get(newCol + 1).player == 2) {
					GamePiece piece = board.get(newRow - 2).get(newCol + 1);
					legalMoves.put(temp, piece.getAttack());
				} else {
					legalMoves.put(temp, 0);
				}
			}
			//Check ^\
			if(newRow - 1 >= 0 && newCol - 1 >= 0 && board.get(newRow - 1).get(newCol - 1) == null) {
				String temp = "" + (newRow - 1) + (newCol - 1);
				if(newRow - 2 >= 0 && board.get(newRow - 2).get(newCol - 1) != null 
						&& board.get(newRow - 2).get(newCol - 1).player == 2) {
					GamePiece piece = board.get(newRow - 2).get(newCol - 1);
					legalMoves.put(temp, piece.getAttack());
				} else {
					legalMoves.put(temp, 0);
				}
			}
			
			//Attack cases \v
			if(newRow + 1 < MAX_HEIGHT && newCol + 1 < MAX_WIDTH && board.get(newRow + 1).get(newCol + 1) == null && board.get(newRow).get(newCol + 1) != null
					&& board.get(newRow).get(newCol + 1).player == 2) {
				String temp = "" + (newRow + 1) + (newCol + 1);
				GamePiece piece = board.get(newRow).get(newCol + 1);
				legalMoves.put(temp, piece.getAttack());
			}
			
			// Check Attack v/
			if(newRow + 1 < MAX_HEIGHT && newCol - 1 >= 0 && board.get(newRow + 1).get(newCol - 1) == null && board.get(newRow).get(newCol - 1) != null
					&& board.get(newRow).get(newCol - 1).player == 2) {
				String temp = "" + (newRow + 1) + (newCol - 1);
				GamePiece piece = board.get(newRow).get(newCol - 1);
				legalMoves.put(temp, piece.getAttack());
			}
			
		} else {
			//Check Attack /^
			if(newRow - 1 >= 0 && newCol + 1 < MAX_WIDTH && board.get(newRow - 1).get(newCol + 1) == null  && board.get(newRow).get(newCol + 1) != null
					&& board.get(newRow).get(newCol + 1).player == 1) {
				String temp = "" + (newRow - 1) + (newCol + 1);
				GamePiece piece = board.get(newRow).get(newCol + 1);
				legalMoves.put(temp, piece.getAttack());
			}
			//Check Attack ^\
			if(newRow - 1 >= 0 && newCol - 1 >= 0 && board.get(newRow - 1).get(newCol - 1) == null  && board.get(newRow).get(newCol - 1) != null
					&& board.get(newRow).get(newCol - 1).player == 1) {
				String temp = "" + (newRow - 1) + (newCol - 1);
				GamePiece piece = board.get(newRow).get(newCol - 1);
				legalMoves.put(temp, piece.getAttack());
			}
			
			//Check \v
			if(newRow + 1 < MAX_HEIGHT && newCol + 1 < MAX_WIDTH && board.get(newRow + 1).get(newCol + 1) == null) {
				String temp = "" + (newRow + 1) + (newCol + 1);
				if(newRow + 2 < MAX_HEIGHT && board.get(newRow + 2).get(newCol + 1) != null 
						&& board.get(newRow + 2).get(newCol + 1).player == 1) {
					GamePiece piece = board.get(newRow + 2).get(newCol + 1);
					legalMoves.put(temp, piece.getAttack());
				} else {
					legalMoves.put(temp, 0);
				}
			}
			
			// Check v/
			if(newRow + 1 < MAX_HEIGHT && newCol - 1 >= 0 && board.get(newRow + 1).get(newCol - 1) == null) {
				String temp = "" + (newRow + 1) + (newCol - 1);
				if(newRow + 2 < MAX_HEIGHT && board.get(newRow + 2).get(newCol - 1) != null 
						&& board.get(newRow + 2).get(newCol - 1).player == 1) {
					GamePiece piece = board.get(newRow + 2).get(newCol - 1);
					legalMoves.put(temp, piece.getAttack());
				} else {
					legalMoves.put(temp, 0);
				}
			}
		}
	}

	@Override
	public Object clone() {
		MiniNinja p = new MiniNinja(super.player, super.row, super.col);
		return p;
	}
	
	public String toString() {
		return "" + character;
	}
}
