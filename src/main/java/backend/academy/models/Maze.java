package backend.academy.models;

import backend.academy.utils.RandomUtils;

/**
 * Лабиринт, состоящий из клеток. Содержит методы генерации и настройки лабиринта.
 */
public final class Maze {
    private final int height;  // Высота лабиринта
    private final int width;   // Ширина лабиринта
    private final Cell[][] grid;  // Сетка клеток лабиринта

    // Вероятности различных поверхностей
    private static final int SWAMP_CHANCE = 5;
    private static final int SAND_CHANCE = 15;
    private static final int COIN_CHANCE = 20;
    private static final int ROAD_CHANCE = 30;
    private static final int MAX_CHANCE = 100;
    private static final double CYCLE_CHANCE = 0.1;  // Вероятность добавления цикла

    /**
     * Создает лабиринт заданного размера с начальной установкой всех клеток как стены.
     *
     * @param height высота лабиринта
     * @param width ширина лабиринта
     */
    public Maze(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL, SurfaceType.NORMAL);
            }
        }
    }

    /**
     * Возвращает случайный тип поверхности для клетки на основе вероятностей.
     *
     * @return случайный тип поверхности
     */
    public SurfaceType getRandomSurface() {
        int chance = RandomUtils.nextInt(MAX_CHANCE);
        SurfaceType surfaceType = SurfaceType.NORMAL;

        if (chance < SWAMP_CHANCE) {
            surfaceType = SurfaceType.SWAMP;
        } else if (chance < SAND_CHANCE) {
            surfaceType = SurfaceType.SAND;
        } else if (chance < COIN_CHANCE) {
            surfaceType = SurfaceType.COIN;
        } else if (chance < ROAD_CHANCE) {
            surfaceType = SurfaceType.ROAD;
        }

        return surfaceType;
    }

    /**
     * Пробивает стену между двумя соседними клетками.
     *
     * @param current первая клетка
     * @param chosen вторая клетка
     */
    public void removeWall(Coordinate current, Coordinate chosen) {
        int wallRow = (current.row() + chosen.row()) / 2;
        int wallCol = (current.col() + chosen.col()) / 2;
        grid[wallRow][wallCol] = new Cell(wallRow, wallCol, Cell.Type.PASSAGE, getRandomSurface());
    }

    /**
     * Проверяет, находится ли клетка в пределах лабиринта.
     *
     * @param row строка клетки
     * @param col столбец клетки
     * @return true, если клетка в пределах лабиринта
     */
    public boolean isInBounds(int row, int col) {
        return row > 0 && row < height - 1 && col > 0 && col < width - 1;
    }

    /**
     * Добавляет циклы в лабиринт, пробивая стены между проходами.
     */
    public void addCycles() {
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) {
                if (isWallBetweenPassages(row, col) && shouldCreateCycle()) {
                    if (canCreateCycle(row, col)) {
                        grid[row][col] = new Cell(row, col, Cell.Type.PASSAGE, getRandomSurface());
                    }
                }
            }
        }
    }

    // Проверяет, является ли клетка стеной между проходами
    private boolean isWallBetweenPassages(int row, int col) {
        return grid[row][col].type() == Cell.Type.WALL
            && ((row % 2 == 1 && col % 2 == 0) || (row % 2 == 0 && col % 2 == 1));
    }

    // Определяет, должна ли стена быть пробита для создания цикла
    private boolean shouldCreateCycle() {
        return RandomUtils.chance(CYCLE_CHANCE);
    }

    // Проверяет, возможно ли создание цикла путем пробития стены
    private boolean canCreateCycle(int row, int col) {
        int passages = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (grid[newRow][newCol].type() == Cell.Type.PASSAGE) {
                passages++;
            }
        }
        return passages == 2;
    }

    // Геттеры для получения размеров лабиринта и сетки клеток
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Cell[][] getGrid() {
        return grid;
    }
}
