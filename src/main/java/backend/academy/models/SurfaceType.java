package backend.academy.models;

public enum SurfaceType {
    NORMAL(1),         // Обычная поверхность, стандартная стоимость перемещения
    SWAMP(5),          // Болото: высокая стоимость перемещения (ухудшающая поверхность)
    SAND(3),           // Песок: средняя стоимость перемещения (ухудшающая поверхность)
    COIN(-1),          // Монетка: улучшает перемещение, снижает общую стоимость
    ROAD(0.5);         // Дорожка: ускоряет перемещение, снижает стоимость

    private final double cost;

    SurfaceType(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }
}
