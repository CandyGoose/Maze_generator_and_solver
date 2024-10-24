package backend.academy.generators;

import backend.academy.generators.PrimGenerator;
import backend.academy.models.Cell;
import backend.academy.models.Maze;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrimGeneratorTest {

    @Test
    public void testGenerateMaze() {
        PrimGenerator generator = new PrimGenerator();
        Maze maze = generator.generate(11, 11);

        assertEquals(11, maze.getHeight());
        assertEquals(11, maze.getWidth());

        boolean hasPassage = false;
        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                if (maze.getGrid()[row][col].type() == Cell.Type.PASSAGE) {
                    hasPassage = true;
                    break;
                }
            }
        }
        assertTrue(hasPassage, "Лабиринт должен содержать хотя бы один проход");
    }
}
