package backend.academy.models;

/**
 * Клетка лабиринта, описываемая координатами, типом (проход или стена) и типом поверхности.
 */
public record Cell(int row, int col, Type type, SurfaceType surface) {

    public enum Type { WALL, PASSAGE }

    /**
     * Возвращает тип поверхности клетки.
     *
     * @return тип поверхности клетки
     */
    public SurfaceType getSurface() {
        return surface;
    }
}
