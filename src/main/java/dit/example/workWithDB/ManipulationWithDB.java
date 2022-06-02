package dit.example.workWithDB;

import java.sql.*;
import java.util.Scanner;

public class ManipulationWithDB {

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE STUDENTS (ID INT AUTO_INCREMENT PRIMARY KEY ,FIRST_NAME VARCHAR(15), LAST_NAME VARCHAR(15), PATRONYMIC VARCHAR(15), BIRTH_DAY DATE, GROUP_STUDENT VARCHAR(15))";

    private static final String DATA_QUERY_1 = "INSERT INTO STUDENTS VALUES(default,'Дмитрий','Тимофеев','Игоревич','1981-01-21','ДИТ')";
    private static final String DATA_QUERY_2 = "INSERT INTO STUDENTS VALUES(default,'Надежда','Аброськина','Петровна','1982-02-22','НПА')";
    private static final String DATA_QUERY_3 = "INSERT INTO STUDENTS VALUES(default,'Иван','Андропов','Яковлев','1983-03-23','ДИТ')";
    private static final String DATA_QUERY_4 = "INSERT INTO STUDENTS VALUES(default,'Яна','Стрёмная','Сергеевна','1984-04-24','НПА')";
    private static final String DATA_QUERY_5 = "INSERT INTO STUDENTS VALUES(default,'Константин','Аброськин','Олегович','1985-05-25','ДИТ')";
    private static final String DATA_QUERY_6 = "INSERT INTO STUDENTS VALUES(default,'Юлия','Мясникова','Игоревна','1986-06-26','ХЗ')";
    private static final String DATA_QUERY_7 = "INSERT INTO STUDENTS VALUES(default,'Сергей','Косурин','','1987-07-27','ХЗ')";
    private static final String DATA_QUERY_8 = "INSERT INTO STUDENTS VALUES(default,'Алла','Живогляд','Сергеевна','1988-08-28','НПА')";
    private static final String DATA_QUERY_9 = "INSERT INTO STUDENTS VALUES(default,'Игорь','Тимофеев','Викторович','1989-09-29','ДИТ')";

    public void showDataDB(Connection connectionDB) {
        PreparedStatement query = null;
        ResultSet resultSet = null;

        try {
            query = connectionDB.prepareStatement("SELECT * FROM STUDENTS");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            resultSet = query.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println(String.format("|%-3s|%-15s|%-15s|%-15s|%-15s|%-15s|",
                "ID",
                "FIRST_NAME",
                "LAST_NAME",
                "PATRONYMIC",
                "BIRTH_DAY",
                "GROUP_STUDENT"
        ));
        System.out.println(String.format("|%-3s|%-15s|%-15s|%-15s|%-15s|%-15s|",
                "---",
                "---------------",
                "---------------",
                "---------------",
                "---------------",
                "---------------"
        ));

        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                System.out.println(String.format("|%-3s|%-15s|%-15s|%-15s|%-15s|%-15s|",
                        resultSet.getString("ID"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("LAST_NAME"),
                        resultSet.getString("PATRONYMIC"),
                        resultSet.getString("BIRTH_DAY"),
                        resultSet.getString("GROUP_STUDENT")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addStudent(Connection connectionDB) {
        Statement dataFillQuery = null;
        Scanner scanner = new Scanner(System.in);

        System.out.print("FIRST_NAME: ");
        String firstName = scanner.nextLine();

        System.out.print("LAST_NAME: ");
        String lastName = scanner.nextLine();

        System.out.print("PATRONYMIC: ");
        String patronymic = scanner.nextLine();

        System.out.print("BIRTH_DAY (format yyyy-mm-dd): ");
        Date birthDay = Date.valueOf(scanner.nextLine());

        System.out.print("GROUP_STUDENT: ");
        String groupStudent = scanner.nextLine();

        String addStudentQuery = "INSERT INTO STUDENTS VALUES(default,'" + firstName + "','" + lastName + "','" + patronymic + "','" + birthDay + "','" + groupStudent + "')";

        try {
            dataFillQuery = connectionDB.createStatement();
            dataFillQuery.execute(addStudentQuery);
            System.out.println("Success: Student is added!");
        } catch (SQLException e) {
            System.out.println("Err! Student is not added: " + e.getMessage());
        }
    }

    public void deleteStudent(Connection connectionDB) {
        PreparedStatement query = null;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter ID to delete: ");
        int id = scanner.nextInt();

        try {
            query = connectionDB.prepareStatement("DELETE FROM STUDENTS WHERE ID = " + id);
            query.execute();
            System.out.println("Success: Student with id " + id + " is deleted!");
        } catch (SQLException e) {
            System.out.println("Err! Student is not deleted: " + e.getMessage());
        }
    }

    public boolean tableExists(Connection connectionDB) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connectionDB.prepareStatement("SELECT count(*) " + "FROM information_schema.tables " + "WHERE table_name = ?" + "LIMIT 1;");
            preparedStatement.setString(1, "STUDENTS");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            return resultSet.getInt(1) != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable(Connection connectionDB) {
        Statement dataFillQuery = null;

        try {
            dataFillQuery = connectionDB.createStatement();
        } catch (SQLException e) {
            System.out.println("Database connection failure: " + e.getMessage());
        }

        try {
            if (dataFillQuery != null) {
                dataFillQuery.execute(CREATE_TABLE_QUERY);
            }
        } catch (SQLException e) {
            System.out.println("Table is not created: " + e.getMessage());
        }
    }

    public void fillDataDB(Connection connectionDB) {
        Statement dataFillQuery = null;

        try {
            dataFillQuery = connectionDB.createStatement();
        } catch (SQLException e) {
            System.out.println("Database connection failure: " + e.getMessage());
        }

        try {
            if (dataFillQuery != null) {
                dataFillQuery.execute(DATA_QUERY_1);
                dataFillQuery.execute(DATA_QUERY_2);
                dataFillQuery.execute(DATA_QUERY_3);
                dataFillQuery.execute(DATA_QUERY_4);
                dataFillQuery.execute(DATA_QUERY_5);
                dataFillQuery.execute(DATA_QUERY_6);
                dataFillQuery.execute(DATA_QUERY_7);
                dataFillQuery.execute(DATA_QUERY_8);
                dataFillQuery.execute(DATA_QUERY_9);
            }
        } catch (SQLException e) {
            System.out.println("Table is not fill: " + e.getMessage());
        }
    }
}
