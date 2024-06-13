package Technics;

import Stracture.Action;
import Stracture.ActionOutcome;
import Stracture.Cell;
import Stracture.CellType;
import Stracture.Grid;

public class ValueIteration {
    private final Grid grid; // The grid representing the environment
    private double[][] utilities; // Utilities for each state
    private final Action[][] policy; // Derived policy from utilities

    // Constructor to initialize ValueIteration parameters
    public ValueIteration(Grid grid) {
        this.grid = grid;
        this.utilities = new double[grid.getHeight()][grid.getWidth()];
        this.policy = new Action[grid.getHeight()][grid.getWidth()];
    }

    // Main method to run the Value Iteration algorithm
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
                        newUtilities[i][j] = 0; // Utilities for walls are zero
                        policy[i][j] = null; // No action for walls
                    } else if (reward != 0) {
                        newUtilities[i][j] = reward; // Terminal state utilities are their rewards
                        policy[i][j] = null; // No action for terminal states
                    } else {
                        double maxUtility = Double.NEGATIVE_INFINITY;
                        Action bestAction = null;
                        for (Action action : Action.values()) {
                            double utility = 0;
                            for (ActionOutcome outcome : action.getOutcomes(grid.getP())) {
                                int newX = i + outcome.getDeltaX();
                                int newY = j + outcome.getDeltaY();
                                // Check for boundaries and walls
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
                        newUtilities[i][j] = cell.getStepCost() + 0.5 * maxUtility;
                        policy[i][j] = bestAction;
                    }
                    // Check for convergence
                    if (Math.abs(newUtilities[i][j] - utilities[i][j]) > 0.01) {
                        converged = false;
                    }
                }
            }
            // Swap references for utilities arrays
            double[][] temp = utilities;
            utilities = newUtilities;
            newUtilities = temp;
        }
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
