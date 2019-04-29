import java.util.ArrayList;

public class OptimalMoveFinder {

    ArrayCoordinate findBestMove(ArrayList<ArrayCoordinate> possibleMoves, ArrayCoordinate originalPosition) {
        for (int i = 0; i < possibleMoves.size(); i++) {
            latitudeChecker(possibleMoves.get(i), originalPosition);
            enemyTerritoryChecker(possibleMoves.get(i));
            moveToCenter(possibleMoves.get(i), originalPosition);
            nearbyPieceChecker(possibleMoves.get(i), originalPosition);
            prioritizeBack(originalPosition);
        }
    }

    void latitudeChecker(ArrayCoordinate possibleMove, ArrayCoordinate originalPosition) {

    }

    void enemyTerritoryChecker(ArrayCoordinate possibleMove) {
        if ((possibleMove.getRowValue() > 11) && (possibleMove.getRowValue()) < 16 && (possibleMove.getColumnValue() < 4)) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.getRowValue() > 16 && possibleMove.getRowValue() < 21 && possibleMove.getColumnValue() > 12) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.getRowValue() == 12 && possibleMove.getColumnValue() > 8) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.getRowValue() == 13 && possibleMove.getColumnValue() > 9) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.getRowValue() == 14 && possibleMove.getColumnValue() > 10) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.getRowValue() == 15 && possibleMove.getColumnValue() > 11) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.getRowValue() == 17 && possibleMove.getColumnValue() < 5) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.getRowValue() == 18 && possibleMove.getColumnValue() < 6) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.getRowValue() == 19 && possibleMove.getColumnValue() < 7) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.getRowValue() == 20 && possibleMove.getColumnValue() < 8) {
            possibleMove.setPriority(-1);
        }
    }

    void moveToCenter(ArrayCoordinate possibleMove, ArrayCoordinate originalPosition) {

    }

    void nearbyPieceChecker(ArrayCoordinate possibleMove, ArrayCoordinate originalPosition) {

    }

    void prioritizeBack(ArrayCoordinate possibleMove, ArrayCoordinate originalPosition) {
        int multiplier;
        multiplier = (24 - originalPosition.getRowValue()) * 5;
        possibleMove.multiplyPriority(multiplier);
    }
}