package backend.academy.interfaces;

import backend.academy.models.Maze;

/**
 * Интерфейс для генераторов лабиринтов.
 * Определяет метод для генерации лабиринта заданного размера.
 */
public interface Generator {

    /**
     * Генерирует лабиринт с указанной высотой и шириной.
     *
     * @param height высота лабиринта
     * @param width ширина лабиринта
     * @return сгенерированный лабиринт
     */
    Maze generate(int height, int width);
}
