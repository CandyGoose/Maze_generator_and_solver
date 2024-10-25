package backend.academy.renderers;

import backend.academy.interfaces.Renderer;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.List;

/**
 * Класс для консольного отображения лабиринта с использованием emoji.
 * Реализует интерфейс Renderer и предоставляет различные способы визуализации
 * лабиринта, включая отображение пути, меток, начальной и конечной точек.
 */
public class ConsoleRenderer implements Renderer {
    private static final String WALL_EMOJI = "\u2B1B";      // Черный квадрат (стена)
    private static final String PASSAGE_EMOJI = "\u2B1C";   // Белый квадрат (обычный проход)
    private static final String START_EMOJI = "\uD83D\uDFE9"; // Зеленый квадрат (начало)
    private static final String END_EMOJI = "\uD83D\uDFE5";   // Красный квадрат (конец)
    private static final String PATH_EMOJI = "\uD83D\uDFE8";  // Желтый квадрат (путь)
    private static final String SWAMP_EMOJI = "\uD83D\uDC38";   // Болото
    private static final String SAND_EMOJI = "\uD83C\uDFDC\uFE0F"; // Песок
    private static final String COIN_EMOJI = "\uD83D\uDC8E";    // Монета
    private static final String ROAD_EMOJI = "\uD83D\uDE88";    // Дорога

    private static final int FIRST_SPACING_POSITION = 2;
    private static final int SECOND_SPACING_POSITION = 7;
    private static final int SPACING_FACTOR = 10;

    /**
     * Отображает лабиринт в консоли без дополнительных элементов.
     *
     * @param maze лабиринт для отображения
     * @return строка с текстовым представлением лабиринта
     */
    @Override
    public String render(Maze maze) {
        return renderMaze(maze, null, null, null);
    }

    /**
     * Отображает лабиринт с выделением пути.
     *
     * @param maze лабиринт для отображения
     * @param path путь, который нужно выделить
     * @return строка с текстовым представлением лабиринта с выделенным путем
     */
    @Override
    public String render(Maze maze, List<Coordinate> path) {
        return renderMaze(maze, path, null, null);
    }

    /**
     * Отображает лабиринт с метками для строк и столбцов.
     *
     * @param maze лабиринт для отображения
     * @return строка с текстовым представлением лабиринта и метками
     */
    public String renderWithLabels(Maze maze) {
        return renderMaze(maze, null, null, null, true);
    }

    /**
     * Отображает лабиринт с начальной и конечной точками.
     *
     * @param maze лабиринт для отображения
     * @param start координаты начальной точки
     * @param end координаты конечной точки
     * @return строка с текстовым представлением лабиринта с выделением начальной и конечной точек
     */
    public String renderWithPoints(Maze maze, Coordinate start, Coordinate end) {
        return renderMaze(maze, null, start, end);
    }

    /**
     * Отображает лабиринт с путем, начальной и конечной точками.
     *
     * @param maze лабиринт для отображения
     * @param path путь, который нужно выделить
     * @param start координаты начальной точки
     * @param end координаты конечной точки
     * @return строка с текстовым представлением лабиринта с путем и выделенными точками
     */
    public String renderWithPathAndPoints(Maze maze, List<Coordinate> path, Coordinate start, Coordinate end) {
        return renderMaze(maze, path, start, end);
    }

    // Основной метод отрисовки лабиринта
    private String renderMaze(Maze maze, List<Coordinate> path, Coordinate start, Coordinate end) {
        return renderMaze(maze, path, start, end, false);
    }

    /**
     * Основной метод отрисовки лабиринта, с дополнительной возможностью отображения меток.
     *
     * @param maze лабиринт для отображения
     * @param path путь, который нужно выделить (может быть null)
     * @param start координаты начальной точки (может быть null)
     * @param end координаты конечной точки (может быть null)
     * @param showLabels если true, отображает метки для строк и столбцов
     * @return строка с текстовым представлением лабиринта
     */
    private String renderMaze(Maze maze, List<Coordinate> path, Coordinate start, Coordinate end, boolean showLabels) {
        StringBuilder sb = new StringBuilder();
        Cell[][] grid = maze.getGrid();
        boolean[][] pathMap = createPathMap(maze, path);
        int height = maze.getHeight();
        int width = maze.getWidth();

        int maxRowLabel = height - 1;
        int maxRowDigits = String.valueOf(maxRowLabel).length();
        int rowLabelWidth = Math.max(maxRowDigits, 2);

        if (showLabels) {
            sb.append(formatColumnLabels(width, rowLabelWidth));
        }

        for (int row = 0; row < height; row++) {
            if (showLabels) {
                sb.append(String.format("%" + rowLabelWidth + "d ", row));
            }
            for (int col = 0; col < width; col++) {
                sb.append(getCellRepresentation(grid[row][col], pathMap[row][col], start, end, row, col));
                sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Создает карту пути, чтобы отметить клетки, принадлежащие пути.
     *
     * @param maze лабиринт
     * @param path путь, который нужно отобразить
     * @return карта пути в виде двумерного массива boolean
     */
    private boolean[][] createPathMap(Maze maze, List<Coordinate> path) {
        boolean[][] pathMap = new boolean[maze.getHeight()][maze.getWidth()];
        if (path != null) {
            for (Coordinate coord : path) {
                pathMap[coord.row()][coord.col()] = true;
            }
        }
        return pathMap;
    }

    /**
     * Возвращает строковое представление клетки на основе ее типа, расположения на пути,
     * начальной и конечной точки.
     *
     * @param cell клетка
     * @param isOnPath true, если клетка является частью пути
     * @param start начальная точка
     * @param end конечная точка
     * @param row строка клетки
     * @param col столбец клетки
     * @return emoji-представление клетки
     */
    private String getCellRepresentation(Cell cell, boolean isOnPath, Coordinate start, Coordinate end,
        int row, int col) {
        if (start != null && row == start.row() && col == start.col()) {
            return START_EMOJI;
        } else if (end != null && row == end.row() && col == end.col()) {
            return END_EMOJI;
        } else if (isOnPath) {
            return PATH_EMOJI;
        } else {
            return getEmojiForCell(cell);
        }
    }

    /**
     * Возвращает emoji-представление клетки в зависимости от ее типа и поверхности.
     *
     * @param cell клетка
     * @return emoji-представление клетки
     */
    private String getEmojiForCell(Cell cell) {
        if (cell.type() == Cell.Type.WALL) {
            return WALL_EMOJI;
        } else {
            return switch (cell.getSurface()) {
                case SWAMP -> SWAMP_EMOJI;
                case SAND -> SAND_EMOJI;
                case COIN -> COIN_EMOJI;
                case ROAD -> ROAD_EMOJI;
                default -> PASSAGE_EMOJI;
            };
        }
    }

    /**
     * Форматирует метки для столбцов в верхней части лабиринта.
     *
     * @param width ширина лабиринта
     * @param rowLabelWidth ширина метки строки
     * @return строка с метками для столбцов
     */
    private String formatColumnLabels(int width, int rowLabelWidth) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ".repeat(rowLabelWidth + 1));
        for (int col = 0; col < width; col++) {
            sb.append(String.format("%2d", col));
            if (col % SPACING_FACTOR == FIRST_SPACING_POSITION || col % SPACING_FACTOR == SECOND_SPACING_POSITION) {
                sb.append("  ");
            } else {
                sb.append(" ");
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
