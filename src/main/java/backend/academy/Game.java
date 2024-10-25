package backend.academy;

import backend.academy.generators.PrimGenerator;
import backend.academy.generators.RecursiveBacktrackerGenerator;
import backend.academy.interfaces.Generator;
import backend.academy.interfaces.Solver;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.renderers.ConsoleRenderer;
import backend.academy.solvers.AStarSolver;
import backend.academy.solvers.BFSSolver;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Класс Game управляет процессом генерации и решения лабиринтов.
 * Содержит логику генерации, выбора начальных и конечных точек, а также решения лабиринта.
 */
public class Game {

    private final Scanner scanner;
    private final ConsoleRenderer renderer;
    private final PrintStream out;

    private static final String INVALID_INPUT_MSG = "Неверный ввод. Введите число: ";
    private static final String CHOICE_PROMPT = "Выберите вариант от %d до %d: ";

    public Game(Scanner scanner, ConsoleRenderer renderer, PrintStream out) {
        this.scanner = scanner;
        this.renderer = renderer;
        this.out = out;
    }

    /**
     * Запускает игровой процесс: генерация лабиринта, выбор начальных и конечных точек,
     * выбор алгоритма решения и отображение пути.
     */
    public void start() {
        out.println("Генератор и Решатель Лабиринтов");

        out.print("Введите высоту лабиринта (нечетное число, например 21): ");
        int height = readPositiveOddInt();
        out.print("Введите ширину лабиринта (нечетное число, например 21): ");
        int width = readPositiveOddInt();

        // Выбор алгоритма генерации лабиринта
        out.println("1 - Алгоритм Прима");
        out.println("2 - Алгоритм Recursive Backtracker");
        int genChoice = readChoice(1, 2);
        Generator generator = genChoice == 1 ? new PrimGenerator() : new RecursiveBacktrackerGenerator();

        Maze maze = generator.generate(height, width);
        out.println("Сгенерированный лабиринт:");
        out.println(renderer.renderWithLabels(maze));

        // Ввод начальной и конечной точек
        out.println("Введите координаты точки А (начало):");
        Coordinate start = getCoordinate(maze);
        out.println("Введите координаты точки Б (конец):");
        Coordinate end = getCoordinate(maze);

        out.println("Лабиринт с точками А и Б:");
        out.println(renderer.renderWithPoints(maze, start, end));

        // Выбор алгоритма поиска пути
        out.println("Выберите алгоритм поиска пути:");
        out.println("1 - BFS (поиск в ширину)");
        out.println("2 - A* (A-star)");
        int solChoice = readChoice(1, 2);

        Solver solver = solChoice == 1 ? new BFSSolver() : new AStarSolver();
        List<Coordinate> path = solver.solve(maze, start, end);

        if (path.isEmpty()) {
            out.println("Путь не найден.");
        } else {
            out.println("Найденный путь:");
            out.println(renderer.renderWithPathAndPoints(maze, path, start, end));
        }
    }

    // Метод для чтения положительного нечетного целого числа
    private int readPositiveOddInt() {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(scanner.next());
                if (value > 0 && value % 2 != 0) {
                    break;
                } else {
                    out.print("Введите положительное нечетное число: ");
                }
            } catch (NumberFormatException e) {
                out.print(INVALID_INPUT_MSG);
            }
        }
        return value;
    }

    // Метод для чтения числа в диапазоне
    private int readChoice(int min, int max) {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.next());
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    out.printf(CHOICE_PROMPT, min, max);
                }
            } catch (NumberFormatException e) {
                out.print(INVALID_INPUT_MSG);
            }
        }
        return choice;
    }

    // Метод для ввода координат точки, гарантирующий, что это проход
    private Coordinate getCoordinate(Maze maze) {
        int row;
        int col;
        while (true) {
            out.printf("Введите строку (1 - %d): ", maze.getHeight() - 2);
            row = readCoordinate(1, maze.getHeight() - 2);
            out.printf("Введите столбец (1 - %d): ", maze.getWidth() - 2);
            col = readCoordinate(1, maze.getWidth() - 2);
            if (maze.getGrid()[row][col].type() == Cell.Type.PASSAGE) {
                break;
            } else {
                out.println("Неверные координаты или выбрана стена. Попробуйте снова.");
            }
        }
        return new Coordinate(row, col);
    }

    // Метод для чтения координаты строки или столбца в заданном диапазоне
    private int readCoordinate(int min, int max) {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(scanner.next());
                if (value >= min && value <= max) {
                    break;
                } else {
                    out.printf(CHOICE_PROMPT, min, max);
                }
            } catch (NumberFormatException e) {
                out.print(INVALID_INPUT_MSG);
            }
        }
        return value;
    }
}
