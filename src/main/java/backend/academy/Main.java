package backend.academy;

import backend.academy.generators.PrimGenerator;
import backend.academy.generators.RecursiveBacktrackerGenerator;
import backend.academy.interfaces.Generator;
import backend.academy.interfaces.Renderer;
import backend.academy.interfaces.Solver;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.renderers.ConsoleRenderer;
import backend.academy.solvers.AStarSolver;
import backend.academy.solvers.BFSSolver;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleRenderer renderer = new ConsoleRenderer();

        System.out.println("Генератор и Решатель Лабиринтов");

        System.out.print("Введите высоту лабиринта (нечетное число, например 21): ");
        int height = readPositiveOddInt(scanner);
        System.out.print("Введите ширину лабиринта (нечетное число, например 21): ");
        int width = readPositiveOddInt(scanner);

        System.out.println("1 - Алгоритм Прима");
        System.out.println("2 - Алгоритм Recursive Backtracker");
        int genChoice = readChoice(scanner, 1, 2);
        Generator generator = switch (genChoice) {
            case 1 -> new RecursiveBacktrackerGenerator();
            case 2 -> new PrimGenerator();
            default -> {
                System.out.println("Неверный выбор. Используется Рекурсивный Обход.");
                yield new RecursiveBacktrackerGenerator();
            }
        };

        Maze maze = generator.generate(height, width);

        System.out.println("Сгенерированный лабиринт:");
        System.out.println(renderer.renderWithLabels(maze));

        System.out.println("Введите координаты точки А (начало):");
        Coordinate start = getCoordinate(scanner, maze);
        System.out.println("Введите координаты точки Б (конец):");
        Coordinate end = getCoordinate(scanner, maze);

        System.out.println("Лабиринт с точками А и Б:");
        System.out.println(renderer.renderWithPoints(maze, start, end));

        System.out.println("Выберите алгоритм поиска пути:");
        System.out.println("1 - BFS (поиск в ширину)");
        System.out.println("2 - A* (A-star)");
        int solChoice = readChoice(scanner, 1, 2);

        Solver solver = switch (solChoice) {
            case 1 -> new BFSSolver();
            case 2 -> new AStarSolver();
            default -> {
                System.out.println("Неверный выбор. Используется BFS.");
                yield new BFSSolver();
            }
        };

        List<Coordinate> path = solver.solve(maze, start, end);

        if (path.isEmpty()) {
            System.out.println("Путь не найден.");
        } else {
            System.out.println("Найденный путь:");
            System.out.println(renderer.renderWithPathAndPoints(maze, path, start, end));
        }

        scanner.close();
    }

    private static int readPositiveOddInt(Scanner scanner) {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(scanner.next());
                if (value > 0 && value % 2 != 0) {
                    break;
                } else {
                    System.out.print("Введите положительное нечетное число: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Неверный ввод. Введите число: ");
            }
        }
        return value;
    }

    private static int readChoice(Scanner scanner, int min, int max) {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.next());
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    System.out.print("Выберите вариант от " + min + " до " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Неверный ввод. Введите число: ");
            }
        }
        return choice;
    }

    private static Coordinate getCoordinate(Scanner scanner, Maze maze) {
        int row, col;
        while (true) {
            System.out.print("Введите строку (1 - " + (maze.getHeight() - 2) + "): ");
            row = readCoordinate(scanner, 1, maze.getHeight() - 2);
            System.out.print("Введите столбец (1 - " + (maze.getWidth() - 2) + "): ");
            col = readCoordinate(scanner, 1, maze.getWidth() - 2);
            if (maze.getGrid()[row][col].type() == Cell.Type.PASSAGE) {
                break;
            } else {
                System.out.println("Неверные координаты или выбрана стена. Попробуйте снова.");
            }
        }
        return new Coordinate(row, col);
    }

    private static int readCoordinate(Scanner scanner, int min, int max) {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(scanner.next());
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.print("Введите число от " + min + " до " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Неверный ввод. Введите число: ");
            }
        }
        return value;
    }
}
