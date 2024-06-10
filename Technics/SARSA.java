package Technics;

import java.util.Random;
import Stracture.Action;
import Stracture.ActionOutcome;
import Stracture.CellType;
import Stracture.Grid;

public class SARSA {
    private Grid grid; // The grid representing the environment
    private double alpha; // Learning rate
    private double discountFactor; // Discount factor for future rewards
    private double epsilon; // Epsilon for the epsilon-greedy policy
    private int episodes; // Number of episodes to run
    private double theta; // Threshold for convergence
    private double[][][] qValues; // Q-values for each state-action pair
    private Action[][] policy; // Derived policy from Q-values
    private double p; // Probability used for action outcomes

    // Constructor to initialize SARSA parameters
    public SARSA(Grid grid, double alpha, double gamma, double epsilon, int episodes, double theta, double p) {
        this.grid = grid;
        this.alpha = alpha;
        this.discountFactor = gamma;
        this.epsilon = epsilon;
        this.episodes = episodes;
        this.theta = theta;
        this.qValues = new double[grid.getHeight()][grid.getWidth()][Action.values().length];
        this.policy = new Action[grid.getHeight()][grid.getWidth()];
        this.p = p;
    }

    // Main method to run the SARSA algorithm
    public void run() {
        Random rand = new Random();
        for (int episode = 0; episode < episodes; episode++) {
            int x = rand.nextInt(grid.getHeight());
            int y = rand.nextInt(grid.getWidth());
            Action action = epsilonGreedy(x, y, rand);

            // Continue the episode until reaching a terminal state
            while (grid.getCell(x, y).getReward() == 0 && grid.getCell(x, y).getCellType() != CellType.WALL) {
                int newX = x + action.getDeltaX();
                int newY = y + action.getDeltaY();

                // Check for boundaries and walls
                if (newX < 0 || newX >= grid.getHeight() || newY < 0 || newY >= grid.getWidth() || grid.getCell(newX, newY).getCellType() == CellType.WALL) {
                    newX = x;
                    newY = y;
                }
                double reward = grid.getCell(newX, newY).getReward();
                Action nextAction = epsilonGreedy(newX, newY, rand);
                double qNext = qValues[newX][newY][nextAction.ordinal()];
                double oldQ = qValues[x][y][action.ordinal()];
                qValues[x][y][action.ordinal()] += alpha * (reward + discountFactor * qNext - oldQ);
                x = newX;
                y = newY;
                action = nextAction;
            }
            // Check for convergence after half the episodes
            if (episode > episodes / 2 && checkConvergence()) {
                break;
            }
        }
        updatePolicy();
    }

    // Method to check for convergence based on Q-values
    private boolean checkConvergence() {
        double maxChange = 0;
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                for (int k = 0; k < Action.values().length; k++) {
                    double val = Math.abs(qValues[i][j][k]);
                    if (val > maxChange) {
                        maxChange = val;
                    }
                }
            }
        }
        return maxChange < theta;
    }

    // Update the policy based on the Q-values
    private void updatePolicy() {
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                if (grid.getCell(i, j).getCellType() == CellType.WALL || grid.getCell(i, j).getReward() != 0) {
                    policy[i][j] = null; // No action for walls or terminal states
                } else {
                    double maxQ = Double.NEGATIVE_INFINITY;
                    Action bestAction = null;
                    for (Action action : Action.values()) {
                        double newMaxQ = 0;
                        for (ActionOutcome ao : action.getOutcomes(p)) {
                            newMaxQ += qValues[i][j][ao.getAction().ordinal()] * ao.getProbability();
                        }
                        if (newMaxQ > maxQ) {
                            maxQ = newMaxQ;
                            bestAction = action;
                        }
                    }
                    policy[i][j] = bestAction;
                }
            }
        }
    }

    // Epsilon-greedy policy for action selection
    private Action epsilonGreedy(int x, int y, Random rand) {
        if (rand.nextDouble() < epsilon) {
            return Action.values()[rand.nextInt(Action.values().length)];
        } else {
            double maxQ = Double.NEGATIVE_INFINITY;
            Action bestAction = null;
            for (Action action : Action.values()) {
                double newMaxQ = 0;
                for (ActionOutcome ao : action.getOutcomes(p)) {
                    newMaxQ += qValues[x][y][ao.getAction().ordinal()] * ao.getProbability();
                }
                if (newMaxQ > maxQ) {
                    maxQ = newMaxQ;
                    bestAction = action;
                }
            }
            return bestAction;
        }
    }
    public double[][][] getQValues() {
        return qValues;
    }
    public Action[][] getPolicy() {
        return policy;
    }
}
