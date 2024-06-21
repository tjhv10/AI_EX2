import Stracture.Grid;
import Stracture.Tools;
import Technics.PolicyIterationBoltzmann;
import Technics.SARSA;
import Technics.ValueIteration;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println();
        List<Grid> grids = Grid.readFile();
        double alpha = 0.5;
        double epsilon = 0.001;
        int puzzle = 0;
        
        for (Grid grid : grids) {
            System.out.println(puzzle++ + ":");
            double [][] vi = new ValueIteration(grid).run();
            double [][] pib = new PolicyIterationBoltzmann(grid, 1, 0.5).run();
            double [][] sarsa = new SARSA(grid, alpha, epsilon,10000).run();
            System.out.println(Tools.averageDifference(vi, sarsa));
        }
    }
}