package common;
import common.basic.MusicGenre;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class {@code ScanValidation} contains overridden methods
 * of class {@code Scanner} and not only to read data from {@code System.in}.
 * All these methods ask user to enter valid data while
 * detecting incorrect input.
 */
public class ScanValidation {

    /**
     * Asks user to enter {@code int} value while
     * catching {@link InputMismatchException} exception.
     *
     * @return entered {@code int} value.
     */
    public static int nextInt() {
        Scanner varScan = new Scanner(System.in);
        int obj;
        try {
            obj = varScan.nextInt();
        } catch (InputMismatchException ex) {
            System.out.println("Введенная строка не является целым числом, введите корректное значение.");
            return nextInt();
        }
        return obj;
    }

    /**
     * Asks user to enter {@code double} value while
     * catching {@link InputMismatchException} exception.
     *
     * @return entered {@code double} value.
     */
    public static double nextDouble() {
        Scanner varScan = new Scanner(System.in);
        double obj;
        try {
            obj = varScan.nextDouble();
        } catch (InputMismatchException ex) {
            System.out.println("Введенная строка не является дробным десятичным числом, введите корректное значение.");
            return nextDouble();
        }
        return obj;
    }

    /**
     * Asks user to enter {@code long} value while
     * catching {@link InputMismatchException} exception.
     *
     * @return entered {@code long} value.
     */
    public static long nextLong() {
        Scanner varScan = new Scanner(System.in);
        long obj;
        try {
            obj = varScan.nextLong();
        } catch (InputMismatchException ex) {
            System.out.println("Введенная строка не является целым числом, введите корректное значение.");
            return nextLong();
        }
        return obj;
    }

    /**
     * Asks user to enter {@link MusicGenre} value while
     * catching {@link InputMismatchException} exception.
     *
     * @return entered {@link MusicGenre} value.
     */
    public static MusicGenre nextGenre() {
        Scanner varScan = new Scanner(System.in);
        MusicGenre genre;
        try {
            String g = varScan.next();
            varScan.nextLine();
            genre = MusicGenre.valueOf(g);
        } catch (IllegalArgumentException ex) {
            System.out.println("На музыкальной площадке нет такого жанра, введите один из предложенных ранее.");
            return nextGenre();
        }
        return genre;
    }

    /**
     * Asks user to enter not empty {@code String} value
     * while detecting empty inputted {@code String}.
     *
     * @return entered not empty {@code String} value.
     */
    public static String nextNonEmptyLine() {
        Scanner varScan = new Scanner(System.in);
        String srt = varScan.nextLine();
        if (srt.equals("")) {
            System.out.println("Строка не может быть пустой. Введите её корректно.");
            return nextNonEmptyLine();
        }
        return srt;
    }

}
