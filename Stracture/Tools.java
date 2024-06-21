package Stracture;

public class Tools {
    public static void printPolicy(Action[][] policy, Grid grid) {
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                if (policy[i][j] == null) {
                    if(grid.getCell(i, j).getCellType().equals(CellType.REWARD))
                        System.out.print(String.format("%6s", grid.getCell(i, j).getReward()));
                    else
                    System.out.print("|=!=| ");
                } else {
                    switch (policy[i][j]) {
                        case RIGHT -> System.out.print("  ->  ");
                        case UP -> System.out.print("  ^   ");
                        case DOWN -> System.out.print("  v   ");
                        case LEFT -> System.out.print("  <-  ");
                    }
                }
            }
            System.out.println();
        }
    }
    public static double averageDifference(double[][] array1, double[][] array2) {
        // Check if both arrays have the same dimensions
        if (array1.length != array2.length || array1[0].length != array2[0].length) {
            throw new IllegalArgumentException("Arrays must have the same dimensions");
        }
        double totalDifference = 0.0;
        int numRows = array1.length;
        int numCols = array1[0].length;
        int totalElements = numRows * numCols;

        // Iterate over the arrays and calculate the sum of absolute differences
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                totalDifference += Math.abs(array1[i][j] - array2[i][j]);
            }
        }
        // Calculate and return the average difference
        return totalDifference / totalElements;
    }
}
