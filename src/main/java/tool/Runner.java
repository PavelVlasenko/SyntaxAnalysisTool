package tool;

import tool.console.Console;
import tool.console.Settings;

/**
 * Abstract runner.
 */
public class Runner {

    public static void enterSettings() {
        if(!Settings.test) {
            Console console = new Console();
            console.enterSettings();
        }
    }

    public static void enterUmlSettings() {
        if(!Settings.test) {
            Console console = new Console();
            console.enterUmlSettings();
        }
    }
}
