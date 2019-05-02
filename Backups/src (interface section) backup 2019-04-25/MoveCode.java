
public class MoveCode {
	public ArrayCoordinate startPosition;
	public ArrayCoordinate targetPosition;
	
	MoveCode(){};
	
	MoveCode(int a, int b, int p, int q) {
		this.startPosition = new ArrayCoordinate(a,b);
		this.targetPosition = new ArrayCoordinate(p,q);
	}
}
