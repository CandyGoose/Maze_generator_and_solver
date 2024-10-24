package backend.academy.interfaces;

import backend.academy.models.Maze;
import backend.academy.models.Coordinate;
import java.util.List;

public interface Renderer {
    String render(Maze maze);
    String render(Maze maze, List<Coordinate> path);
}
