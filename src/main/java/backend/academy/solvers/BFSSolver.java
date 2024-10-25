package backend.academy.solvers;

import backend.academy.interfaces.Solver;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Класс для нахождения кратчайшего пути в лабиринте с использованием алгоритма поиска в ширину (BFS).
 * Реализует интерфейс Solver и находит путь в лабиринте от начальной до конечной точки.
 */
public class BFSSolver implements Solver {

    /**
     * Находит кратчайший путь от начальной до конечной точки в лабиринте с помощью BFS.
     *
     * @param maze лабиринт для решения
     * @param start начальная точка
     * @param end конечная точка
     * @return список координат, представляющий путь, или пустой список, если путь не найден
     */
    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        int height = maze.getHeight();
        int width = maze.getWidth();
        boolean[][] visited = new boolean[height][width];  // Отслеживание посещенных клеток
        Coordinate[][] parent = new Coordinate[height][width];  // Хранение предшественников для восстановления пути

        Queue<Coordinate> queue = new LinkedList<>();
        queue.add(start);
        visited[start.row()][start.col()] = true;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Варианты перемещения: вверх, вниз, влево, вправо

        // Основной цикл BFS
        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            // Завершаем, если достигли конечной точки
            if (current.equals(end)) {
                break;
            }

            // Проверка и добавление соседей в очередь
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

        // Восстанавливаем путь от конечной точки к начальной
        return reconstructPath(parent, start, end);
    }

    /**
     * Проверяет, является ли клетка допустимой для перемещения.
     *
     * @param maze лабиринт
     * @param row строка клетки
     * @param col столбец клетки
     * @param visited массив посещенных клеток
     * @return true, если клетка доступна для перемещения и еще не была посещена
     */
    private boolean isValid(Maze maze, int row, int col, boolean[][] visited) {
        return row >= 0 && row < maze.getHeight() && col >= 0 && col < maze.getWidth()
            && maze.getGrid()[row][col].type() == Cell.Type.PASSAGE && !visited[row][col];
    }

    /**
     * Восстанавливает путь от конечной точки к начальной, используя массив предшественников.
     *
     * @param parent массив предшественников для каждой клетки
     * @param start начальная точка
     * @param end конечная точка
     * @return список координат, представляющий путь от начальной до конечной точки
     */
    private List<Coordinate> reconstructPath(Coordinate[][] parent, Coordinate start, Coordinate end) {
        List<Coordinate> path = new ArrayList<>();
        Coordinate current = end;

        // Добавляем каждую клетку пути, двигаясь от конечной точки к начальной
        while (current != null && !current.equals(start)) {
            path.add(current);
            current = parent[current.row()][current.col()];
        }

        // Если путь не найден, возвращаем пустой список
        if (current == null) {
            return List.of();
        }

        path.add(start);

        // Разворачиваем путь, чтобы он шел от начальной точки к конечной
        List<Coordinate> reversed = new ArrayList<>();
        for (int i = path.size() - 1; i >= 0; i--) {
            reversed.add(path.get(i));
        }
        return reversed;
    }
}
