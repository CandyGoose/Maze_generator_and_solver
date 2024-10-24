package backend.academy;

import backend.academy.generators.PrimGenerator;
import backend.academy.generators.RecursiveBacktrackerGenerator;
import backend.academy.interfaces.Generator;
import backend.academy.interfaces.Solver;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.renderers.ConsoleRenderer;
import backend.academy.solvers.AStarSolver;
import backend.academy.solvers.BFSSolver;
import lombok.experimental.UtilityClass;
import java.util.List;
import java.util.Scanner;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите ширину лабиринта: ");
        int width = scanner.nextInt();
        System.out.println("Введите высоту лабиринта: ");
        int height = scanner.nextInt();

        System.out.println("Выберите алгоритм генерации лабиринта: ");
        System.out.println("1 - Алгоритм Прима");
        System.out.println("2 - Алгоритм Recursive Backtracker");
        int generatorChoice = scanner.nextInt();
        Generator generator;
        if (generatorChoice == 1) {
            generator = new PrimGenerator();
        } else {
            generator = new RecursiveBacktrackerGenerator();
        }

        Maze maze = generator.generate(height, width);

        System.out.println("Введите координаты начальной точки (row col): ");
        Coordinate start = new Coordinate(scanner.nextInt(), scanner.nextInt());
        System.out.println("Введите координаты конечной точки (row col): ");
        Coordinate end = new Coordinate(scanner.nextInt(), scanner.nextInt());

        System.out.println("Выберите алгоритм поиска пути: ");
        System.out.println("1 - BFS (поиск в ширину)");
        System.out.println("2 - A* (A-star)");
        int solverChoice = scanner.nextInt();
        Solver solver;
        if (solverChoice == 1) {
            solver = new BFSSolver();
        } else {
            solver = new AStarSolver();
        }

        List<Coordinate> path = solver.solve(maze, start, end);

        ConsoleRenderer renderer = new ConsoleRenderer();
        String output = renderer.renderWithPathAndPoints(maze, path, start, end);
        System.out.println(output);
    }
}
