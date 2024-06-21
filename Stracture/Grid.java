package Stracture;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    public static List<Grid> readFile()
    {
        List<Grid> grids = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("grids.txt"))) { // try-with-resources statement
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                if (line.startsWith("#")) {
                    continue; // Skip comments
                }

                int width = 0;
                int height = 0;
                List<Reward> rewardList = new ArrayList<>();
                double stepCost = 0;
                double probability = 0;

                while (scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("#")) {
                    if (line.startsWith("w = ")) {
                        width = Integer.parseInt(line.split(" = ")[1]);
                    } else if (line.startsWith("h = ")) {
                        height = Integer.parseInt(line.split(" = ")[1]);
                    } else if (line.startsWith("L = ")) {
                        String[] rewardsStr = line.split(" = ")[1].replace("[", "").replace("]", "").split("\\),\\(");
                        for (String rewardStr : rewardsStr) {
                            rewardStr = rewardStr.replace("(", "").replace(")", "");
                            String[] rewardParts = rewardStr.split(",");
                            rewardList.add(new Reward(Integer.parseInt(rewardParts[0]), Integer.parseInt(rewardParts[1]), Double.parseDouble(rewardParts[2])));
                        }
                    } else if (line.startsWith("p = ")) {
                        probability = Double.parseDouble(line.split(" = ")[1]);
                    } else if (line.startsWith("r = ")) {
                        stepCost = Double.parseDouble(line.split(" = ")[1]);
                    }
                }

                Reward[] rewards = rewardList.toArray(Reward[]::new);

                int size = 0;
                for (Reward reward : rewardList) {
                    if (reward.getReward() == 0) {
                        size++;
                    }
                }

                Point[] walls = new Point[size];
                int j = 0;
                for (Reward reward : rewardList) {
                    if (reward.getReward() == 0) {
                        walls[j++] = new Point(reward.getX(), reward.getY());
                    }
                }
                grids.add(new Grid(width, height, rewards, stepCost, walls, probability));
            }
        } catch (FileNotFoundException e) {
        }
        return grids;
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
