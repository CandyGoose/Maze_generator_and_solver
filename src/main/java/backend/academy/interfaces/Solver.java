package backend.academy.interfaces;


import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.List;

public interface Solver {
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}
