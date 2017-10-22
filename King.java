import java.util.ArrayList;
import java.util.Hashtable;

public class King extends GamePiece implements Cloneable{

	public char character;
	public int attackValue;
	
	public King(int player, int row, int col) {
		super.player = player;
		super.row = row;
		super.col = col;
		this.character = 'K';
		if(player == 1) {
			attackValue = -10000;
		} else {
			attackValue = 10000;
		}
	}
	
	
	@Override
	public void move(int[] move) {
		System.out.println("Error the king cannot move");
	}

	@Override
	public Hashtable<String, Integer> getMoves() {
		return null;
	}

	@Override
	public int getAttack() {
		return this.attackValue;
	}

	@Override
	public void generateMoves(ArrayList<ArrayList<GamePiece>> board) {

	}
	
	@Override
	public Object clone() {
		King p = new King(this.player, this.row, this.col);
		return p;
	}
	
	public String toString() {
		return "" + character;
	}
}
