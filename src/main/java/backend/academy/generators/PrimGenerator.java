package backend.academy.generators;

import backend.academy.interfaces.Generator;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация алгоритма Прима для генерации лабиринта. Алгоритм создает лабиринт с единственным
 * путем между двумя любыми точками и возможностью добавления циклов.
 */
public class PrimGenerator implements Generator {

    /**
     * Генерирует лабиринт заданной высоты и ширины с использованием алгоритма Прима.
     * @param height высота лабиринта
     * @param width ширина лабиринта
     * @return сгенерированный лабиринт
     */
    @Override
    public Maze generate(int height, int width) {
        // Приводим размеры лабиринта к нечетным для корректной генерации
        int adjustedHeight = height % 2 == 0 ? height + 1 : height;
        int adjustedWidth = width % 2 == 0 ? width + 1 : width;

        Maze maze = new Maze(adjustedHeight, adjustedWidth);
        List<Coordinate> wallList = new ArrayList<>();

        // Начальная точка лабиринта
        int startRow = 1;
        int startCol = 1;
        maze.getGrid()[startRow][startCol] = new Cell(startRow, startCol, Cell.Type.PASSAGE, maze.getRandomSurface());
        addWalls(maze, startRow, startCol, wallList);

        // Основной цикл генерации
        while (!wallList.isEmpty()) {
            int randIndex = RandomUtils.nextInt(wallList.size());
            Coordinate wall = wallList.remove(randIndex);

            // Получаем список клеток по обе стороны стены
            List<Coordinate> adjacentCells = getAdjacentCells(maze, wall);

            // Продолжаем, если стена не находится между двумя клетками
            if (adjacentCells.size() != 2) {
                continue;
            }

            Coordinate cell1 = adjacentCells.get(0);
            Coordinate cell2 = adjacentCells.get(1);

            boolean cell1InMaze = maze.getGrid()[cell1.row()][cell1.col()].type() == Cell.Type.PASSAGE;
            boolean cell2InMaze = maze.getGrid()[cell2.row()][cell2.col()].type() == Cell.Type.PASSAGE;

            // Проверка, что только одна из соседних клеток уже является частью лабиринта
            if (cell1InMaze ^ cell2InMaze) {
                Coordinate inMaze = cell1InMaze ? cell1 : cell2;
                Coordinate notInMaze = cell1InMaze ? cell2 : cell1;

                // Убираем стену между клетками и добавляем новую клетку к лабиринту
                maze.removeWall(inMaze, notInMaze);
                maze.getGrid()[notInMaze.row()][notInMaze.col()] = new Cell(notInMaze.row(), notInMaze.col(),
                    Cell.Type.PASSAGE, maze.getRandomSurface());

                // Добавляем соседние стены новой клетки в список для обработки
                addWalls(maze, notInMaze.row(), notInMaze.col(), wallList);
            }
        }

        // Добавляем циклы в лабиринт после завершения основной генерации
        maze.addCycles();

        return maze;
    }

    /**
     * Добавляет соседние стены клетки в список для обработки.
     * @param maze текущий лабиринт
     * @param row строка клетки
     * @param col столбец клетки
     * @param wallList список стен
     */
    private void addWalls(Maze maze, int row, int col, List<Coordinate> wallList) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (maze.isInBounds(newRow, newCol) && maze.getGrid()[newRow][newCol].type() == Cell.Type.WALL) {
                Coordinate wall = new Coordinate(newRow, newCol);
                if (!wallList.contains(wall)) {
                    wallList.add(wall);
                }
            }
        }
    }

    /**
     * Находит клетки, смежные со стеной, которые могут быть соединены.
     * @param maze текущий лабиринт
     * @param wall координаты стены
     * @return список соседних клеток по обе стороны от стены
     */
    private List<Coordinate> getAdjacentCells(Maze maze, Coordinate wall) {
        List<Coordinate> cells = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int adjRow = wall.row() + dir[0];
            int adjCol = wall.col() + dir[1];
            if (maze.isInBounds(adjRow, adjCol) && isCell(wall, dir)) {
                cells.add(new Coordinate(adjRow, adjCol));
            }
        }
        return cells;
    }

    /**
     * Проверяет, является ли клетка допустимой (находится ли она на допустимом расстоянии от стены).
     * @param wall координаты стены
     * @param dir направление
     * @return true, если клетка является допустимой
     */
    private boolean isCell(Coordinate wall, int[] dir) {
        return (wall.row() + dir[0]) % 2 != 0 && (wall.col() + dir[1]) % 2 != 0;
    }
}
