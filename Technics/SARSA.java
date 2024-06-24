package Technics;

import Stracture.Action;
import Stracture.ActionOutcome;
import Stracture.CellType;
import Stracture.Grid;
import java.util.Random;

public class SARSA {
    private final Grid grid; // The grid representing the environment
    private final double alpha; // Learning rate
    private final double epsilon; // Epsilon for the epsilon-greedy policy
    private final int episodes; // Number of episodes to run
    private final double[][][] qValues; // Q-values for each state-action pair
    private final Action[][] policy; // Derived policy from Q-values

    // Constructor to initialize SARSA parameters
    public SARSA(Grid grid, double alpha, double epsilon, int episodes) {
        this.grid = grid;
        this.alpha = alpha;
        this.epsilon = epsilon;
        this.episodes = episodes;
        this.qValues = new double[grid.getHeight()][grid.getWidth()][Action.values().length];
        this.policy = new Action[grid.getHeight()][grid.getWidth()];
    }

    // Main method to run the SARSA algorithm
    public double[][] run() {
        double[][] ret = new double[grid.getHeight()][grid.getWidth()];
        Random rand = new Random();

        for (int episode = 0; episode < episodes; episode++) {
            int x = rand.nextInt(grid.getHeight());
            int y = rand.nextInt(grid.getWidth());
            Action action = epsilonGreedy(x, y, rand);
            int steps = 0;

            while (steps < 1000) {
                steps++;
                int newX = x + action.getDeltaX();
                int newY = y + action.getDeltaY();

                // Check for boundaries and walls
                if (newX < 0 || newX >= grid.getHeight() || newY < 0 || newY >= grid.getWidth() || grid.getCell(newX, newY).getCellType() == CellType.WALL) {
                    newX = x;
                    newY = y;
                }

                double curReward = grid.getCell(x, y).getReward();
                Action nextAction = epsilonGreedy(newX, newY, rand);
                double oldQ = qValues[x][y][action.ordinal()];

                if (curReward != 0||grid.getCell(x, y).getCellType() == CellType.WALL) {
                    // Terminal state: directly set the Q-values to the reward
                    for (int i = 0; i < Action.values().length; i++) {
                        qValues[x][y][i] = curReward;
                    }
                } else {
                    // Non-terminal state: update Q-value using SARSA update rule
                    double expectedQ = 0.0;
                    // ..................................
                    // ? We couldnt understand if the sarsa algorithm uses probability in it so if it is then uncomment this for loop and comment row 74
                    // ..................................
                    // for (ActionOutcome outcome : action.getOutcomes(grid.getP())) {
                    //     int oldx= newX,oldy= newY;
                    //     newX = newX + outcome.getDeltaX();
                    //     newY = newY + outcome.getDeltaY();
                    //     if (newX < 0 || newX >= grid.getHeight() || newY < 0 || newY >= grid.getWidth()) {
                    //         newX = oldx;
                    //         newY = oldy;
                    //     }
                    //     expectedQ += qValues[oldx][oldy][outcome.getAction().ordinal()] * outcome.getProbability();
                    // }
                    expectedQ += qValues[newX][newY][nextAction.ordinal()];
                    qValues[x][y][action.ordinal()] += alpha * (curReward + grid.getCell(newX, newY).getStepCost() + 0.5 * expectedQ - oldQ);
                }
                x = newX;
                y = newY;
                action = nextAction;
                if (grid.getCell(x, y).getReward() != 0) {
                    break;
                }
            }

            if (episode > episodes / 2 && checkConvergence()) {
                break;
            }
        }

        // System.out.println("SARSA:");
        for (int i = 0; i < qValues.length; i++) 
        {
            for (int j = 0; j < qValues[0].length; j++) {
        //         System.out.print(String.format("%.5f", findMax(qValues[i][j])) + " ");
                ret[i][j] = findMax(qValues[i][j]);
            }
        //     System.out.println();
        }
        updatePolicy();
        return ret;
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
        return maxChange < 0.01;
    }

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
                        for (ActionOutcome ao : action.getOutcomes(grid.getP())) {
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

    public static double findMax(double[] array) {
        double max = array[0];
        int x =1;
        if(max==0)
        {
            max = array[1];
            x++;
        }
        for (int i = x; i < array.length; i++) {
            if (array[i] > max) {
                if(array[i]!=0)
                    max = array[i];
            }
        }
        return max;
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
                for (ActionOutcome ao : action.getOutcomes(grid.getP())) {
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
