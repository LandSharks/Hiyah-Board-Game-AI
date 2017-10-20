
public class GameMove {
	public GamePiece piece;
	public String moveKey;
	public int moveValue;
	
	public GameMove(GamePiece piece, String moveKey, int moveValue) {
		this.piece = piece;
		this.moveKey = moveKey;
		this.moveValue = moveValue;
	}
}
