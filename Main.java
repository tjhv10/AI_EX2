import java.util.List;

import Tools.Grid;
import Tools.Reward;
import Tools.PrintingTools;

import java.util.Arrays;
import java.awt.Point;


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

        double stepCost = -0.1;

        Grid grid = new Grid(width, height, rewards, stepCost, walls);

        double discountFactor = 1;
        double theta = 0.01;
        double alpha = 0.1;
        double epsilon = 0.1;
        int episodes = 1000;

        ValueIteration vi = new ValueIteration(grid, discountFactor, theta);
        vi.run();

        QLearning ql = new QLearning(grid, alpha, discountFactor, epsilon, episodes);
        ql.run();

        SARSA sarsa = new SARSA(grid, alpha, discountFactor, epsilon, episodes);
        sarsa.run();

        System.out.println("Value Iteration Results:");
        PrintingTools.printPolicy(vi.getPolicy(), grid);

        System.out.println("Q-Learning Results:");
        PrintingTools.printPolicy(ql.getPolicy(), grid);

        System.out.println("SARSA Results:");
        PrintingTools.printPolicy(sarsa.getPolicy(), grid);
    }
}
