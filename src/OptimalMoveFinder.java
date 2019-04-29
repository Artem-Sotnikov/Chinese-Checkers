import java.lang.reflect.Array;
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

    }

    void moveToCenter(ArrayCoordinate possibleMove, ArrayCoordinate originalPosition) {

    }

    void nearbyPieceChecker(ArrayCoordinate possibleMove, ArrayCoordinate originalPosition) {

    }

    void prioritizeBack(ArrayCoordinate originalPosition) {
        originalPosition.getRowValue();
    }
}
