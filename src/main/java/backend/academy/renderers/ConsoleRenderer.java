package backend.academy.renderers;

import backend.academy.interfaces.Renderer;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.models.SurfaceType;
import java.util.List;

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

    @Override
    public String render(Maze maze) {
        StringBuilder sb = new StringBuilder();
        Cell[][] grid = maze.getGrid();
        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                Cell cell = grid[row][col];
                sb.append(getEmojiForCell(cell));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String render(Maze maze, List<Coordinate> path) {
        StringBuilder sb = new StringBuilder();
        Cell[][] grid = maze.getGrid();
        boolean[][] pathMap = new boolean[maze.getHeight()][maze.getWidth()];
        for (Coordinate coord : path) {
            pathMap[coord.row()][coord.col()] = true;
        }

        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                if (pathMap[row][col]) {
                    sb.append(PATH_EMOJI);
                } else {
                    Cell cell = grid[row][col];
                    sb.append(getEmojiForCell(cell));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getEmojiForCell(Cell cell) {
        if (cell.type() == Cell.Type.WALL) {
            return WALL_EMOJI;
        } else {
            return switch (cell.getSurface()) {
                case SWAMP -> SWAMP_EMOJI;   // Болото
                case SAND -> SAND_EMOJI;     // Песок
                case COIN -> COIN_EMOJI;     // Монетка
                case ROAD -> ROAD_EMOJI;     // Дорога
                default -> PASSAGE_EMOJI;    // Обычный проход
            };
        }
    }

    public String renderWithLabels(Maze maze) {
        StringBuilder sb = new StringBuilder();
        int height = maze.getHeight();
        int width = maze.getWidth();
        Cell[][] grid = maze.getGrid();

        int maxRowLabel = height - 1;
        int maxRowDigits = String.valueOf(maxRowLabel).length();
        int rowLabelWidth = Math.max(maxRowDigits, 2);

        sb.append(formatColumnLabels(width, rowLabelWidth));

        for (int row = 0; row < height; row++) {
            sb.append(String.format("%" + rowLabelWidth + "d ", row));
            for (int col = 0; col < width; col++) {
                Cell cell = grid[row][col];
                sb.append(getEmojiForCell(cell));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String renderWithPoints(Maze maze, Coordinate start, Coordinate end) {
        StringBuilder sb = new StringBuilder();
        int height = maze.getHeight();
        int width = maze.getWidth();
        Cell[][] grid = maze.getGrid();

        int maxRowLabel = height - 1;
        int maxRowDigits = String.valueOf(maxRowLabel).length();
        int rowLabelWidth = Math.max(maxRowDigits, 2);

        sb.append(formatColumnLabels(width, rowLabelWidth));

        for (int row = 0; row < height; row++) {
            sb.append(String.format("%" + rowLabelWidth + "d ", row));
            for (int col = 0; col < width; col++) {
                if (row == start.row() && col == start.col()) {
                    sb.append(START_EMOJI);
                } else if (row == end.row() && col == end.col()) {
                    sb.append(END_EMOJI);
                } else {
                    Cell cell = grid[row][col];
                    sb.append(getEmojiForCell(cell));
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String renderWithPathAndPoints(Maze maze, List<Coordinate> path, Coordinate start, Coordinate end) {
        StringBuilder sb = new StringBuilder();
        int height = maze.getHeight();
        int width = maze.getWidth();
        Cell[][] grid = maze.getGrid();

        boolean[][] pathMap = new boolean[height][width];
        for (Coordinate coord : path) {
            pathMap[coord.row()][coord.col()] = true;
        }

        int maxRowLabel = height - 1;
        int maxRowDigits = String.valueOf(maxRowLabel).length();
        int rowLabelWidth = Math.max(maxRowDigits, 2);

        sb.append(formatColumnLabels(width, rowLabelWidth));

        for (int row = 0; row < height; row++) {
            sb.append(String.format("%" + rowLabelWidth + "d ", row));
            for (int col = 0; col < width; col++) {
                if (row == start.row() && col == start.col()) {
                    sb.append(START_EMOJI);
                } else if (row == end.row() && col == end.col()) {
                    sb.append(END_EMOJI);
                } else if (pathMap[row][col]) {
                    sb.append(PATH_EMOJI);
                } else {
                    Cell cell = grid[row][col];
                    sb.append(getEmojiForCell(cell));
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String formatColumnLabels(int width, int rowLabelWidth) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ".repeat(rowLabelWidth + 1));
        for (int col = 0; col < width; col++) {
            sb.append(String.format("%2d", col));
            if (col % 10 == 2 || col % 10 == 7) {
                sb.append("  ");
            } else {
                sb.append(" ");
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
