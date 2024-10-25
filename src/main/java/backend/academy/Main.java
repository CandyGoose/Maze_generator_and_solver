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
import lombok.experimental.UtilityClass;

/**
 * Главный класс для запуска консольного приложения по генерации и решению лабиринтов.
 * Позволяет пользователю генерировать лабиринты, выбирать начальные и конечные точки,
 * и находить путь с помощью различных алгоритмов.
 */
@UtilityClass
public class Main {

    private static final String INVALID_INPUT_MSG = "Неверный ввод. Введите число: ";
    private static final String CHOICE_PROMPT = "Выберите вариант от %d до %d: ";

    /**
     * Основной метод для запуска приложения. Включает ввод размеров лабиринта,
     * выбор генератора и решателя, а также отображение лабиринта и найденного пути.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleRenderer renderer = new ConsoleRenderer();
        PrintStream out = System.out;

        out.println("Генератор и Решатель Лабиринтов");

        out.print("Введите высоту лабиринта (нечетное число, например 21): ");
        int height = readPositiveOddInt(scanner, out);
        out.print("Введите ширину лабиринта (нечетное число, например 21): ");
        int width = readPositiveOddInt(scanner, out);

        // Выбор алгоритма генерации лабиринта
        out.println("1 - Алгоритм Прима");
        out.println("2 - Алгоритм Recursive Backtracker");
        int genChoice = readChoice(scanner, out, 1, 2);
        Generator generator = switch (genChoice) {
            case 1 -> new PrimGenerator();
            case 2 -> new RecursiveBacktrackerGenerator();
            default -> {
                out.println("Неверный выбор. Используется Рекурсивный Обход.");
                yield new RecursiveBacktrackerGenerator();
            }
        };

        Maze maze = generator.generate(height, width);

        out.println("Сгенерированный лабиринт:");
        out.println(renderer.renderWithLabels(maze));

        // Ввод координат начальной и конечной точек
        out.println("Введите координаты точки А (начало):");
        Coordinate start = getCoordinate(scanner, maze, out);
        out.println("Введите координаты точки Б (конец):");
        Coordinate end = getCoordinate(scanner, maze, out);

        out.println("Лабиринт с точками А и Б:");
        out.println(renderer.renderWithPoints(maze, start, end));

        // Выбор алгоритма поиска пути
        out.println("Выберите алгоритм поиска пути:");
        out.println("1 - BFS (поиск в ширину)");
        out.println("2 - A* (A-star)");
        int solChoice = readChoice(scanner, out, 1, 2);

        Solver solver = switch (solChoice) {
            case 1 -> new BFSSolver();
            case 2 -> new AStarSolver();
            default -> {
                out.println("Неверный выбор. Используется BFS.");
                yield new BFSSolver();
            }
        };

        List<Coordinate> path = solver.solve(maze, start, end);

        // Отображение найденного пути или сообщения об отсутствии пути
        if (path.isEmpty()) {
            out.println("Путь не найден.");
        } else {
            out.println("Найденный путь:");
            out.println(renderer.renderWithPathAndPoints(maze, path, start, end));
        }

        scanner.close();
    }

    /**
     * Считывает положительное нечетное целое число из консоли.
     *
     * @param scanner Scanner для ввода данных
     * @param out поток для вывода сообщений
     * @return введенное положительное нечетное число
     */
    private static int readPositiveOddInt(Scanner scanner, PrintStream out) {
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

    /**
     * Считывает число из консоли в заданном диапазоне.
     *
     * @param scanner Scanner для ввода данных
     * @param out поток для вывода сообщений
     * @param min минимальное значение (включительно)
     * @param max максимальное значение (включительно)
     * @return выбранное число в диапазоне [min, max]
     */
    private static int readChoice(Scanner scanner, PrintStream out, int min, int max) {
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

    /**
     * Считывает координаты, гарантируя, что выбранная точка является проходом.
     *
     * @param scanner Scanner для ввода данных
     * @param maze лабиринт для проверки координат
     * @param out поток для вывода сообщений
     * @return координаты (строка, столбец) в виде объекта Coordinate
     */
    private static Coordinate getCoordinate(Scanner scanner, Maze maze, PrintStream out) {
        int row;
        int col;
        while (true) {
            out.printf("Введите строку (1 - %d): ", maze.getHeight() - 2);
            row = readCoordinate(scanner, out, 1, maze.getHeight() - 2);
            out.printf("Введите столбец (1 - %d): ", maze.getWidth() - 2);
            col = readCoordinate(scanner, out, 1, maze.getWidth() - 2);
            if (maze.getGrid()[row][col].type() == Cell.Type.PASSAGE) {
                break;
            } else {
                out.println("Неверные координаты или выбрана стена. Попробуйте снова.");
            }
        }
        return new Coordinate(row, col);
    }

    /**
     * Считывает координату строки или столбца в заданном диапазоне.
     *
     * @param scanner Scanner для ввода данных
     * @param out поток для вывода сообщений
     * @param min минимальное значение (включительно)
     * @param max максимальное значение (включительно)
     * @return выбранное значение координаты
     */
    private static int readCoordinate(Scanner scanner, PrintStream out, int min, int max) {
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
