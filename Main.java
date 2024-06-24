import Stracture.Grid;
import Stracture.Tools;
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
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] matrix1 : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix1[j] + " ");
            }
            //System.out.println();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println();
        List<Grid> grids = Grid.readFile();
        double alpha = 0.1;
        double epsilon = 0.1;
        // double sum= 0;
        // int puzzle = 1;
        // for (Grid grid : grids) {
        //     System.out.println(puzzle++ + ":");
        //     double [][] vi = new ValueIteration(grid).run();
        //     double [][] pib = new PolicyIterationBoltzmann(grid, 1, 0.5).run();
        //     double [][] sarsa = new SARSA(grid, alpha, epsilon,10000).run();
        //     sum+=Tools.averageDifference(vi, sarsa);
        //     System.out.println("Diffrance: "+Tools.averageDifference(vi, sarsa));
        // }
        // System.out.println("Avrage diffrance: "+sum/10);
        int x = 8;
        double [][] vi = new ValueIteration(grids.get(x)).run();
        double [][] pib = new PolicyIterationBoltzmann(grids.get(x), 1, 0.5).run();
        double [][] sarsa = new SARSA(grids.get(x), alpha, epsilon,10000).run();
        System.out.println("cels: "+vi[0].length*vi.length);
        System.out.println("Diffrance: "+Tools.averageDifference(vi, sarsa));
        System.out.println("MDP->MBRL");
        printMatrixDifference(vi,pib);
        System.out.println();
        System.out.println("MDP->MFRL");
        printMatrixDifference(vi,sarsa);
        System.out.println();
        System.out.println("MBRL->MFRL");
        printMatrixDifference(pib,sarsa);




    }
}
