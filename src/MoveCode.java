public class MoveCode {
	public ArrayCoordinate startPosition;
	public ArrayCoordinate targetPosition;
	public int priority;
	
	MoveCode(){};
	
	MoveCode(int a, int b, int p, int q) {
		this.startPosition = new ArrayCoordinate(a,b);
		this.targetPosition = new ArrayCoordinate(p,q);
	}
	
	MoveCode(ArrayCoordinate start, ArrayCoordinate end) {
		this.startPosition = start;
		this.targetPosition = end;
	}

    public void setPriority(int priority){
        this.priority += priority;
    }
    public void multiplyPriority(int priority) {
        this.priority *= priority;
    }
    public double getPriority(){
        return this.priority;
    }
}
