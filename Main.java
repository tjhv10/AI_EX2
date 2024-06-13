import Stracture.Grid;
import Stracture.Reward;
import Technics.PolicyIterationBoltzmann;
import Technics.SARSA;
import Technics.ValueIteration;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
        } catch (FileNotFoundException e) {}

        double alpha = 0.1;
        double epsilon = 0.1;
        int episodes = 1000;
        int puzzle;
        for (int i = 0; i < 1; i++) {
            puzzle = 0;
            for (Grid grid : grids) {
                System.out.println(puzzle++ + ":");

                // Measure time for Value Iteration
                long startTime = System.nanoTime();
                ValueIteration vi = new ValueIteration(grid);
                vi.run();
                long endTime = System.nanoTime();
                long viTime = (endTime - startTime) / 1000;
                System.out.println("Value Iteration Time: " + viTime + " µs");

                // System.out.println("Value Iteration Results:");
                // PrintingTools.printPolicy(vi.getPolicy(), grid);
                // System.out.println();

                // Measure time for PIB
                startTime = System.nanoTime();
                PolicyIterationBoltzmann PIB = new PolicyIterationBoltzmann(grid, 1, 0.5);
                PIB.run();
                endTime = System.nanoTime();
                long pibTime = (endTime - startTime) / 1000;
                System.out.println("PolicyIterationBoltzmann Time: " + pibTime + " µs");

                // System.out.println("PIB Results:");
                // PrintingTools.printPolicy(PIB.getPolicy(), grid);
                // System.out.println();

                // Measure time for SARSA
                startTime = System.nanoTime();
                SARSA sarsa = new SARSA(grid, alpha, epsilon, episodes);
                sarsa.run();
                endTime = System.nanoTime();
                long sarsaTime = (endTime - startTime) / 1000;
                System.out.println("SARSA Time: " + sarsaTime + " µs");

                // System.out.println("SARSA Results:");
                // PrintingTools.printPolicy(sarsa.getPolicy(), grid);
                // System.out.println();
            }
        }
    }
}
