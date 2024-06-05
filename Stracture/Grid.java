package Stracture;
import java.awt.Point;

public class Grid {
    private int width;
    private int height;
    private Cell[][] grid;

    public Grid(int width, int height, Reward[] rewards, double stepCost, Point[] walls) {
        this.width = width;
        this.height = height;
        grid = new Cell[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(stepCost, CellType.NORMAL);
            }
        }

        for (Reward reward : rewards) {
            grid[reward.getX()][reward.getY()].setReward(reward.getReward());
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

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }
}
