
public class EvaluationEngine {
	public static double evaluate(Square[] positions, ArrayCoordinate primePosition) {
		double score = 0;
		
		for (int idx = 0; idx < 10; idx++) {
			double tempDeltaRow = Math.abs(primePosition.rowValue - positions[idx].boardLocation.rowValue);  
			double tempDeltaCol = Math.abs(primePosition.columnValue - positions[idx].boardLocation.columnValue);
			score =- (tempDeltaRow + tempDeltaCol);
		}
		
		
		
		return score;
	}
}
