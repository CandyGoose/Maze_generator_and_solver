package backend.academy.generators;

import backend.academy.models.Cell;
import backend.academy.models.Maze;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecursiveBacktrackerGeneratorTest {

    private RecursiveBacktrackerGenerator generator;

    @BeforeEach
    public void setup() {
        generator = new RecursiveBacktrackerGenerator();
    }

    @Test
    public void testMazeGeneration() {
        int height = 11;
        int width = 11;
        Maze maze = generator.generate(height, width);

        assertEquals(height, maze.getHeight());
        assertEquals(width, maze.getWidth());

        assertEquals(Cell.Type.PASSAGE, maze.getGrid()[1][1].type());

        for (int row = 0; row < height; row++) {
            assertEquals(Cell.Type.WALL, maze.getGrid()[row][0].type());
            assertEquals(Cell.Type.WALL, maze.getGrid()[row][width - 1].type());
        }

        for (int col = 0; col < width; col++) {
            assertEquals(Cell.Type.WALL, maze.getGrid()[0][col].type());
            assertEquals(Cell.Type.WALL, maze.getGrid()[height - 1][col].type());
        }
    }

    @Test
    public void testPassagesAndWallsInMaze() {
        int height = 11;
        int width = 11;
        Maze maze = generator.generate(height, width);

        int passageCount = 0;
        int wallCount = 0;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (maze.getGrid()[row][col].type() == Cell.Type.PASSAGE) {
                    passageCount++;
                } else {
                    wallCount++;
                }
            }
        }

        assertTrue(passageCount > 0);
        assertTrue(wallCount > 0);
    }

    @Test
    public void testMazeOddDimensions() {
        int height = 12;
        int width = 12;
        Maze maze = generator.generate(height, width);

        assertEquals(13, maze.getHeight());
        assertEquals(13, maze.getWidth());
    }
}
