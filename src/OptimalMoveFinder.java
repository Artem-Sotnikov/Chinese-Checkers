import java.util.ArrayList;

public class OptimalMoveFinder {    
    MoveCode findBestMove(ArrayList<MoveCode> possibleMoves) {
        for (int i = 0; i < possibleMoves.size(); i++) {
            latitudeChecker(possibleMoves.get(i));
            enemyTerritoryChecker(possibleMoves.get(i));
            moveToCenter(possibleMoves.get(i));
            //nearbyPieceChecker(possibleMoves.get(i));
            prioritizeBack(possibleMoves.get(i));
        }

        int highestPriority = -2147483648;
        int bestMove = 0;
        for (int k = 0; k < possibleMoves.size(); k++) {
            if (possibleMoves.get(k).priority > highestPriority) {
                highestPriority = possibleMoves.get(k).priority;
                bestMove = k;
            }
        }

        return possibleMoves.get(bestMove);
    }

    void latitudeChecker(MoveCode possibleMove) {
      int moveLength = possibleMove.targetPosition.getRow() - possibleMove.startPosition.getRow();
      possibleMove.setPriority(moveLength);
    }

    void enemyTerritoryChecker(MoveCode possibleMove) {
        if ((possibleMove.targetPosition.getRow() > 11) && (possibleMove.targetPosition.getRow()) < 16 && (possibleMove.targetPosition.getColumn() < 4)) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.targetPosition.getRow() > 16 && possibleMove.targetPosition.getRow() < 21 && possibleMove.targetPosition.getColumn() > 12) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.targetPosition.getRow() == 12 && possibleMove.targetPosition.getColumn() > 8) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.targetPosition.getRow() == 13 && possibleMove.targetPosition.getColumn() > 9) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.targetPosition.getRow() == 14 && possibleMove.targetPosition.getColumn() > 10) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.targetPosition.getRow() == 15 && possibleMove.targetPosition.getColumn() > 11) {
            possibleMove.setPriority(-2);
        } else if (possibleMove.targetPosition.getRow() == 17 && possibleMove.targetPosition.getColumn() < 5) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.targetPosition.getRow() == 18 && possibleMove.targetPosition.getColumn() < 6) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.targetPosition.getRow() == 19 && possibleMove.targetPosition.getColumn() < 7) {
            possibleMove.setPriority(-1);
        } else if (possibleMove.targetPosition.getRow() == 20 && possibleMove.targetPosition.getColumn() < 8) {
            possibleMove.setPriority(-1);
        }
    }

    void moveToCenter(MoveCode possibleMove) {

    }

//    void nearbyPieceChecker(MoveCode possibleMove) {
//      if/*(one in front, next one empty)*/(){
//        possibleMove.setPriority(possibleMove.getPriority() + 1);
//      }else if(two or more block it in front){
//        possibleMove.setPriority(possibleMove.getPriority() - 1);
//      }else if(empty){
//        possibleMove.setPriority(possibleMove.getPriority() - 2);
//      }
//
//    }

    void prioritizeBack(MoveCode possibleMove) {
        int multiplier;
        multiplier = (24 - possibleMove.startPosition.getRow()) * 5;
        possibleMove.multiplyPriority(multiplier);
    }
}