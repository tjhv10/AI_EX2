package Technics;
import Tools.Action;
import Tools.ActionOutcome;
import Tools.Cell;
import Tools.CellType;
import Tools.Grid;

public class ValueIteration {
    private Grid grid;
    private double discountFactor;
    private double theta;
    private double[][] utilities;
    private Action[][] policy;

    public ValueIteration(Grid grid, double discountFactor, double theta) {
        this.grid = grid;
        this.discountFactor = discountFactor;
        this.theta = theta;
        this.utilities = new double[grid.getHeight()][grid.getWidth()];
        this.policy = new Action[grid.getHeight()][grid.getWidth()];
    }

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
                        policy[i][j] = null; // No action needed for wall cells
                    } else if (reward != 0) {
                        newUtilities[i][j] = reward;
                        policy[i][j] = null; // No action needed for terminal states
                    } else {
                        double maxUtility = Double.NEGATIVE_INFINITY;
                        Action bestAction = null;
                        for (Action action : Action.values()) {
                            double utility = 0;
                            for (ActionOutcome outcome : action.getOutcomes()) {
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
        }
    }

    public double[][] getUtilities() {
        return utilities;
    }

    public Action[][] getPolicy() {
        return policy;
    }
}
