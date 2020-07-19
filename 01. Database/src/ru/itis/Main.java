package ru.itis;

import ru.itis.models.Student;
import ru.itis.models.Mentor;
import ru.itis.repositories.StudentsRepository;
import ru.itis.repositories.StudentsRepositoryJdbcImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leontev Roman
 * 11-905
 * 12.07.20
 */

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "111";


    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        StudentsRepository studentsRepository = new StudentsRepositoryJdbcImpl(connection);
        System.out.println(studentsRepository.findById(3L));

        System.out.println("----------------");

        List<Mentor> listMentor1 = new ArrayList<>();
        Student student1 = new Student(null, "Дима", "Капустин", 18, 902, listMentor1);
        listMentor1.add(new Mentor(null, "Владимир", "Путин", student1, 1));
        listMentor1.add(new Mentor(null, "Алишер", "Моргенштерн", student1, 2));
        studentsRepository.save(student1);
        System.out.println(listMentor1);

        System.out.println("----------------");

        List<Mentor> listMentor2 = new ArrayList<>();
        Student student2 = new Student(30L, "Локтар", "Огар", 18, 302, listMentor2);
        listMentor2.add(new Mentor(null, "Алексей", "Навальный", student2, 1));
        listMentor2.add(new Mentor(null, "Слава", "Бустер", student2, 2));
        studentsRepository.update(student2);
        System.out.println(student2);


        System.out.println("----------------");

        System.out.println(studentsRepository.findAllByAge(18));

        System.out.println("----------------");

        System.out.println(studentsRepository.findAll());
        connection.close();
    }
}
