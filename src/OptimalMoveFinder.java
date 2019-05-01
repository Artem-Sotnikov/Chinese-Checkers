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

//    void nearbyPieceChecker(MoveCode possibleMove, ArrayCoordinate[] occupiedSpaces, Square[][] squares) {
//        for (int i=0;i<25;i++){
//            for (int j=0;j<25;j++){
//                if (((possibleMove.targetPosition.row != 8) && (possibleMove.targetPosition.column != 4)) && ((possibleMove.targetPosition.row != 24) && (possibleMove.targetPosition.column != 12))){
//                    if ((squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column-1] == null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column+1] != null) && (squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column+1] != null)){
//                        ArrayCoordinate nextC1 = new ArrayCoordinate((possibleMove.targetPosition.row + 1), (possibleMove.targetPosition.column));
//                        ArrayCoordinate nextC2 = new ArrayCoordinate((possibleMove.targetPosition.row + 1), (possibleMove.targetPosition.column + 1));
//                        ArrayCoordinate nextC3 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column + 1));
//                        if ((contains(occupiedSpaces, nextC1)) || ((contains(occupiedSpaces, nextC2))) || ((contains(occupiedSpaces, nextC3)))){
//                            possibleMove.setPriority(1);
//                        }
//                    } else if ((squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column-1] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column+1] != null) && (squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column+1] == null)){
//                        ArrayCoordinate nextC1 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column - 1));
//                        ArrayCoordinate nextC2 = new ArrayCoordinate((possibleMove.targetPosition.row + 1), (possibleMove.targetPosition.column));
//                        ArrayCoordinate nextC3 = new ArrayCoordinate((possibleMove.targetPosition.row + 1), (possibleMove.targetPosition.column + 1));
//                        if ((contains(occupiedSpaces, nextC1)) || ((contains(occupiedSpaces, nextC2))) || ((contains(occupiedSpaces, nextC3)))){
//                            possibleMove.setPriority(1);
//                        }
//                    } else if ((squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column-1] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column+1] == null) && (squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column+1] == null)){
//                        ArrayCoordinate nextC1 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column - 1));
//                        ArrayCoordinate nextC2 = new ArrayCoordinate((possibleMove.targetPosition.row - 1), (possibleMove.targetPosition.column));
//                        if ((contains(occupiedSpaces, nextC1)) || ((contains(occupiedSpaces, nextC2)))){
//                            possibleMove.setPriority(1);
//                        }
//                    } else if ((squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column-1] == null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column] == null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column+1] != null) && (squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column+1] != null)){
//                        ArrayCoordinate nextC1 = new ArrayCoordinate((possibleMove.targetPosition.row + 1), (possibleMove.targetPosition.column + 1));
//                        ArrayCoordinate nextC2 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column + 1));
//                        if ((contains(occupiedSpaces, nextC1)) || ((contains(occupiedSpaces, nextC2))) || ((contains(occupiedSpaces, nextC3)))){
//                            possibleMove.setPriority(1);
//                        }
//                    } else if ((squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column-1] == null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column] == null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column+1] == null) && (squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column+1] != null)){
//                        ArrayCoordinate nextC1 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column + 1));
//                        if ((contains(occupiedSpaces, nextC1))){
//                            possibleMove.setPriority(1);
//                        }
//                    } else if ((squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column-1] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column] == null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column+1] == null) && (squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column+1] == null)){
//                        ArrayCoordinate nextC1 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column - 1));
//                        if ((contains(occupiedSpaces, nextC1))){
//                            possibleMove.setPriority(1);
//                        }
//                    } else if ((squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column-1] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column] == null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column+1] != null) && (squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column+1] != null)){
//                        ArrayCoordinate nextC1 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column - 1));
//                        ArrayCoordinate nextC2 = new ArrayCoordinate((possibleMove.targetPosition.row + 1), (possibleMove.targetPosition.column + 1));
//                        ArrayCoordinate nextC3 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column + 1));
//                        if ((contains(occupiedSpaces, nextC1)) || ((contains(occupiedSpaces, nextC2))) || ((contains(occupiedSpaces, nextC3)))){
//                            possibleMove.setPriority(1);
//                        }
//                    } else if ((squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column-1] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column+1] == null) && (squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column+1] != null)){
//                        ArrayCoordinate nextC1 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column - 1));
//                        ArrayCoordinate nextC2 = new ArrayCoordinate((possibleMove.targetPosition.row + 1), (possibleMove.targetPosition.column));
//                        ArrayCoordinate nextC3 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column + 1));
//                        if ((contains(occupiedSpaces, nextC1)) || ((contains(occupiedSpaces, nextC2))) || ((contains(occupiedSpaces, nextC3)))){
//                            possibleMove.setPriority(1);
//                        }
//                    } else if ((squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column-1] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column] != null) && (squares[possibleMove.targetPosition.row+1][possibleMove.targetPosition.column+1] != null) && (squares[possibleMove.targetPosition.row][possibleMove.targetPosition.column+1] != null)){
//                        ArrayCoordinate nextC1 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column - 1));
//                        ArrayCoordinate nextC2 = new ArrayCoordinate((possibleMove.targetPosition.row + 1), (possibleMove.targetPosition.column));
//                        ArrayCoordinate nextC3 = new ArrayCoordinate((possibleMove.targetPosition.row + 1), (possibleMove.targetPosition.column + 1));
//                        ArrayCoordinate nextC4 = new ArrayCoordinate((possibleMove.targetPosition.row), (possibleMove.targetPosition.column + 1));
//                        if ((contains(occupiedSpaces, nextC1)) || ((contains(occupiedSpaces, nextC2))) || ((contains(occupiedSpaces, nextC3))) || ((contains(occupiedSpaces, nextC4)))){
//                            possibleMove.setPriority(1);
//                        }
//                    }
//                }
//            }
//        }
//    }


    void prioritizeBack(MoveCode possibleMove) {
        int multiplier;
        multiplier = (24 - possibleMove.startPosition.row) * 5;
        possibleMove.multiplyPriority(multiplier);
    }
}