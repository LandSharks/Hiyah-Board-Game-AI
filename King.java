import java.util.ArrayList;

public class King implements Cloneable, GamePiece{

	public int col;
	public int row;
	public int player; // 1 = HUMAN 2 = COM
	public char character;
	public int attackValue;
	
	public King(int player, int row, int col) {
		this.player = player;
		this.row = row;
		this.col = col;
		this.character = 'K';
		if(player == 1) {
			attackValue = Integer.MAX_VALUE;
		} else {
			attackValue = Integer.MIN_VALUE;
		}
	}
	
	@Override
	public void move(int[] move) {
		System.out.println("Error the king cannot move");
	}

	@Override
	public ArrayList<int[]> getMoves() {
		return null;
	}


	@Override
	public void generateMoves(ArrayList<ArrayList<GamePiece>> board) {

	}

	//King cannot do anything. Special case.
	@Override
	public boolean legal(int row, int col, ArrayList<GamePiece> gamePieces) {
		return false;
	}
	
	@Override
	public Object clone() {
		King p = new King(this.player, this.row, this.col);
		return p;
	}
}
