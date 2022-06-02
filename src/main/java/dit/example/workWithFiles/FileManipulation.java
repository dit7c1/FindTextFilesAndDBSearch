package dit.example.workWithFiles;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class FileManipulation extends SimpleFileVisitor<Path> {

    private String separator = File.separator;
    private final String PATH_RESULT_FILE = "testFindTXTDir" + separator + "RESULT_FILE.txt";
    private Map<String, String> fullPathToFileTXTMap = new HashMap<>();
    private List<String> sortedFullPathToFileTXTList = new ArrayList<>();

    @Override

    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.toString().endsWith(".txt")) {
            fullPathToFileTXTMap.put(file.getParent().toString() + separator + file.getFileName(), file.getFileName().toString());
        }
        return FileVisitResult.CONTINUE;
    }

    public void sortFilesAtoZ() {
        getFullPathToFileTXTMap().entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> sortedFullPathToFileTXTList.add(entry.getKey().toLowerCase().trim()));

    }

    public void createAndSaveResultFile() throws IOException {
        File pathResultFile = new File(PATH_RESULT_FILE);
        PrintWriter writeToResultFile = new PrintWriter(pathResultFile);

        for (String loopFromFile : getSortedFullPathToFileTXTList()) {
            File pathTXTFile = new File(loopFromFile);
            Scanner readFile = new Scanner(pathTXTFile);
            while (readFile.hasNextLine()) {
                writeToResultFile.println(readFile.nextLine());
            }
            readFile.close();
        }
        writeToResultFile.close();
    }


    public Map<String, String> getFullPathToFileTXTMap() {
        return fullPathToFileTXTMap;
    }

    public List<String> getSortedFullPathToFileTXTList() {
        return sortedFullPathToFileTXTList;
    }

}
