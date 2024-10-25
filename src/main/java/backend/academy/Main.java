package backend.academy;

import backend.academy.renderers.ConsoleRenderer;
import java.util.Scanner;
import lombok.experimental.UtilityClass;

/**
 * Класс Main — точка входа в приложение.
 */
@UtilityClass
public class Main {
    public static void main(String[] args) {
        Game game = new Game(new Scanner(System.in), new ConsoleRenderer(), System.out);
        game.start();
    }
}
