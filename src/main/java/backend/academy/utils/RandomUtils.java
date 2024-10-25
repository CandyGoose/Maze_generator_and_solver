package backend.academy.utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Утилитарный класс для работы с генерацией случайных чисел.
 */
public final class RandomUtils {

    private static final Random RANDOM = new Random();

    private RandomUtils() {

    }

    /**
     * Возвращает случайное целое число от 0 до upperBound - 1.
     *
     * @param upperBound верхняя граница (не включительно)
     * @return случайное целое число в диапазоне [0, upperBound)
     */
    public static int nextInt(int upperBound) {
        return RANDOM.nextInt(upperBound);
    }

    /**
     * Возвращает случайное значение double от 0.0 до 1.0.
     *
     * @return случайное значение double от 0.0 до 1.0
     */
    public static double nextDouble() {
        return RANDOM.nextDouble();
    }

    /**
     * Возвращает true с заданной вероятностью.
     *
     * @param chance вероятность от 0.0 до 1.0
     * @return true, если случайное значение меньше вероятности
     */
    public static boolean chance(double chance) {
        return RANDOM.nextDouble() < chance;
    }

    /**
     * Перемешивает элементы в списке случайным образом.
     *
     * @param list список для перемешивания
     */
    public static <T> void shuffleList(List<T> list) {
        Collections.shuffle(list, RANDOM);
    }
}
