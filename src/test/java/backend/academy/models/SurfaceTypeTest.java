package backend.academy.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SurfaceTypeTest {

    @Test
    public void testSurfaceTypeCosts() {
        assertEquals(1.0, SurfaceType.NORMAL.getCost(), "Плоская поверхность должна иметь стоимость 1.0");
        assertEquals(3.0, SurfaceType.SAND.getCost(), "Песок должен иметь утроенную стоимость.");
        assertEquals(0.5, SurfaceType.ROAD.getCost(), "Дорога должна снижать стоимость.");
    }
}
