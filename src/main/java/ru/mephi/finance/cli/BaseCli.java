package ru.mephi.finance.cli;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public abstract class BaseCli {
    protected Scanner scanner;

    @Autowired
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    protected int readIntInput() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Требуется ввести целое число.");
        }
    }

    protected String readNonEmptyStringInput(String name) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException(name + " не может быть пустым.");
        }
        return input;
    }

    protected BigDecimal readBigDecimalInput() {
        try {
            String inputStr = scanner.nextLine().trim().replace(",", ".");
            BigDecimal input = new BigDecimal(inputStr).setScale(2, RoundingMode.DOWN);
            if (input.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Значение может быть только положительным.");
            }
            return input;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Значение должно быть числом (целым или десятичной дробью).");
        }
    }

    protected boolean readPositiveReply() {
        String input = scanner.nextLine().trim();
        return input.equalsIgnoreCase("да");
    }

    protected abstract void printMenu();

    protected abstract void printHelp();
}
