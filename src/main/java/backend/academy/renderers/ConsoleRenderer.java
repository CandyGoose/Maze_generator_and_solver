package backend.academy.renderers;

import backend.academy.interfaces.Renderer;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
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

    private static final int FIRST_SPACING_POSITION = 2;
    private static final int SECOND_SPACING_POSITION = 7;
    private static final int SPACING_FACTOR = 10;

    @Override
    public String render(Maze maze) {
        return renderMaze(maze, null, null, null);
    }

    @Override
    public String render(Maze maze, List<Coordinate> path) {
        return renderMaze(maze, path, null, null);
    }

    public String renderWithLabels(Maze maze) {
        return renderMaze(maze, null, null, null, true);
    }

    public String renderWithPoints(Maze maze, Coordinate start, Coordinate end) {
        return renderMaze(maze, null, start, end);
    }

    public String renderWithPathAndPoints(Maze maze, List<Coordinate> path, Coordinate start, Coordinate end) {
        return renderMaze(maze, path, start, end);
    }

    private String renderMaze(Maze maze, List<Coordinate> path, Coordinate start, Coordinate end) {
        return renderMaze(maze, path, start, end, false);
    }

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

    private boolean[][] createPathMap(Maze maze, List<Coordinate> path) {
        boolean[][] pathMap = new boolean[maze.getHeight()][maze.getWidth()];
        if (path != null) {
            for (Coordinate coord : path) {
                pathMap[coord.row()][coord.col()] = true;
            }
        }
        return pathMap;
    }

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
