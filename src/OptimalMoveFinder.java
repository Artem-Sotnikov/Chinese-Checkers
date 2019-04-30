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
      int moveLength = possibleMove.targetPosition.row - possibleMove.startPosition.row;
      possibleMove.setPriority(moveLength);
    }

    void enemyTerritoryChecker(MoveCode possibleMove) {
        if ((possibleMove.targetPosition.row > 11) && (possibleMove.targetPosition.row) < 16 && (possibleMove.targetPosition.column < 4)) {
            possibleMove.setPriority(-50000);
        } else if (possibleMove.targetPosition.row > 16 && possibleMove.targetPosition.row < 21 && possibleMove.targetPosition.column > 12) {
            possibleMove.setPriority(-50000);
        } else if (possibleMove.targetPosition.row == 12 && possibleMove.targetPosition.column > 8) {
            possibleMove.setPriority(-50000);
        } else if (possibleMove.targetPosition.row == 13 && possibleMove.targetPosition.column > 9) {
            possibleMove.setPriority(-50000);
        } else if (possibleMove.targetPosition.row == 14 && possibleMove.targetPosition.column > 10) {
            possibleMove.setPriority(-50000);
        } else if (possibleMove.targetPosition.row == 15 && possibleMove.targetPosition.column > 11) {
            possibleMove.setPriority(-50000);
        } else if (possibleMove.targetPosition.row == 17 && possibleMove.targetPosition.column < 5) {
            possibleMove.setPriority(-50000);
        } else if (possibleMove.targetPosition.row == 18 && possibleMove.targetPosition.column < 6) {
            possibleMove.setPriority(-50000);
        } else if (possibleMove.targetPosition.row == 19 && possibleMove.targetPosition.column < 7) {
            possibleMove.setPriority(-50000);
        } else if (possibleMove.targetPosition.row == 20 && possibleMove.targetPosition.column < 8) {
            possibleMove.setPriority(-50000);
        }
    }

    void moveToCenter(MoveCode possibleMove) {
        if (possibleMove.startPosition.column > possibleMove.targetPosition.column && possibleMove.targetPosition.column > (possibleMove.targetPosition.row / 2) ) {
            possibleMove.setPriority(1);
        } else if (possibleMove.startPosition.column < possibleMove.targetPosition.column && possibleMove.targetPosition.column < (possibleMove.targetPosition.row / 2)) {
            possibleMove.setPriority(1);
        }
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
        multiplier = (24 - possibleMove.startPosition.row) * 5;
        possibleMove.multiplyPriority(multiplier);
    }
}