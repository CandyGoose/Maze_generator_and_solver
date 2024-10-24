package backend.academy.generators;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimGenerator {

    private final Random random = new Random();

    public Maze generate(int height, int width) {
        if (height % 2 == 0) height += 1;
        if (width % 2 == 0) width += 1;

        Maze maze = new Maze(height, width);
        List<Coordinate> wallList = new ArrayList<>();

        int startRow = 1;
        int startCol = 1;
        maze.setCell(startRow, startCol, Cell.Type.PASSAGE);

        addWalls(maze, startRow, startCol, wallList);

        while (!wallList.isEmpty()) {
            int randIndex = random.nextInt(wallList.size());
            Coordinate wall = wallList.remove(randIndex);

            List<Coordinate> adjacentCells = getAdjacentCells(maze, wall);

            if (adjacentCells.size() != 2) {
                continue;
            }

            Coordinate cell1 = adjacentCells.get(0);
            Coordinate cell2 = adjacentCells.get(1);

            boolean cell1InMaze = maze.getGrid()[cell1.row()][cell1.col()].type() == Cell.Type.PASSAGE;
            boolean cell2InMaze = maze.getGrid()[cell2.row()][cell2.col()].type() == Cell.Type.PASSAGE;

            if (cell1InMaze ^ cell2InMaze) {
                Coordinate newCell = cell1InMaze ? cell2 : cell1;

                maze.setCell(wall.row(), wall.col(), Cell.Type.PASSAGE);
                maze.setCell(newCell.row(), newCell.col(), Cell.Type.PASSAGE);

                addWalls(maze, newCell.row(), newCell.col(), wallList);
            }
        }

        return maze;
    }

    private void addWalls(Maze maze, int row, int col, List<Coordinate> wallList) {
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isInBounds(maze, newRow, newCol) && maze.getGrid()[newRow][newCol].type() == Cell.Type.WALL) {
                wallList.add(new Coordinate(newRow, newCol));
            }
        }
    }

    private boolean isInBounds(Maze maze, int row, int col) {
        return row > 0 && row < maze.getHeight() - 1 && col > 0 && col < maze.getWidth() - 1;
    }

    private List<Coordinate> getAdjacentCells(Maze maze, Coordinate wall) {
        List<Coordinate> cells = new ArrayList<>();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = wall.row() + dir[0];
            int adjCol = wall.col() + dir[1];
            if (isInBounds(maze, adjRow, adjCol) && (adjRow % 2 != 0 && adjCol % 2 != 0)) {
                cells.add(new Coordinate(adjRow, adjCol));
            }
        }
        return cells;
    }
}
