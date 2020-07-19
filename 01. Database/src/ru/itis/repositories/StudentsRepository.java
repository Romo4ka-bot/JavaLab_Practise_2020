package ru.itis.repositories;

import ru.itis.models.Student;

import java.util.List;

/**
 * @author Leontev Roman
 * 11-905
 * 12.07.20
 */

public interface StudentsRepository extends CrudRepository<Student> {
    List<Student> findAllByAge(int age);
}
