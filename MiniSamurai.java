import java.util.ArrayList;
import java.util.Hashtable;

public class MiniSamurai extends GamePiece implements Cloneable{
	

	public char character;
	public int attackValue;
	
	private Hashtable<String, Integer> legalMoves;
	
	public MiniSamurai(int player, int row, int col) {
		super.player = player;
		super.row = row;
		super.col = col;
		if(player == 1) {
			this.character = 's';
			this.attackValue = 1001;
		} else {
			this.character = 'a';
			this.attackValue = -1005;
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
		if(player == 1) {
			//UP
			if( newRow - 1 >= 0 && board.get(newRow - 1).get(newCol) == null) {
				String temp = "" + (newRow - 1) + newCol;
				if(newRow - 2 >= 0 && board.get(newRow - 2).get(newCol) != null && board.get(newRow - 2).get(newCol).player == 2) {
					GamePiece piece = board.get(newRow - 2).get(newCol);
					legalMoves.put(temp, piece.getAttack());
				} else {
					legalMoves.put(temp, 0);
				}
			}
			//Check -> Attacks
			if(newCol + 1 < MAX_WIDTH && newRow - 1 >= 0 && board.get(newRow).get(newCol + 1) == null 
					&& board.get(newRow - 1).get(newCol + 1) != null && board.get(newRow - 1).get(newCol + 1).player == 2) {
					
				String temp = "" + newRow + (newCol + 1);
				GamePiece piece = board.get(newRow - 1).get(newCol + 1);
				legalMoves.put(temp, piece.getAttack());
			}
			//Check <- Attacks
			if(newCol - 1 >= 0 && newRow - 1 >= 0 && board.get(newRow).get(newCol - 1) == null 
					&& board.get(newRow - 1).get(newCol - 1) != null && board.get(newRow - 1).get(newCol - 1).player == 2) {
					
				String temp = "" + newRow + (newCol - 1);
				GamePiece piece = board.get(newRow - 1).get(newCol - 1);
				legalMoves.put(temp, piece.getAttack());
			}
		} else {
			//DOWN
			if( newRow + 1 < MAX_HEIGHT && board.get(newRow + 1).get(newCol) == null) {
				String temp = "" + (newRow + 1) + newCol;
				if(newRow + 2 < MAX_HEIGHT && board.get(newRow + 2).get(newCol) != null && board.get(newRow + 2).get(newCol).player == 1) {
					GamePiece piece = board.get(newRow + 2).get(newCol);
					legalMoves.put(temp, piece.getAttack());
				} else {
					legalMoves.put(temp, 0);
				}
			}
			//Check -> Attacks
			if(newCol + 1 < MAX_WIDTH && newRow + 1 < MAX_HEIGHT && board.get(newRow).get(newCol + 1) == null 
					&& board.get(newRow + 1).get(newCol + 1) != null && board.get(newRow + 1).get(newCol + 1).player == 1) {
					
				String temp = "" + newRow + (newCol + 1);
				GamePiece piece = board.get(newRow + 1).get(newCol + 1);
				legalMoves.put(temp, piece.getAttack());
			}
			//Check <- Attacks
			if(newCol - 1 >= 0 && newRow + 1 < MAX_HEIGHT && board.get(newRow).get(newCol - 1) == null 
					&& board.get(newRow + 1).get(newCol - 1) != null && board.get(newRow + 1).get(newCol - 1).player == 1) {
					
				String temp = "" + newRow + (newCol - 1);
				GamePiece piece = board.get(newRow + 1).get(newCol - 1);
				legalMoves.put(temp, piece.getAttack());
			}
		}
	}
	
	@Override
	public Object clone() {
		MiniSamurai p = new MiniSamurai(super.player, super.row, super.col);
		return p;
	}	
	
	public String toString() {
		return "" + character;
	}
}
