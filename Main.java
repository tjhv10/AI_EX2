import java.util.List;
import java.util.Arrays;
import java.awt.Point;


public class Main {
    public static void main(String[] args) {
        int width = 4;
        int height = 3;

        List<Reward> rewards = Arrays.asList(
                new Reward(0, 3, 10),
                new Reward(1, 3, -10)
        );

        List<Point> walls = Arrays.asList(
                new Point(1, 1)
        );

        double stepCost = -0.04;

        Grid grid = new Grid(width, height, rewards, stepCost, walls);

        double discountFactor = 0.5;
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
        Tools.printPolicy(vi.getPolicy(), grid);

        System.out.println("Q-Learning Results:");
        Tools.printPolicy(ql.getPolicy(), grid);

        System.out.println("SARSA Results:");
        Tools.printPolicy(sarsa.getPolicy(), grid);
    }


    
}
