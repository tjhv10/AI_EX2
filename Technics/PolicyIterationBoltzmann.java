package Technics;

import Stracture.Action;
import Stracture.ActionOutcome;
import Stracture.Cell;
import Stracture.CellType;
import Stracture.Grid;

public class PolicyIterationBoltzmann {
    private Grid grid; // The grid representing the environment
    private double discountFactor; // Discount factor for future rewards
    private double theta; // Threshold for convergence
    private double[][] utilities; // Utilities for each state
    private Action[][] policy; // Derived policy from utilities
    private double p; // Probability used for action outcomes
    private double temperature; // Temperature parameter for Boltzmann distribution
    private double coolingRate; // Cooling rate for temperature decay

    // Constructor to initialize PolicyIterationBoltzmann parameters
    public PolicyIterationBoltzmann(Grid grid, double discountFactor, double theta, double p, double temperature, double coolingRate) {
        this.grid = grid;
        this.discountFactor = discountFactor;
        this.theta = theta;
        this.utilities = new double[grid.getHeight()][grid.getWidth()];
        this.policy = new Action[grid.getHeight()][grid.getWidth()];
        this.p = p;
        this.temperature = temperature;
        this.coolingRate = coolingRate;
    }

    // Main method to run the Policy Iteration algorithm
    public void run() {
        int width = grid.getWidth();
        int height = grid.getHeight();
        double[][] newUtilities = new double[height][width];

        boolean converged = false;
        while (!converged) {
            converged = true;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Cell cell = grid.getCell(i, j);
                    double reward = cell.getReward();
                    if (cell.getCellType() == CellType.WALL) {
                        newUtilities[i][j] = 0;
                        policy[i][j] = null;
                    } else if (reward != 0) {
                        newUtilities[i][j] = reward;
                        policy[i][j] = null;
                    } else {
                        double maxUtility = Double.NEGATIVE_INFINITY;
                        Action bestAction = null;
                        for (Action action : Action.values()) {
                            double utility = 0;
                            for (ActionOutcome outcome : action.getOutcomes(p)) {
                                int newX = i + outcome.getDeltaX();
                                int newY = j + outcome.getDeltaY();
                                if (newX >= 0 && newX < height && newY >= 0 && newY < width) {
                                    if (grid.getCell(newX, newY).getCellType() == CellType.WALL) {
                                        utility += outcome.getProbability() * utilities[i][j];
                                    } else {
                                        utility += outcome.getProbability() * utilities[newX][newY];
                                    }
                                } else {
                                    utility += outcome.getProbability() * utilities[i][j];
                                }
                            }
                            if (utility > maxUtility) {
                                maxUtility = utility;
                                bestAction = action;
                            }
                        }
                        newUtilities[i][j] = cell.getStepCost() + discountFactor * maxUtility;
                        policy[i][j] = bestAction;
                    }

                    if (Math.abs(newUtilities[i][j] - utilities[i][j]) > theta) {
                        converged = false;
                    }
                }
            }
            double[][] temp = utilities;
            utilities = newUtilities;
            newUtilities = temp;

            // Decrease the temperature
            temperature = temperature * coolingRate;
        }
        extractPolicy();
    }

    // Method to extract the policy based on the utilities using Boltzmann distribution
    private void extractPolicy() {
        int width = grid.getWidth();
        int height = grid.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = grid.getCell(i, j);
                if (cell.getReward() == 0 && cell.getCellType() != CellType.WALL) {
                    double[] actionProbabilities = new double[Action.values().length];
                    double sum = 0;
                    for (Action action : Action.values()) {
                        double utility = 0;
                        for (ActionOutcome outcome : action.getOutcomes(p)) {
                            int newX = i + outcome.getDeltaX();
                            int newY = j + outcome.getDeltaY();
                            if (newX >= 0 && newX < height && newY >= 0 && newY < width) {
                                if (grid.getCell(newX, newY).getCellType() == CellType.WALL) {
                                    utility += outcome.getProbability() * utilities[i][j];
                                } else {
                                    utility += outcome.getProbability() * utilities[newX][newY];
                                }
                            } else {
                                utility += outcome.getProbability() * utilities[i][j];
                            }
                        }
                        actionProbabilities[action.ordinal()] = Math.exp(utility / temperature);
                        sum += actionProbabilities[action.ordinal()];
                    }
                    for (int k = 0; k < actionProbabilities.length; k++) {
                        actionProbabilities[k] /= sum;
                    }
                    policy[i][j] = selectAction(actionProbabilities);
                }
            }
        }
    }

    // Method to select an action based on the Boltzmann distribution
    private Action selectAction(double[] actionProbabilities) {
        double rand = Math.random();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < actionProbabilities.length; i++) {
            cumulativeProbability += actionProbabilities[i];
            if (rand <= cumulativeProbability) {
                return Action.values()[i];
            }
        }
        return Action.values()[Action.values().length - 1]; // Return the last action by default
    }

    // Getter for utilities
    public double[][] getUtilities() {
        return utilities;
    }

    // Getter for the derived policy
    public Action[][] getPolicy() {
        return policy;
    }
}
