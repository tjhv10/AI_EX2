// package Technics;

// import Stracture.Action;
// import Stracture.ActionOutcome;
// import Stracture.Cell;
// import Stracture.CellType;
// import Stracture.Grid;
// import java.util.Random;

// public class SARSA {
//     private final Grid grid; // The grid representing the environment
//     private final double epsilon; // Epsilon for the epsilon-greedy policy
//     private double[][] utilities; // Q-values for each state-action pair
//     private final Action[][] policy; // Derived policy from Q-values
//     private final Random random;
//     private final double alpha;

//     // Constructor to initialize SARSA parameters
//     public SARSA(Grid grid, double alpha, double epsilon) {
//         this.grid = grid;
//         this.epsilon = epsilon;
//         this.random = new Random();
//         this.utilities = new double[grid.getHeight()][grid.getWidth()];
//         this.policy = new Action[grid.getHeight()][grid.getWidth()];
//         this.alpha = alpha;
//     }

//     // Main method to run the SARSA algorithm
//     public void run() throws InterruptedException {
//         int width = grid.getWidth();
//         int height = grid.getHeight();
//         double[][] newUtilities = new double[height][width];

//         boolean converged = false;
//         while (!converged) {
//             converged = true;
//             for (int i = 0; i < height; i++) {
//                 for (int j = 0; j < width; j++) {
//                     Cell cell = grid.getCell(i, j);
//                     double reward = cell.getReward();
//                     Action bestAction = null;
//                     if (random.nextDouble() < epsilon) {
//                         bestAction = Action.values()[random.nextInt(Action.values().length)];
//                     }
//                     else{
//                     if (cell.getCellType() == CellType.WALL) {
//                         newUtilities[i][j] = 0; // Utilities for walls are zero
//                         policy[i][j] = null; // No action for walls
//                     } else if (reward != 0) {
//                         newUtilities[i][j] = reward; // Terminal state utilities are their rewards
//                         policy[i][j] = null; // No action for terminal states
//                     } else {
//                         double maxUtility = Double.NEGATIVE_INFINITY;
                        
//                         for (Action action : Action.values()) {
//                             double utility = 0;
//                             for (ActionOutcome outcome : action.getOutcomes(grid.getP())) {
//                                 int newX = i + outcome.getDeltaX();
//                                 int newY = j + outcome.getDeltaY();
//                                 // Check for boundaries and walls
//                                 if (newX >= 0 && newX < height && newY >= 0 && newY < width) {
//                                     if (grid.getCell(newX, newY).getCellType() == CellType.WALL) {
//                                         utility += outcome.getProbability() * utilities[i][j];
//                                     } else {
//                                         utility += outcome.getProbability() * utilities[newX][newY];
//                                     }
//                                 } else {
//                                     utility += outcome.getProbability() * utilities[i][j];
//                                 }
//                             }
//                             if (utility > maxUtility) {
//                                 maxUtility = utility;
//                                 bestAction = action;
//                             }
//                         }
                        
//                         // Apply epsilon-greedy policy
                       
//                         newUtilities[i][j] =  alpha * (reward + grid.getCell(0, 0).getStepCost() +  0.5 * maxUtility - utilities[i][j]);
//                     }
                    
//                 }
//                     policy[i][j] = bestAction;
//                     // Check for convergence
//                     if (Math.abs(newUtilities[i][j] - utilities[i][j]) > 0.001) {
//                         converged = false;
//                     }
//                 }
//             }
//             // Swap references for utilities arrays
//             double[][] temp = utilities;
//             utilities = newUtilities;
//             newUtilities = temp;
//         }
//         System.out.println("SARSA:");
//         for (double[] utilitie : utilities) {
//             for (int j = 0; j < utilities[0].length; j++) {
//                 System.out.print(String.format("%.5f", utilitie[j]) + " ");
//             }
//             System.out.println();
//         }
//     }
//     public Action[][] getPolicy() {
//         return policy;
//     }
// }

























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
    public void run() {
        Random rand = new Random();

        for (int episode = 0; episode < episodes; episode++) {
            int x = rand.nextInt(grid.getHeight());
            int y = rand.nextInt(grid.getWidth());
            Action action = epsilonGreedy(x, y, rand);
            int stuck = 0;
            // System.out.println(findMax(qValues[0][6]));
            while (stuck<1000) {
                stuck++;
                int newX = x + action.getDeltaX();
                int newY = y + action.getDeltaY();
                // Check for boundaries and walls
                if (newX < 0 || newX >= grid.getHeight() || newY < 0 || newY >= grid.getWidth() || grid.getCell(newX, newY).getCellType() == CellType.WALL) {
                    newX = x;
                    newY = y;
                }
                double curReward = grid.getCell(x, y).getReward();
                Action nextAction = epsilonGreedy(newX, newY, rand);
                double qNext = qValues[newX][newY][nextAction.ordinal()];
                double oldQ = qValues[x][y][action.ordinal()];
                if(curReward!=0)
                {
                    for (int i = 0; i < 4; i++) {
                        qValues[x][y][i] = curReward;
                    }                    
                }
                else
                    qValues[x][y][action.ordinal()] += alpha * (curReward + grid.getCell(newX, newY).getStepCost() + 0.5 * qNext - oldQ);
                
                x = newX;
                y = newY;
                action = nextAction;
                if(curReward!=0)
                    break;
            }
            
            if (episode > episodes / 2 && checkConvergence()) {
                break;
            }
        }
        System.out.println("SARSA:");
        for (double[][] qValue : qValues) {
            for (int j = 0; j < qValues[0].length; j++) {
                System.out.print(String.format("%.5f", findMax(qValue[j])) + " ");
            }
            System.out.println();
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
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
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
