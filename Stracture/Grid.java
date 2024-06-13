package Stracture;
import java.awt.Point;

public class Grid {
    private final int width;
    private final int height;
    private final Cell[][] grid;
    private final double p;
    private final Reward[] rewards;
    private final double stepCost;
    

    public Grid(int width, int height, Reward[] rewards, double stepCost, Point[] walls, double p) {
        this.width = width;
        this.height = height;
        this.rewards = rewards;
        this.stepCost = stepCost;
        grid = new Cell[height][width];
        this.p = p;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(stepCost, CellType.NORMAL);
            }
        }

        for (Reward reward : rewards) {
            grid[reward.getY()][reward.getX()].setReward(reward.getReward());
        }

        for (Point wall : walls) {
            grid[(int) wall.getY()][(int) wall.getX()] = new Cell(0, CellType.WALL);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Reward[] getRewards() {
        return rewards;
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public double getP() {
        return p;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grid (").append(width).append("x").append(height).append(")\n");
        sb.append("Step Cost: ").append(stepCost).append("\n");
        sb.append("Transition Probability: ").append(p).append("\n");
        sb.append("Rewards:\n");

        for (Reward reward : rewards) {
            sb.append("  (").append(reward.getX()).append(", ").append(reward.getY())
              .append(") -> ").append(reward.getReward()).append("\n");
        }

        sb.append("Grid Layout:\n");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = grid[i][j];
                if (cell.getCellType() == CellType.WALL) {
                    sb.append(" W ");
                } else {
                    sb.append(String.format("%4.1f", cell.getReward())).append(" ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
