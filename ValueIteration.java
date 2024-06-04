public class ValueIteration {
    private Grid grid;
    private double discountFactor;
    private double theta;
    private double[][] utilities;

    public ValueIteration(Grid grid, double discountFactor, double theta) {
        this.grid = grid;
        this.discountFactor = discountFactor;
        this.theta = theta;
        this.utilities = new double[grid.getHeight()][grid.getWidth()];
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
                    if (reward != 0) {
                        newUtilities[i][j] = reward;
                    } else {
                        double maxUtility = Double.NEGATIVE_INFINITY;
                        for (Action action : Action.values()) {
                            double utility = 0;
                            for (ActionOutcome outcome : action.getOutcomes()) {
                                int newX = i + outcome.getDeltaX();
                                int newY = j + outcome.getDeltaY();
                                if (newX >= 0 && newX < height && newY >= 0 && newY < width) {
                                    utility += outcome.getProbability() * utilities[newX][newY];
                                } else {
                                    utility += outcome.getProbability() * utilities[i][j];
                                }
                            }
                            maxUtility = Math.max(maxUtility, utility);
                        }
                        newUtilities[i][j] = cell.getStepCost() + discountFactor * maxUtility;
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
}
