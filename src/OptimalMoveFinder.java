import java.util.ArrayList;

public class OptimalMoveFinder {

    double determinePriority(ArrayList<ArrayCoordinate> moves, double highestPriority) {
        //double highestPriority = -1000000000;
        for(int i=0;i<moves.size();i++){
            if (moves.get(i).getPriority() > highestPriority) {
                highestPriority = moves.get(i).getPriority();
            }
        }
        return highestPriority;
    }

    ArrayCoordinate determineBestMove(ArrayList<ArrayCoordinate> moves, double highestPriority){
        ArrayCoordinate bestCoordinate = null;
        for(int i=0;i<moves.size();i++){
            if (moves.get(i).getPriority() == highestPriority) {
                bestCoordinate = moves.get(i);
            }
        }
        return bestCoordinate;
    }

    double latitudeChecker(ArrayCoordinate originalPosition, ArrayList<ArrayCoordinate> playerMoves, ArrayList<ArrayCoordinate> enemyPositions, ArrayCoordinate goalPosition, int startPosition) {
        int farthestLatitude = 0;
        if(startPosition == 1){
            for (int i = 0; i < playerMoves.size(); i++){
                if (originalPosition.rowValue - playerMoves.get(i).rowValue > farthestLatitude) {
                    farthestLatitude = originalPosition.rowValue - playerMoves.get(i).rowValue;
                }
            }
        }else if(startPosition == 2){
            for (int i = 0; i < playerMoves.size(); i++){
                if (originalPosition.rowValue - playerMoves.get(i).rowValue > farthestLatitude) {
                    farthestLatitude = originalPosition.rowValue - playerMoves.get(i).rowValue;
                }
            }
        }else if(startPosition == 3){
            for (int i = 0; i < playerMoves.size(); i++){

            }
        }else if(startPosition == 4){
            for (int i = 0; i < playerMoves.size(); i++){

            }
        }else if(startPosition == 5){
            for (int i = 0; i < playerMoves.size(); i++){

            }
        }else if(startPosition == 6){
            for (int i = 0; i < playerMoves.size(); i++){

            }
        }
    }

    double enemyTerritoryChecker(ArrayList<ArrayCoordinate> playerMoves, ArrayList<ArrayCoordinate> enemyPosition, int startPosition) {
        if(startPosition == 1){

        }else if(startPosition == 2){

        }else if(startPosition == 3){

        }else if(startPosition == 4){

        }else if(startPosition == 5){

        }else if(startPosition == 6){

        }
    }

    double moveToCenter(ArrayList<ArrayCoordinate> playerMoves, ArrayList<ArrayCoordinate> enemyPositions, int startPosition) {
        if(startPosition == 1){

        }else if(startPosition == 2){

        }else if(startPosition == 3){

        }else if(startPosition == 4){

        }else if(startPosition == 5){

        }else if(startPosition == 6){

        }
    }

    double nearbyPieceChecker(ArrayList<ArrayCoordinate> playerMoves, ArrayList<ArrayCoordinate> enemyPositions, int startPosition) {
        if(startPosition == 1){

        }else if(startPosition == 2){

        }else if(startPosition == 3){

        }else if(startPosition == 4){

        }else if(startPosition == 5){

        }else if(startPosition == 6){

        }
    }

    double prioritizeBack(ArrayList<ArrayCoordinate> playerMoves, ArrayList<ArrayCoordinate> enemyPositions, int startPosition) {
        if(startPosition == 1){

        }else if(startPosition == 2){

        }else if(startPosition == 3){

        }else if(startPosition == 4){

        }else if(startPosition == 5){

        }else if(startPosition == 6){

        }
    }
}
