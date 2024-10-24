package backend.academy.solvers;

import backend.academy.interfaces.Solver;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;

import java.util.*;

public class AStarSolver implements Solver {

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Map<Coordinate, Coordinate> cameFrom = new HashMap<>();
        Map<Coordinate, Integer> gScore = new HashMap<>();

        gScore.put(start, 0);
        openSet.add(new Node(start, heuristic(start, end)));

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            Coordinate current = currentNode.coordinate;

            if (current.equals(end)) {
                return reconstructPath(cameFrom, current);
            }

            for (Coordinate neighbor : getNeighbors(maze, current)) {
                int tentativeG = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;
                if (tentativeG < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    int f = tentativeG + heuristic(neighbor, end);
                    openSet.add(new Node(neighbor, f));
                }
            }
        }

        return List.of();
    }

    private List<Coordinate> getNeighbors(Maze maze, Coordinate coord) {
        List<Coordinate> neighbors = new ArrayList<>();
        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

        for (int[] dir : directions) {
            int newRow = coord.row() + dir[0];
            int newCol = coord.col() + dir[1];
            if (isValid(maze, newRow, newCol)) {
                neighbors.add(new Coordinate(newRow, newCol));
            }
        }
        return neighbors;
    }

    private boolean isValid(Maze maze, int row, int col) {
        return row >= 0 && row < maze.getHeight() && col >= 0 && col < maze.getWidth()
            && maze.getGrid()[row][col].type() == Cell.Type.PASSAGE;
    }

    private int heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
    }

    private List<Coordinate> reconstructPath(Map<Coordinate, Coordinate> cameFrom, Coordinate current) {
        List<Coordinate> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private static class Node {
        Coordinate coordinate;
        int f;

        Node(Coordinate coordinate, int f) {
            this.coordinate = coordinate;
            this.f = f;
        }
    }
}
