public class Cell {
    private double reward;
    private double stepCost;

    public Cell(double stepCost) {
        this.reward = 0;
        this.stepCost = stepCost;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public double getStepCost() {
        return stepCost;
    }
}
