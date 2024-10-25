package backend.academy.interfaces;

import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.List;

/**
 * Интерфейс для отрисовки лабиринтов.
 * Определяет различные методы для визуализации лабиринта с элементами,
 * такими как метки, стартовая и конечная точки, а также пути.
 */
public interface Renderer {

    /**
     * Визуализирует лабиринт в виде строки.
     *
     * @param maze лабиринт для отрисовки
     * @return строковое представление лабиринта
     */
    String render(Maze maze);

    /**
     * Визуализирует лабиринт с выделением пути.
     *
     * @param maze лабиринт для отрисовки
     * @param path список координат, представляющий путь
     * @return строковое представление лабиринта с выделенным путем
     */
    String render(Maze maze, List<Coordinate> path);

    /**
     * Визуализирует лабиринт с метками на клетках.
     *
     * @param maze лабиринт для отрисовки
     * @return строковое представление лабиринта с метками
     */
    String renderWithLabels(Maze maze);

    /**
     * Визуализирует лабиринт с обозначением стартовой и конечной точек.
     *
     * @param maze лабиринт для отрисовки
     * @param start начальная точка
     * @param end конечная точка
     * @return строковое представление лабиринта с выделенными точками
     */
    String renderWithPoints(Maze maze, Coordinate start, Coordinate end);

    /**
     * Визуализирует лабиринт с выделением пути, стартовой и конечной точек.
     *
     * @param maze лабиринт для отрисовки
     * @param path список координат, представляющий путь
     * @param start начальная точка
     * @param end конечная точка
     * @return строковое представление лабиринта с выделенным путем и точками
     */
    String renderWithPathAndPoints(Maze maze, List<Coordinate> path, Coordinate start, Coordinate end);
}
