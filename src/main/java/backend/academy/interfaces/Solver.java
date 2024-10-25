package backend.academy.interfaces;

import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.List;

/**
 * Интерфейс для решения лабиринтов.
 * Определяет метод для поиска пути от стартовой до конечной точки.
 */
public interface Solver {

    /**
     * Решает лабиринт, находя путь от стартовой до конечной точки.
     *
     * @param maze лабиринт для решения
     * @param start начальная точка
     * @param end конечная точка
     * @return список координат, представляющий найденный путь, или пустой список, если путь не найден
     */
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}
