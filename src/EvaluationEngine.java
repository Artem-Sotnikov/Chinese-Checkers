
public class EvaluationEngine {
	public double evaluateBasic(Square[] positions, ArrayCoordinate primePosition, int subjectCode) {
		double score = 0;
		
		for (int idx = 0; idx < 10; idx++) {
			double tempDeltaRow = Math.abs(primePosition.row - positions[idx].boardLocation.row);  
			double tempDeltaCol = Math.abs(primePosition.column - positions[idx].boardLocation.column);
			score = score - (tempDeltaRow + tempDeltaCol);
		}
		
		if (subjectCode == 1 || subjectCode == 4) {
			score -= 70;
		}
		
		return score;
	}
}
