public class Tools {
    // public static void printUtilities(double[][] utilities, Grid grid) {
    //     for (int i = 0; i < grid.getHeight(); i++) {
    //         for (int j = 0; j < grid.getWidth(); j++) {
    //             System.out.printf("%.2f ", utilities[i][j]);
    //         }
    //         System.out.println();
    //     }
    // }

    // public static void printQValues(double[][][] qValues, Grid grid) {
    //     for (int i = 0; i < grid.getHeight(); i++) {
    //         for (int j = 0; j < grid.getWidth(); j++) {
    //             System.out.print("(");
    //             for (Action action : Action.values()) {
    //                 System.out.printf("%.2f ", qValues[i][j][action.ordinal()]);
    //             }
    //             System.out.print(") ");
    //         }
    //         System.out.println();
    //     }
    // }

    public static void printPolicy(Action[][] policy, Grid grid) {
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                if (policy[i][j] == null) {
                    System.out.print("end");
                } else {
                    switch (policy[i][j]) {
                        case UP:
                            System.out.print(" ^  ");
                            break;
                        case DOWN:
                            System.out.print(" v  ");
                            break;
                        case LEFT:
                            System.out.print(" <- ");
                            break;
                        case RIGHT:
                            System.out.print(" -> ");
                            break;
                    }
                }
            }
            System.out.println();
        }
    }
}
