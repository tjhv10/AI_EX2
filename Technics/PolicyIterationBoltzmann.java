package Technics;

import Stracture.Action;
import Stracture.ActionOutcome;
import Stracture.Cell;
import Stracture.CellType;
import Stracture.Grid;
import java.util.HashMap;
import java.util.Map;

public class PolicyIterationBoltzmann {
    private final Grid grid; // The grid representing the environment
    private double[][] utilities; // Utilities for each state
    private final Action[][] policy; // Derived policy from utilities
    private double temperature; // Temperature parameter for Boltzmann distribution
    private final double coolingRate; // Cooling rate for temperature decay

    // Constructor to initialize PolicyIterationBoltzmann parameters
    public PolicyIterationBoltzmann(Grid grid, double temperature, double coolingRate) {
        this.grid = grid;
        this.utilities = new double[grid.getHeight()][grid.getWidth()];
        this.policy = new Action[grid.getHeight()][grid.getWidth()];
        this.temperature = temperature;
        this.coolingRate = coolingRate;
    }

    // Main method to run the Policy Iteration algorithm
    public double [][] run() {
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
                        Map<Action, Double> actionUtilities = new HashMap<>();
                        for (Action action : Action.values()) {
                            double utility = 0;
                            for (ActionOutcome outcome : action.getOutcomes(grid.getP())) {
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
                            actionUtilities.put(action, utility);
                        }
                        
                        
                        double maxUtility = Double.NEGATIVE_INFINITY;
                        Action bestAction = null;
                        for (Map.Entry<Action, Double> entry : actionUtilities.entrySet()) {
                            if (entry.getValue() > maxUtility) {
                                maxUtility = entry.getValue();
                                bestAction = entry.getKey();
                            }
                        }
                        
                        newUtilities[i][j] = cell.getStepCost() + 0.5 * maxUtility;
                        policy[i][j] = bestAction;
                    }

                    if (Math.abs(newUtilities[i][j] - utilities[i][j]) > 0.01) {
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
        // System.out.println("PIB:");
        // for (double[] row : utilities) {
        //     for (double value : row) {
        //         System.out.print(String.format("%.5f", value) + " ");
        //     }
        //     System.out.println();
        // }
        return utilities;
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
