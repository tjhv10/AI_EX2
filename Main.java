import Stracture.Grid;
import Technics.PolicyIterationBoltzmann;
import Technics.SARSA;
import Technics.ValueIteration;
import java.util.List;

public class Main {
    public static void printMatrixDifference(double[][] matrixA, double[][] matrixB) {
        if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) {
            System.out.println("Matrices must have the same dimensions");
            return;
        }

        int rows = matrixA.length;
        int cols = matrixA[0].length;
        double[][] differenceMatrix = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double d =  matrixA[i][j] - matrixB[i][j];
                if(d>=0){
                    differenceMatrix[i][j] = d;
                }else {
                    differenceMatrix[i][j] = d * -1;

                }
            }
        }
        printMatrix(differenceMatrix);
        System.out.println();
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] matrix1 : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix1[j] + " ");
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println();
        List<Grid> grids = Grid.readFile();
        double alpha = 0.1;
        double epsilon = 0.1;
        // int puzzle = 1;
        // for (Grid grid : grids) {
        //     System.out.println(puzzle++ + ":");
        //     double [][] vi = new ValueIteration(grid).run();
        //     double [][] pib = new PolicyIterationBoltzmann(grid, 1, 0.5).run();
        //     double [][] sarsa = new SARSA(grid, alpha, epsilon,10000).run();
        //     System.out.println("Diffrance VI, PIB: "+Tools.averageDifference(vi, pib));
        //     System.out.println("Diffrance VI, SARSA: "+Tools.averageDifference(vi, sarsa));
        //     System.out.println("Diffrance PIB, SARSA: "+Tools.averageDifference(pib, sarsa));
        // }
        for (int x = 0; x < grids.size(); x++) {
            // System.out.println("puzzle number "+(x+1)+":");
            double [][] vi = new ValueIteration(grids.get(x)).run();
            double [][] pib = new PolicyIterationBoltzmann(grids.get(x), 1, 0.5).run();
            double [][] sarsa = new SARSA(grids.get(x), alpha, epsilon,10000).run();
            // System.out.println(x+1);
            // System.out.println("Diffrance VI, PIB: "+Tools.averageDifference(vi, pib));
            // System.out.println("Diffrance VI, SARSA: "+Tools.averageDifference(vi, sarsa));
            // System.out.println("Diffrance PIB, SARSA: "+Tools.averageDifference(pib, sarsa));
            printMatrixDifference(vi,pib);
            System.out.println();
            printMatrixDifference(vi,sarsa);
            System.out.println();
            printMatrixDifference(pib,sarsa);
            System.out.println();
        }
    }
}
