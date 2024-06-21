import Stracture.Grid;
import Technics.PolicyIterationBoltzmann;
import Technics.SARSA;
import Technics.ValueIteration;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println();
        List<Grid> grids = Grid.readFile();
        double alpha = 0.5;
        double epsilon = 0.1;
        // int puzzle = 0;
        // for (Grid grid : grids) {
        //     System.out.println(puzzle++ + ":");
        //     new ValueIteration(grid).run();
        //     new PolicyIterationBoltzmann(grid, 1, 0.5).run();
        //     new SARSA(grid, alpha, epsilon,100000).run();
        // }
        int x = 7;
        new ValueIteration(grids.get(x)).run();
        new PolicyIterationBoltzmann(grids.get(x), 1, 0.5).run();
        new SARSA(grids.get(x), alpha, epsilon,100000).run();
    }
}