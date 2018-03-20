package tool.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * File util class.
 */
public class FileManager {
    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("You must provide a valid dir path.");
            return;
        }

        createFileList(args[1], args[0]);
    }

    public static List<String> getAllFilePaths(String dir) {
        List<String> filePaths = new ArrayList<>();
        File [] dirFile = {new File(dir)};
        getAllFilePaths(dirFile, filePaths);
        return filePaths;
    }

    private  static void getAllFilePaths(File[] files, List<String> filePaths) {
        for(File file : files) {
            if (!file.isDirectory()) {
                filePaths.add(file.getAbsolutePath());
            } else {
                getAllFilePaths(file.listFiles(), filePaths);
            }
        }
    }

    public static String readLine(String filename, int lineNumber) {
       Path path = Paths.get(filename);
        try {
            List<String> lines = Files.readAllLines(path);
            return lines.get(lineNumber-1);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void createFileList(String outputfile, String dirname) {
        File dir = new File(dirname);
        if(dir.isDirectory()) {
            saveFile(outputfile, recursiveJavaFinder(dir));
        } else {
            System.out.println("You must provide a valid dir path.");
        }
    }

    public static String recursiveJavaFinder(File dir) {
        String res = "";
        for(File child : dir.listFiles()) {
            if(child.isDirectory()) {
                res += recursiveJavaFinder(child);
            } else {
                String extension = child.getName().substring(child.getName().lastIndexOf('.') + 1);
                if(extension.equals("java")) {
                    res += child.getAbsolutePath().replace("\\", "/") + "\n";
                }
            }
        }
        return res;
    }

    public static boolean saveFile(String filename, String content) {
        try {
            File file = new File(filename);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }
}
