package backend.academy.interfaces;

import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.List;

public interface Renderer {
    String render(Maze maze);

    String render(Maze maze, List<Coordinate> path);

    String renderWithLabels(Maze maze);

    String renderWithPoints(Maze maze, Coordinate start, Coordinate end);

    String renderWithPathAndPoints(Maze maze, List<Coordinate> path, Coordinate start, Coordinate end);
}
