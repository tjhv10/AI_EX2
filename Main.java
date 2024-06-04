import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int width = 5;
        int height = 5;

        List<Reward> rewards = new ArrayList<>();
        rewards.add(new Reward(0, 0, 10));
        rewards.add(new Reward(4, 4, -10));
        rewards.add(new Reward(2, 2, 0));

        double stepCost = -0.04;

        Grid grid = new Grid(width, height, rewards, stepCost);

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
        printUtilities(vi.getUtilities(), grid);

        System.out.println("Q-Learning Results:");
        printQValues(ql.getQValues(), grid);

        System.out.println("SARSA Results:");
        printQValues(sarsa.getQValues(), grid);
    }

    private static void printUtilities(double[][] utilities, Grid grid) {
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                System.out.printf("%.2f ", utilities[i][j]);
            }
            System.out.println();
        }
    }

    private static void printQValues(double[][][] qValues, Grid grid) {
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                System.out.print("(");
                for (Action action : Action.values()) {
                    System.out.printf("%.2f ", qValues[i][j][action.ordinal()]);
                }
                System.out.print(") ");
            }
            System.out.println();
        }
    }
}
