package tool.console;

import java.util.Scanner;

public class Console {

    public void enterSettings() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n Enter input file path:");

        String inputFilePath  = scanner.nextLine();
        Settings.inputFilePath = inputFilePath;

        System.out.println("\n Enter output dir path:");

        String outputDirPath  = scanner.nextLine();
        Settings.inputFilePath = outputDirPath;
    }
}
