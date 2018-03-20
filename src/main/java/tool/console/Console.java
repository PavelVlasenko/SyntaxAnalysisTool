package tool.console;

import java.util.Scanner;

public class Console {

    public void enterSettings() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n Enter input file path:");

        String inputFilePath  = scanner.next();
        Settings.inputFilePath = inputFilePath;

        System.out.println("\n Select type(enter 0 or 1):");
        System.out.println("[0] Python");
        System.out.println("[1] C");

        int action = scanner.nextInt();
        switch(action) {
            case 0: Settings.type = Type.PYTHON;
                break;
            case 1: Settings.type = Type.C;
                break;
            default: System.out.println("Incorrect action, enter 0 or 1"); System.exit(0);
        }

        System.out.println("\n Enter output dir path:");

        String outputDirPath  = scanner.next();
        Settings.outputDir = outputDirPath;
    }

    public void enterUmlSettings() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n Enter input dir path:");

        String inputDirPath  = scanner.next();
        Settings.inputDir = inputDirPath;

        System.out.println("\n Select type(enter 0 or 1):");
        System.out.println("[0] Python");
        System.out.println("[1] C");

        int action = scanner.nextInt();
        switch(action) {
            case 0: Settings.type = Type.PYTHON;
                break;
            case 1: Settings.type = Type.C;
                break;
            default: System.out.println("Incorrect action, enter 0 or 1"); System.exit(0);
        }

        System.out.println("\n Enter output dir path:");

        String outputDirPath  = scanner.next();
        Settings.outputDir = outputDirPath;
    }
}
