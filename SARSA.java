import java.util.Random;

public class SARSA {
    private Grid grid;
    private double alpha;
    private double gamma;
    private double epsilon;
    private int episodes;
    private double[][][] qValues;

    public SARSA(Grid grid, double alpha, double gamma, double epsilon, int episodes) {
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
            Action action = epsilonGreedy(x, y, rand);

            while (grid.getCell(x, y).getReward() == 0) {
                int newX = x + action.getDeltaX();
                if (newX<0)
                    newX =0;
                if (newX >= grid.getWidth()) 
                    newX = grid.getHeight()-1;

                int newY = y + action.getDeltaY();
                if (newY<0)
                    newY =0;
                if (newY >= grid.getWidth()) {
                    newY = grid.getWidth()-1;
                }
                if (newX >= 0 && newX < grid.getHeight() && newY >= 0 && newY < grid.getWidth()) {
                    double reward = grid.getCell(newX, newY).getReward();
                    Action nextAction = epsilonGreedy(newX, newY, rand);
                    double qNext = qValues[newX][newY][nextAction.ordinal()];
                    qValues[x][y][action.ordinal()] += alpha * (reward + gamma * qNext - qValues[x][y][action.ordinal()]);
                    x = newX;
                    y = newY;
                    action = nextAction;
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

    public double[][][] getQValues() {
        return qValues;
    }
}
