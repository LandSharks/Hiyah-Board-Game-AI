import java.util.ArrayList;

public class MiniSamurai implements GamePiece, Cloneable{
	
	public int col;
	public int row;
	public int player; // 1 = HUMAN 2 = COM
	public char character;
	
	private ArrayList<int[]> legalMoves;
	
	public MiniSamurai(int player, int row, int col) {
		this.player = player;
		this.row = row;
		this.col = col;
		if(this.player == 1) {
			this.character = 's';
		} else {
			this.character = 'a';
		}
		legalMoves = new ArrayList<>();
	}
	
	
	@Override
	public void move(int[] move) {
		this.row = move[0];
		this.col = move[1];
	}
	
	@Override
	public ArrayList<int[]> getMoves() {
		return this.legalMoves;
	}


	@Override
	public void generateMoves(ArrayList<ArrayList<GamePiece>> board) {

	}

	//Checks if the move abides by pieces rules and doesn't collide with additional pieces
	@Override
	public boolean legal(int row, int col, ArrayList<GamePiece> gamePieces) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Object clone() {
		MiniSamurai p = new MiniSamurai(this.player, this.row, this.col);
		return p;
	}	
}
