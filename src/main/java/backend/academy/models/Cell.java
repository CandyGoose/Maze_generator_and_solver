package backend.academy.models;

public record Cell(int row, int col, Type type, SurfaceType surface) {
    public enum Type { WALL, PASSAGE }

    public SurfaceType getSurface() {
        return surface;
    }
}
