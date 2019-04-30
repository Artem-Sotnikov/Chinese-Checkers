import java.util.ArrayList;

public class OptimalMoveFinder {    
    ArrayCoordinate findBestMove(ArrayList<ArrayCoordinate> possibleMoves, ArrayCoordinate originalPosition) {
        for (int i = 0; i < possibleMoves.size(); i++) {
            latitudeChecker(possibleMoves.get(i), originalPosition);      
            enemyTerritoryChecker(possibleMoves.get(i));
            moveToCenter(possibleMoves.get(i), originalPosition);
            nearbyPieceChecker(possibleMoves.get(i), originalPosition);
            prioritizeBack(possibleMoves.get(i), originalPosition);
        }
    }

    void latitudeChecker(ArrayCoordinate possibleMove, ArrayCoordinate originalPosition) {
      int moveLength = possibleMove.getRow() - originalPosition.getRow();
      possibleMove.setPriority(moveLength);
    }

    void enemyTerritoryChecker(ArrayCoordinate possibleMove) {
        if ((possibleMove.getRow() > 11) && (possibleMove.getRow()) < 16 && (possibleMove.getColumn() < 4)) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.getRow() > 16 && possibleMove.getRow() < 21 && possibleMove.getColumn() > 12) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.getRow() == 12 && possibleMove.getColumn() > 8) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.getRow() == 13 && possibleMove.getColumn() > 9) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.getRow() == 14 && possibleMove.getColumn() > 10) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.getRow() == 15 && possibleMove.getColumn() > 11) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.getRow() == 17 && possibleMove.getColumn() < 5) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.getRow() == 18 && possibleMove.getColumn() < 6) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.getRow() == 19 && possibleMove.getColumn() < 7) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.getRow() == 20 && possibleMove.getColumn() < 8) {
            possibleMove.setPriority(-1);
        }
    }

    void moveToCenter(ArrayCoordinate possibleMove, ArrayCoordinate originalPosition) {

    }

    void nearbyPieceChecker(ArrayCoordinate possibleMove, ArrayCoordinate originalPosition) {
      if/*(one in front, next one empty)*/(){
        possibleMove.setPriority(possibleMove.getPriority() + 1);
      }else if(two or more block it in front){
        possibleMove.setPriority(possibleMove.getPriority() - 1);
      }else if(empty){
        possibleMove.setPriority(possibleMove.getPriority() - 2);
      }
      
    }

    void prioritizeBack(ArrayCoordinate possibleMove, ArrayCoordinate originalPosition) {
        int multiplier;
        multiplier = (24 - originalPosition.getRow()) * 5;
        possibleMove.multiplyPriority(multiplier);
    }
}