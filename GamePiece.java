import java.util.ArrayList;

public interface GamePiece {
	public void move(int[] move);
	public boolean legal(int row, int col, ArrayList<GamePiece> objects);
	public ArrayList<int[]> getMoves();
	public void generateMoves(ArrayList<ArrayList<GamePiece>> board);
}
