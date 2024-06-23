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
        double alpha = 0.1;
        double epsilon = 0.1, sum= 0;
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
        int x = 0;
        double [][] vi = new ValueIteration(grids.get(x)).run();
        double [][] pib = new PolicyIterationBoltzmann(grids.get(x), 1, 0.5).run();
        double [][] sarsa = new SARSA(grids.get(x), alpha, epsilon,100000).run();
        System.out.println("Diffrance: "+Tools.averageDifference(vi, sarsa));
    }
}