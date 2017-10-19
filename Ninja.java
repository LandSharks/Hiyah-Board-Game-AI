import java.util.ArrayList;
import java.util.Hashtable;

public class Ninja implements GamePiece, Cloneable {
	
	public int col;
	public int row;
	public int player; // 1 = HUMAN 2 = COM
	public char character;
	public int attackValue;
	
	private final int MAX_HEIGHT = 8;
	private final int MAX_WIDTH = 7;
	
	//Potentially use a hashtable to track both legal moves AND the value behind them?
	private Hashtable<int[], Integer> moves;
	
	private ArrayList<int[]> legalMoves;
	
	public Ninja(int player, int row, int col) {
		this.player = player;
		this.row = row;
		this.col = col;
		if(this.player == 1) {
			this.character = 'N';
			this.attackValue = 4000;
		} else {
			this.character = 'I';
			this.attackValue = -4000;
		}
		
		legalMoves = new ArrayList<>();
	}
	
	
	@Override
	public void move(int[] move) {
		this.row = move[0];
		this.col = move[1];
		legalMoves.clear();
	}

	@Override
	public ArrayList<int[]> getMoves() {
		return this.legalMoves;
	}


	@Override
	public void generateMoves(ArrayList<ArrayList<GamePiece>> board) {
		int newRow = this.row;
		int newCol = this.col;
		if(player == 1) {
			//Check \
			while(newRow + 1 < MAX_HEIGHT && newCol + 1 < MAX_WIDTH) {
				newRow++;
				newCol++;
				//if a piece is there, you can't go on.
				if(board.get(row).get(col) != null) {
					break;
				} else {
					int[] temp = new int[2];
					temp[0] = newRow;
					temp[1] = newCol;
					legalMoves.add(temp);
				}
			}
			//Check /
			while(newRow - 1 >= 0 && newCol + 1 < MAX_WIDTH) {
				newRow--;
				newCol++;
				if(board.get(row).get(col) != null) {
					break;
				} else {
					int[] temp = new int[2];
					temp[0] = newRow;
					temp[1] = newCol;
					legalMoves.add(temp);
				}
			}
			
			//Come back later to backwards attacks
		} else {
			//Check \
			while(newRow + 1 < MAX_HEIGHT && newCol + 1 < MAX_WIDTH) {
				newRow++;
				newCol++;
				//if a piece is there, you can't go on.
				if(board.get(row).get(col) != null) {
					break;
				} else {
					int[] temp = new int[2];
					temp[0] = newRow;
					temp[1] = newCol;
					legalMoves.add(temp);
				}
			}
			//Check /
			while(newRow - 1 >= 0 && newCol + 1 < MAX_WIDTH) {
				newRow--;
				newCol++;
				if(board.get(row).get(col) != null) {
					break;
				} else {
					int[] temp = new int[2];
					temp[0] = newRow;
					temp[1] = newCol;
					legalMoves.add(temp);
				}
			}
			
			//Come back later to backwards attacks
		}
		
	}

	//May be obsolete with generate move? we can just ensure the move selected is within the table or piece.
	@Override
	public boolean legal(int row, int col, ArrayList<GamePiece> gamePieces) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object clone() {
		Ninja p = new Ninja(this.player, this.row, this.col);
		return p;
	}
}
