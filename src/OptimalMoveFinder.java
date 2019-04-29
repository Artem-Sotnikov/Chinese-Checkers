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
      int moveLength = possibleMove.getRow() - originalPosition.getRow();
      possibleMove.setPriority(moveLength);
    }

    void enemyTerritoryChecker(ArrayCoordinate possibleMove) {

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

    void prioritizeBack(ArrayCoordinate originalPosition) {
        originalPosition.getRowValue();
    }
}