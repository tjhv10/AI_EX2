package Tools;
public class Cell {
    private double stepCost;
    private double reward;
    private CellType cellType;

    public Cell(double stepCost, CellType cellType) {
        this.stepCost = stepCost;
        this.cellType = cellType;
        this.reward = 0;  // Default reward, can be changed later if needed
    }

    public double getStepCost() {
        return stepCost;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
        this.cellType = CellType.REWARD;
    }

    public CellType getCellType() {
        return cellType;
    }
}
