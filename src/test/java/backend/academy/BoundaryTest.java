package backend.academy;

import backend.academy.generators.PrimGenerator;
import backend.academy.models.Maze;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoundaryTest {

    @Test
    public void testMaxMazeSize() {
        PrimGenerator generator = new PrimGenerator();
        Maze maze = generator.generate(1000, 1000);

        assertEquals(1001, maze.getHeight(), "Размер лабиринта должен быть нечётным.");
        assertEquals(1001, maze.getWidth(), "Размер лабиринта должен быть нечётным.");
    }
}
