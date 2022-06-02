package dit.example;

import dit.example.workWithDB.ManipulationWithDB;
import dit.example.workWithFiles.FileManipulation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class View {
    private final String separator = File.separator;
    private final String LOCAL_DB_URL = "jdbc:h2:." + separator + "db" + separator + "dbTest";
    private final String MEM_DB_URL = "jdbc:h2:mem:";
    private final String WORK_WITH_FILES = "1";
    private final String WORK_WITH_DB = "2";
    private final String END_WORK = "0";
    private final String ADD_STUDENT = "11";
    private final String DEL_STUDENT = "22";
    private final String SHOW_STUDENT = "33";
    private final String FILL_TABLE = "44";
    private final String BACK = "00";

    public void showMainMenu() {
        System.out.println("|====================================================================================================================================|");
        System.out.println("|===========================================================-=Главное меню=-=========================================================|");
        System.out.println("| Выберите действие:                                                                                                                 |");
        System.out.println("| 1: (Работа с текстовыми файлами) - Найти все текстовые файлы, отсортировать их по имени и склеить содержимое в один текстовый файл.|");
        System.out.println("| 2: (Работа с БД) - Добавить студента, удалить студента по уникальному номеру, вывести список студентов.                            |");
        System.out.println("| 0: Выход.                                                                                                                          |");
        System.out.println("|====================================================================================================================================|");
        System.out.println();
        System.out.print("Ввод: ");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        switch (choice) {
            case (WORK_WITH_FILES):
                FileManipulation fileManipulation = new FileManipulation();
                try {
                    Files.walkFileTree(Paths.get("testFindTXTDir"), fileManipulation);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                fileManipulation.sortFilesAtoZ();
                try {
                    fileManipulation.createAndSaveResultFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Data is saved to file: RESULT_FILE.txt");
                System.out.println();
                showMainMenu();
                break;

            case (WORK_WITH_DB):
                showMenuLevel2();
                break;

            case (END_WORK):
                System.out.println("App is finished");
                System.exit(0);
                break;

            default:
                System.out.println("Некорректный выбор");
                showMainMenu();
                break;
        }
        scanner.close();
    }

    private void showMenuLevel2() {
        System.out.println("|=============================================================|");
        System.out.println("|===================-=Меню (Работа с БД)=-====================|");
        System.out.println("| Выберите действие:                                          |");
        System.out.println("| 11: (Работа с БД) - Добавить студента.                      |");
        System.out.println("| 22: (Работа с БД) - Удалить студента по уникальному номеру. |");
        System.out.println("| 33: (Работа с БД) - Вывести список студентов.               |");
        System.out.println("| 44: (Работа с БД) - Заполнить таблицу.                      |");
        System.out.println("| 00: Назад.                                                  |");
        System.out.println("|=============================================================|");
        System.out.println();
        System.out.print("Ввод: ");

        ManipulationWithDB manipulationWithDB = new ManipulationWithDB();
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        switch (choice) {
            case (ADD_STUDENT):
                try (Connection connectionDB = DriverManager.getConnection(LOCAL_DB_URL)) {
                    if (!manipulationWithDB.tableExists(connectionDB)) {
                        manipulationWithDB.createTable(connectionDB);
                    }
                    manipulationWithDB.addStudent(connectionDB);
                } catch (SQLException ex) {
                    System.out.println("Database connection failure: " + ex.getMessage());
                }
                System.out.println();
                showMenuLevel2();
                break;

            case (DEL_STUDENT):
                try (Connection connectionDB = DriverManager.getConnection(LOCAL_DB_URL)) {
                    if (!manipulationWithDB.tableExists(connectionDB)) {
                        manipulationWithDB.createTable(connectionDB);
                    }
                    manipulationWithDB.deleteStudent(connectionDB);
                } catch (SQLException ex) {
                    System.out.println("Database connection failure: " + ex.getMessage());
                }
                System.out.println();
                showMenuLevel2();
                break;

            case (SHOW_STUDENT):
                try (Connection connectionDB = DriverManager.getConnection(LOCAL_DB_URL)) {
                    if (!manipulationWithDB.tableExists(connectionDB)) {
                        manipulationWithDB.createTable(connectionDB);
                    }
                    manipulationWithDB.showDataDB(connectionDB);
                } catch (SQLException ex) {
                    System.out.println("Database connection failure: " + ex.getMessage());
                }
                System.out.println();
                showMenuLevel2();
                break;

            case (FILL_TABLE):
                try (Connection connectionDB = DriverManager.getConnection(LOCAL_DB_URL)) {
                    if (!manipulationWithDB.tableExists(connectionDB)) {
                        manipulationWithDB.createTable(connectionDB);
                    }
                    manipulationWithDB.fillDataDB(connectionDB);
                } catch (SQLException ex) {
                    System.out.println("Database connection failure: " + ex.getMessage());
                }
                System.out.println();
                showMenuLevel2();
                break;

            case (BACK):
                System.out.println();
                showMainMenu();
                break;

            default:
                System.out.println("Некорректный выбор");
                showMenuLevel2();
                break;
        }
        scanner.close();
    }
}
