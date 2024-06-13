package Stracture;

public class PrintingTools {
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
}
