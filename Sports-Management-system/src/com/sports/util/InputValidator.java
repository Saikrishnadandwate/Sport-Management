package com.sports.util;

import java.util.Scanner;

/**
 * Utility class to safely read and validate console inputs.
 * Prevents Scanner traps (like newline skipping) and InputMismatchException.
 */
public class InputValidator {

    public static String readString(Scanner scanner, String prompt, boolean isRequired) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return "";
            }
            String input = scanner.nextLine().trim();
            if (isRequired && input.isEmpty()) {
                System.out.println(">> Error: This field cannot be empty. Please enter a valid value.");
                continue;
            }
            return input;
        }
    }

    public static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return min;
            }
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.printf(">> Error: Value must be between %d and %d. Please try again.%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println(">> Error: Invalid integer. Please enter digits only.");
            }
        }
    }

    public static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            if (!scanner.hasNextLine()) {
                return false;
            }
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            }
            if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println(">> Error: Please enter 'y' for Yes or 'n' for No.");
        }
    }
}
