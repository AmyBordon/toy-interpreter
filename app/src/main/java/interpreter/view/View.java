package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;
import java.nio.file.Path;
import java.util.Scanner;

public class View {
    private final Path dir = Path.of(
            System.getProperty("java.io.tmpdir"),
            "toy-interpreter"
    );

    private final Scanner in = new Scanner(System.in);
    private IStmt selected = null;   // nothing selected at the start

    private void menu() {
        System.out.println("\n1. Select a program to run");
        System.out.println("2. Run the selected program");
        System.out.println("0. Exit");
        System.out.print("> ");
    }

    public void start() {
        while (true) {
            menu();
            if (!in.hasNextLine()) return;
            var input = in.nextLine().trim();
            if (input.equals("1"))
                select();
            else if (input.equals("2"))
                run();
            else if (input.equals("0"))
                break;
            else
                System.out.println("The input is not valid!");
        }
    }

    private void examplesMenu() {
        System.out.println("------------");
        System.out.println("1. Example 1");
        System.out.println("2. Example 2");
        System.out.println("3. Example 3");
        System.out.println("4. Example 4");
        System.out.println("------------");
        System.out.print("> ");
    }

    private void select() {
        examplesMenu();
        if (!in.hasNextLine()) return;
        var input = in.nextLine().trim();
        if (input.equals("1"))
            selected = Examples.EX1;
        else if (input.equals("2"))
            selected = Examples.EX2;
        else if (input.equals("3"))
            selected = Examples.EX3;
        else if (input.equals("4"))
            selected = Examples.EX4;
        else {
            System.out.println("The input is not valid!");
            return;
        }
        System.out.println("Selected: " + selected);
    }

    private void run() {
        if (selected == null) {
            System.out.println("Select a program first.");
            return;
        }
        PrgState state = PrgState.of(selected);
        try {
            new Controller(new Repository(state, dir.resolve("log.txt").toString()), true).allStep();
        } catch (MyException e) {
            System.out.println("Error: " + e.getMessage());   // menu must not crash
        }
    }
}