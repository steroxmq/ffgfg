package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String dbDirectory = "database_files/";
        String dbName = "users.db";
        String fullDbPath = dbDirectory + dbName;

        // Создание папки для базы данных
        new java.io.File(dbDirectory).mkdir();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + fullDbPath)) {
            if (connection != null) {
                System.out.println("Connected to the database!");

                // SQL для создания таблицы user_info
                String createTableSQL = "CREATE TABLE IF NOT EXISTS user_info (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "surname VARCHAR(100) NOT NULL, " +
                        "date_of_birth VARCHAR(100) NOT NULL, " +
                        "email VARCHAR(100) NOT NULL, " +
                        "registration_date VARCHAR(100) NOT NULL, " +
                        "role_id INTEGER NOT NULL, " +
                        "books_uploaded INTEGER DEFAULT 0" + // Опциональное поле
                        ");";

                // Создание таблицы
                try (Statement statement = connection.createStatement()) {
                    statement.execute(createTableSQL);
                    System.out.println("Table 'user_info' has been created.");
                }

                // Заполнение таблицы семью пользователями
                String insertSQL = "INSERT INTO user_info (name, surname, date_of_birth, email, registration_date, role_id, books_uploaded) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?);";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                    // 7 заранее заданных пользователей
                    String[][] users = {
                            {"Alice", "Johnson", "1985-06-15", "alice.johnson@example.com", "2023-01-01", "0", "5"},
                            {"Bob", "Smith", "1990-09-21", "bob.smith@example.com", "2023-02-01", "1", "12"},
                            {"Charlie", "Brown", "1978-03-11", "charlie.brown@example.com", "2023-03-01", "0", "2"},
                            {"Diana", "Prince", "1989-07-10", "diana.prince@example.com", "2023-04-01", "1", "8"},
                            {"Edward", "Carter", "1995-12-20", "edward.carter@example.com", "2023-05-01", "0", "1"},
                            {"Fiona", "Green", "1992-11-25", "fiona.green@example.com", "2023-06-01", "1", "4"},
                            {"George", "Hill", "1983-01-05", "george.hill@example.com", "2023-07-01", "0", "3"}
                    };

                    for (String[] user : users) {
                        preparedStatement.setString(1, user[0]); // name
                        preparedStatement.setString(2, user[1]); // surname
                        preparedStatement.setString(3, user[2]); // date_of_birth
                        preparedStatement.setString(4, user[3]); // email
                        preparedStatement.setString(5, user[4]); // registration_date
                        preparedStatement.setInt(6, Integer.parseInt(user[5])); // role_id
                        preparedStatement.setInt(7, Integer.parseInt(user[6])); // books_uploaded
                        preparedStatement.executeUpdate();
                    }
                    System.out.println("7 sample users have been added to the database.");
                }

                // Ввод данных для 8-го пользователя через консоль
                Scanner scanner = new Scanner(System.in);

                System.out.println("Enter details for the 8th user:");

                System.out.print("Name: ");
                String name = scanner.nextLine();

                System.out.print("Surname: ");
                String surname = scanner.nextLine();

                System.out.print("Date of Birth (YYYY-MM-DD): ");
                String dateOfBirth = scanner.nextLine();

                System.out.print("Email: ");
                String email = scanner.nextLine();

                System.out.print("Registration Date (YYYY-MM-DD): ");
                String registrationDate = scanner.nextLine();

                System.out.print("Role ID (0 for regular user, 1 for admin): ");
                int roleId = scanner.nextInt();

                System.out.print("Number of Books Uploaded: ");
                int booksUploaded = scanner.nextInt();

                // Добавление 8-го пользователя в базу данных
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, surname);
                    preparedStatement.setString(3, dateOfBirth);
                    preparedStatement.setString(4, email);
                    preparedStatement.setString(5, registrationDate);
                    preparedStatement.setInt(6, roleId);
                    preparedStatement.setInt(7, booksUploaded);
                    preparedStatement.executeUpdate();
                }
                System.out.println("8th user has been added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
