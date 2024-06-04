import java.util.List;
import java.util.Arrays;
import java.awt.Point;
import Stracture.Grid;
import Stracture.PrintingTools;
import Stracture.Reward;
import Technics.QLearning;
import Technics.SARSA;
import Technics.ValueIteration;

public class Main {
    public static void main(String[] args) {
        int height = 3;
        int width = 4;
    
        List<Reward> rewards = Arrays.asList(
                new Reward(0, 3, 1),
                new Reward(1, 3, -1)
        );

        List<Point> walls = Arrays.asList(
                new Point(1, 1)
        );

        double stepCost = -0.04;

        Grid grid = new Grid(width, height, rewards, stepCost, walls);

        double discountFactor = 1;
        double theta = 0.01;
        double alpha = 0.1;
        double epsilon = 0.1;
        int episodes = 1000;
        double p = 0.8;
        
        // Measure time for Value Iteration
        long startTime = System.currentTimeMillis();
        ValueIteration vi = new ValueIteration(grid, discountFactor, theta, p);
        vi.run();
        long endTime = System.currentTimeMillis();
        long viTime = endTime - startTime;
        System.out.println();
        System.out.println("Value Iteration Time: " + viTime + " ms");

        // Measure time for Q-Learning
        startTime = System.currentTimeMillis();
        QLearning ql = new QLearning(grid, alpha, discountFactor, epsilon, episodes);
        // TODO add feture of miss step with (1-p)/2 probability
        ql.run();
        endTime = System.currentTimeMillis();
        long qlTime = endTime - startTime;
        System.out.println("Q-Learning Time: " + qlTime + " ms");

        // Measure time for SARSA
        startTime = System.currentTimeMillis();
        SARSA sarsa = new SARSA(grid, alpha, discountFactor, epsilon, episodes);
        // TODO add feture of miss step with (1-p)/2 probability
        sarsa.run();
        endTime = System.currentTimeMillis();
        long sarsaTime = endTime - startTime;
        System.out.println("SARSA Time: " + sarsaTime + " ms");

        System.out.println("Value Iteration Results:");
        PrintingTools.printPolicy(vi.getPolicy(), grid);

        System.out.println("Q-Learning Results:");
        PrintingTools.printPolicy(ql.getPolicy(), grid);

        System.out.println("SARSA Results:");
        PrintingTools.printPolicy(sarsa.getPolicy(), grid);
    }
}
