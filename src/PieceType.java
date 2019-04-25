import java.awt.Color;

public class PieceType {
	public String team;
	public Color color;
	public int targetRegion;
	
	PieceType(String teamName, Color clr, int target) {
		this.team = teamName;
		this.color = clr;
		this.targetRegion = target;
	}
}
