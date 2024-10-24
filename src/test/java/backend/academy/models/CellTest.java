package backend.academy.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CellTest {

    @Test
    public void testCellInitialization() {
        Cell cell = new Cell(0, 0, Cell.Type.PASSAGE, SurfaceType.NORMAL);

        assertEquals(0, cell.row());
        assertEquals(0, cell.col());
        assertEquals(Cell.Type.PASSAGE, cell.type());
        assertEquals(SurfaceType.NORMAL, cell.getSurface());
    }

    @Test
    public void testWallCellInitialization() {
        Cell wallCell = new Cell(1, 1, Cell.Type.WALL, SurfaceType.SAND);

        assertEquals(1, wallCell.row());
        assertEquals(1, wallCell.col());
        assertEquals(Cell.Type.WALL, wallCell.type());
        assertEquals(SurfaceType.SAND, wallCell.getSurface());
    }
}

