package backend.academy.solvers;

import backend.academy.interfaces.Solver;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;

import java.util.*;

public class BFSSolver implements Solver {

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        int height = maze.getHeight();
        int width = maze.getWidth();
        boolean[][] visited = new boolean[height][width];
        Coordinate[][] parent = new Coordinate[height][width];

        Queue<Coordinate> queue = new LinkedList<>();
        queue.add(start);
        visited[start.row()][start.col()] = true;

        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            if (current.equals(end)) {
                break;
            }

            for (int[] dir : directions) {
                int newRow = current.row() + dir[0];
                int newCol = current.col() + dir[1];
                if (isValid(maze, newRow, newCol, visited)) {
                    queue.add(new Coordinate(newRow, newCol));
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = current;
                }
            }
        }

        return reconstructPath(parent, start, end);
    }

    private boolean isValid(Maze maze, int row, int col, boolean[][] visited) {
        return row >= 0 && row < maze.getHeight() && col >= 0 && col < maze.getWidth()
            && maze.getGrid()[row][col].type() == Cell.Type.PASSAGE && !visited[row][col];
    }

    private List<Coordinate> reconstructPath(Coordinate[][] parent, Coordinate start, Coordinate end) {
        List<Coordinate> path = new ArrayList<>();
        Coordinate current = end;
        while (current != null && !current.equals(start)) {
            path.add(current);
            current = parent[current.row()][current.col()];
        }
        if (current == null) {
            return List.of();
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }
}
