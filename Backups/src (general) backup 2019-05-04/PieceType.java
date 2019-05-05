import java.awt.Color;

public class PieceType {
	public String team;
	public Color color;
	public int targetRegion;	
	public int teamCode;
	
	PieceType(String teamName, Color clr, int target, int code) {
		this.team = teamName;
		this.color = clr;
		this.targetRegion = target;
		this.teamCode = code;
	}
}
