import java.util.Random;

public class QLearning {
    private Grid grid;
    private double alpha;
    private double gamma;
    private double epsilon;
    private int episodes;
    private double[][][] qValues;

    public QLearning(Grid grid, double alpha, double gamma, double epsilon, int episodes) {
        this.grid = grid;
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.episodes = episodes;
        this.qValues = new double[grid.getHeight()][grid.getWidth()][Action.values().length];
    }

    public void run() {
        Random rand = new Random();

        for (int episode = 0; episode < episodes; episode++) {
            int x = rand.nextInt(grid.getHeight());
            int y = rand.nextInt(grid.getWidth());

            while (grid.getCell(x, y).getReward() == 0) {
                Action action = epsilonGreedy(x, y, rand);
                int newX = x + action.getDeltaX();
                int newY = y + action.getDeltaY();
                if (newX >= 0 && newX < grid.getHeight() && newY >= 0 && newY < grid.getWidth()) {
                    double reward = grid.getCell(newX, newY).getReward();
                    double maxQ = maxQ(newX, newY);
                    qValues[x][y][action.ordinal()] += alpha * (reward + gamma * maxQ - qValues[x][y][action.ordinal()]);
                    x = newX;
                    y = newY;
                }
            }
        }
    }

    private Action epsilonGreedy(int x, int y, Random rand) {
        if (rand.nextDouble() < epsilon) {
            return Action.values()[rand.nextInt(Action.values().length)];
        } else {
            double maxQ = Double.NEGATIVE_INFINITY;
            Action bestAction = null;
            for (Action action : Action.values()) {
                if (qValues[x][y][action.ordinal()] > maxQ) {
                    maxQ = qValues[x][y][action.ordinal()];
                    bestAction = action;
                }
            }
            return bestAction;
        }
    }

    private double maxQ(int x, int y) {
        double maxQ = Double.NEGATIVE_INFINITY;
        for (Action action : Action.values()) {
            maxQ = Math.max(maxQ, qValues[x][y][action.ordinal()]);
        }
        return maxQ;
    }

    public double[][][] getQValues() {
        return qValues;
    }
}
