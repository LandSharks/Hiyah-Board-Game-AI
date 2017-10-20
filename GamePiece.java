import java.util.ArrayList;
import java.util.Hashtable;

public abstract class GamePiece {
	public int col;
	public int row;
	public int player; // 1 = HUMAN 2 = COM
	
	public final int MAX_HEIGHT = 8;
	public final int MAX_WIDTH = 7;
	
	public abstract void move(int[] move);
	public abstract Hashtable<String, Integer> getMoves();
	public abstract void generateMoves(ArrayList<ArrayList<GamePiece>> board);
	public abstract int getAttack();
}
