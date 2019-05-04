import java.util.ArrayList;
import java.util.Arrays;


class MoveNode {
	public ArrayCoordinate position;
	public ArrayList<MoveNode> branches;	
	public boolean isRoot;
	
	MoveNode(ArrayCoordinate source) {
		this.position = source;
		branches = new ArrayList<MoveNode>(0);
	}
	
	MoveNode(int row, int col) {
		this.position = new ArrayCoordinate(row, col);
		branches = new ArrayList<MoveNode>(0);
	};
	
	MoveNode(){ branches = new ArrayList<MoveNode>(0); };
	
	public static ArrayCoordinate[] concat(ArrayCoordinate[] first, ArrayCoordinate[] second) {
		  ArrayCoordinate[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
		}
	
	public ArrayList<ArrayCoordinate> returnAsArray() {	
		ArrayList<ArrayCoordinate> returnList = new ArrayList<ArrayCoordinate>(0);
		
		if (!this.isRoot) {
			returnList.add(this.position);
		}
		
		for (int i = 0; i < branches.size(); i++) {				
			returnList.addAll(branches.get(i).returnAsArray());
		}			
		
		
		return returnList;
	}
	
	public ArrayList<MoveCode> toMoveCodes() {
		
		ArrayList<ArrayCoordinate> finalCoords = this.returnAsArray();
		ArrayList<MoveCode> returnList = new ArrayList<MoveCode>();
		
		for (int idx = 0; idx < finalCoords.size(); idx++) {
			returnList.add(new MoveCode(this.position,finalCoords.get(idx)));
		}
		
		return returnList;
	}
}