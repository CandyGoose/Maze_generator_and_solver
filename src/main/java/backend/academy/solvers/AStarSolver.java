package backend.academy.solvers;

import backend.academy.interfaces.Solver;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Класс для нахождения пути в лабиринте с использованием алгоритма A*.
 * Реализует интерфейс Solver и находит оптимальный путь с учетом
 * различных типов поверхностей и их стоимости перемещения.
 */
public class AStarSolver implements Solver {

    /**
     * Находит оптимальный путь от начальной точки до конечной точки в лабиринте.
     *
     * @param maze лабиринт для решения
     * @param start начальная точка
     * @param end конечная точка
     * @return список координат, представляющий путь, или пустой список, если путь не найден
     */
    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));
        Set<Coordinate> closedSet = new HashSet<>();
        Map<Coordinate, Coordinate> cameFrom = new HashMap<>();
        Map<Coordinate, Double> gScore = new HashMap<>();

        // Инициализируем начальную точку с нулевой стоимостью пути
        gScore.put(start, 0.0);
        openSet.add(new Node(start, heuristic(start, end)));

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            Coordinate current = currentNode.coordinate;

            if (closedSet.contains(current)) {
                continue;
            }

            closedSet.add(current);

            // Если достигли конечной точки, строим путь
            if (current.equals(end)) {
                return reconstructPath(cameFrom, current);
            }

            // Проходим по соседям текущей клетки
            for (Coordinate neighbor : getNeighbors(maze, current)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                // Рассчитываем временную стоимость пути до соседа
                double tentativeG = gScore.getOrDefault(current, Double.MAX_VALUE)
                    + maze.getGrid()[neighbor.row()][neighbor.col()].getSurface().getCost();

                // Обновляем данные о пути, если нашли более короткий путь до соседа
                if (tentativeG < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    double f = tentativeG + heuristic(neighbor, end);
                    openSet.add(new Node(neighbor, f));
                }
            }
        }

        // Возвращаем пустой список, если путь не найден
        return List.of();
    }

    /**
     * Находит соседние клетки, доступные для перемещения.
     *
     * @param maze лабиринт
     * @param coord координаты текущей клетки
     * @return список координат соседних клеток
     */
    private List<Coordinate> getNeighbors(Maze maze, Coordinate coord) {
        List<Coordinate> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = coord.row() + dir[0];
            int newCol = coord.col() + dir[1];
            if (isValid(maze, newRow, newCol)) {
                neighbors.add(new Coordinate(newRow, newCol));
            }
        }
        return neighbors;
    }

    /**
     * Проверяет, является ли клетка допустимой для перемещения.
     *
     * @param maze лабиринт
     * @param row строка клетки
     * @param col столбец клетки
     * @return true, если клетка доступна для перемещения
     */
    private boolean isValid(Maze maze, int row, int col) {
        return row >= 0 && row < maze.getHeight() && col >= 0 && col < maze.getWidth()
            && maze.getGrid()[row][col].type() == Cell.Type.PASSAGE;
    }

    /**
     * Вычисляет эвристическое расстояние (манхэттенское расстояние) между двумя клетками.
     *
     * @param a начальная клетка
     * @param b конечная клетка
     * @return эвристическое значение расстояния
     */
    private double heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
    }

    /**
     * Восстанавливает путь от конечной точки к начальной на основе карты cameFrom.
     *
     * @param cameFrom карта, указывающая предшественников для каждой клетки
     * @param current текущая клетка, с которой начинается восстановление пути
     * @return список координат, представляющий восстановленный путь
     */
    private List<Coordinate> reconstructPath(Map<Coordinate, Coordinate> cameFrom, Coordinate current) {
        List<Coordinate> path = new ArrayList<>();
        Coordinate step = current;
        path.add(step);

        // Проходим от конечной точки к начальной
        while (cameFrom.containsKey(step)) {
            step = cameFrom.get(step);
            path.add(step);
        }

        Collections.reverse(path);
        return path;
    }

    /**
     * Вспомогательный класс для хранения координат и значения f для приоритетной очереди.
     */
    private static class Node {
        Coordinate coordinate;
        double f;

        Node(Coordinate coordinate, double f) {
            this.coordinate = coordinate;
            this.f = f;
        }
    }
}
