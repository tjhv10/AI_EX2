package Technics;

import java.util.Random;
import Stracture.Action;
import Stracture.ActionOutcome;
import Stracture.CellType;
import Stracture.Grid;

public class SARSA {
    private Grid grid;
    private double alpha;
    private double gamma;
    private double epsilon;
    private int episodes;
    private double theta;
    private double[][][] qValues;
    private Action[][] policy;
    private double p;

    public SARSA(Grid grid, double alpha, double gamma, double epsilon, int episodes, double theta, double p) {
        this.grid = grid;
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.episodes = episodes;
        this.theta = theta;
        this.qValues = new double[grid.getHeight()][grid.getWidth()][Action.values().length];
        this.policy = new Action[grid.getHeight()][grid.getWidth()];
        this.p = p;
    }

    public void run() {
        Random rand = new Random();
        boolean converged = true;

        for (int episode = 0; episode < episodes; episode++) {
            int x = rand.nextInt(grid.getHeight());
            int y = rand.nextInt(grid.getWidth());
            Action action = epsilonGreedy(x, y, rand);

            while (grid.getCell(x, y).getReward() == 0 && grid.getCell(x, y).getCellType() != CellType.WALL) {
                int newX = x + action.getDeltaX();
                int newY = y + action.getDeltaY();

                if (newX < 0 || newX >= grid.getHeight() || newY < 0 || newY >= grid.getWidth() || grid.getCell(newX, newY).getCellType() == CellType.WALL) {
                    newX = x;
                    newY = y;
                }

                double reward = grid.getCell(newX, newY).getReward();
                Action nextAction = epsilonGreedy(newX, newY, rand);
                double qNext = qValues[newX][newY][nextAction.ordinal()];
                double oldQ = qValues[x][y][action.ordinal()];
                qValues[x][y][action.ordinal()] += alpha * (reward + gamma * qNext - oldQ);

                x = newX;
                y = newY;
                action = nextAction;
            }

            converged = checkConvergence();
        }
        updatePolicy();
    }

    private boolean checkConvergence() {
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                for (int k = 0; k < Action.values().length; k++) {
                    if (Math.abs(qValues[i][j][k]) > theta) {
                        return false;
                    }
                }
            }
        }
        return true;
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
