package ru.kpfu.itis.repositories;

import ru.kpfu.itis.models.Mentor;
import ru.kpfu.itis.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leontev Roman
 * 11-905
 * 12.07.20
 */

public class StudentsRepositoryJdbcImpl implements StudentsRepository {

    //language=SQL
    private static final String SQL_SELECT_BY_ID_STUDENT = "select * from student where id = ";
    private static final String SQL_SELECT_BY_STUD_ID_MENTOR = "select * from mentor where student_id = ";
    //language=SQL
    private static final String SQL_UPDATE_STUDENT = "update student set first_name = '%s', last_name = '%s', " +
            "age = %d, group_number = %d where id = %d";
    //language=SQL
    private static final String SQL_INSERT_STUDENT = "insert into student (first_name, last_name, " +
            "age, group_number) values ('%s', '%s', %d, %d)";
    //language=SQL
    public static final String SQL_INSERT_MENTOR = "insert into mentor " +
            "(first_name, last_name, student_id, subject_id) values ('%s', '%s', %d, %d)";
    private static final String SQL_SELECT_BY_AGE = "select * from student where age = ";
    //language=SQL
    private static final String SQL_SELECT_JOIN_STUD_AND_MENTOR =
            "select m.id as m_id, " +
                    "m.first_name as m_first_name, " +
                    "m.last_name as m_last_name, " +
                    "m.subject_id as m_subject_id, " +
                    "s.id as s_id, " +
                    "s.first_name as s_first_name, " +
                    "s.last_name as s_last_name, " +
                    "s.age as s_age, " +
                    "s.group_number as s_group_number " +
                    "from student s left join mentor m on s.id = student_id";
    //language=SQL
    private static final String SQL_DELET = "delete from mentor where student_id = ";


    private Connection connection;

    public StudentsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Student> findAllByAge(int age) {
        Statement statement1 = null;
        ResultSet resultStudent = null;

        try {
            statement1 = connection.createStatement();
            //находим студентов
            resultStudent = statement1.executeQuery(SQL_SELECT_BY_AGE + age);

            List<Student> listStudent = new ArrayList<>();
            Student student;

            while (resultStudent.next()) {
                student = new Student(
                        resultStudent.getLong("id"),
                        resultStudent.getString("first_name"),
                        resultStudent.getString("last_name"),
                        resultStudent.getInt("age"),
                        resultStudent.getInt("group_number"));


                //для того, чтобы найти менторов нашего студента нужен еще один объект класса Statement
                try (Statement statement2 = connection.createStatement()) {

                    //находим менторов у студента с id == student.getId()
                    ResultSet resultMentor = statement2.executeQuery(SQL_SELECT_BY_STUD_ID_MENTOR + student.getId());
                    List<Mentor> listMentor = new ArrayList<>();

                    while (resultMentor.next()) {
                        listMentor.add(new Mentor(
                                resultMentor.getLong("id"),
                                resultMentor.getString("first_name"),
                                resultMentor.getString("last_name"),
                                student,
                                resultMentor.getInt("subject_id")
                        ));
                    }

                    //добавляем заполненный список менторов
                    student.setMentors(listMentor);

                    listStudent.add(student);

                } catch (SQLException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            return listStudent;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (resultStudent != null) {
                try {
                    resultStudent.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement1 != null) {
                try {
                    statement1.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }


    // Необходимо вытащить список всех студентов, при этом у каждого студента должен быть проставлен список менторов
    // у менторов в свою очередь ничего проставлять (кроме имени, фамилии, id не надо)
    // student1(id, firstName, ..., mentors = [{id, firstName, lastName, null}, {}, ), student2, student3
    // все сделать одним запросом
    @Override
    public List<Student> findAll() {
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_JOIN_STUD_AND_MENTOR);

            List<Student> list = new ArrayList<>();

            long id = 0;

            if (result.next()) {
                id = result.getLong("s_id");
            }

            //добавляем первого студента
            addStudent(list, id, result);

            while (result.next()) {
                for (int i = 0; i < list.size(); i++) {
                    Student current = list.get(i);
                    //если есть студент с таким же id, то добавляем нового ментора в список
                    if (current.getId() == id) {
                        current.getMentors().add(new Mentor(
                                result.getLong("m_id"),
                                result.getString("m_first_name"),
                                result.getString("m_last_name"),
                                null,
                                null
                        ));
                    } else {
                        //добавляем студентов
                        addStudent(list, id, result);
                        break;
                    }
                }
                id = result.getLong("s_id");
            }

            return list;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public Student findById(Long id) {
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            //находим студентов
            result = statement.executeQuery(SQL_SELECT_BY_ID_STUDENT + id);
            Student student;
            if (result.next()) {
                student = new Student(
                        id,
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getInt("age"),
                        result.getInt("group_number")
                );
            } else return null;

            //находим менторов наших студентов
            result = statement.executeQuery(SQL_SELECT_BY_STUD_ID_MENTOR + id);
            List<Mentor> listMentor = new ArrayList<>();

            while (result.next()) {
                listMentor.add(new Mentor(
                                result.getLong("id"),
                                result.getString("first_name"),
                                result.getString("last_name"),
                                student,
                                result.getInt("subject_id")
                        )
                );
            }

            student.setMentors(listMentor);

            return student;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    // просто вызывается insert для сущности
    // student = Student(null, 'Марсель', 'Сидиков', 26, 915)
    // studentsRepository.save(student);
    // // student = Student(3, 'Марсель', 'Сидиков', 26, 915)
    @Override
    public void save(Student entity) {
        Statement statement = null;

        try {
            statement = connection.createStatement();

            String insertStudent = String.format(SQL_INSERT_STUDENT,
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getAge(),
                    entity.getGroupNumber()
            );

            //сохраняем id, чтобы изменить у нашей сущности entity
            ResultSet id = statement.executeQuery(insertStudent + " returning id");

            if (id.next()) {
                entity.setId(id.getLong("id"));
            }

            for (int i = 0; i < entity.getMentors().size(); i++) {
                Mentor mentor = entity.getMentors().get(i);
                String insertMentor = String.format(SQL_INSERT_MENTOR,
                        mentor.getFirstName(),
                        mentor.getLastName(),
                        mentor.getStudent().getId(),
                        mentor.getSubject()
                );

                id = statement.executeQuery(insertMentor + " returning id");

                if (id.next()) {
                    mentor.setId(id.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    // для сущности, у которой задан id выполнить обновление всех полей

    // student = Student(3, 'Марсель', 'Сидиков', 26, 915)
    // student.setFirstName("Игорь")
    // student.setLastName(null);
    // studentsRepository.update(student);
    // (3, 'Игорь', null, 26, 915)

    @Override
    public void update(Student entity) {
        Statement statement = null;

        try {
            statement = connection.createStatement();
            String updateStudent = String.format(SQL_UPDATE_STUDENT,
                    entity.getFirstName(),
                    null,
                    entity.getAge(),
                    entity.getGroupNumber(),
                    entity.getId()
            );
            statement.executeUpdate(updateStudent);

            //удаляем менторов у студента с id == entity.id
            statement.executeUpdate(SQL_DELET + entity.getId());

            //добавляем менторов у нового студента
            for (int i = 0; i < entity.getMentors().size(); i++) {
                Mentor mentor = entity.getMentors().get(i);
                String insertMentor = String.format(SQL_INSERT_MENTOR,
                        mentor.getFirstName(),
                        mentor.getLastName(),
                        mentor.getStudent().getId(),
                        mentor.getSubject()
                );

                ResultSet id = statement.executeQuery(insertMentor + " returning id");

                if (id.next()) {
                    mentor.setId(id.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    //вспомогательный метод для добавления студента
    private void addStudent(List<Student> listStudent, long id, ResultSet result) {

        try {
            List<Mentor> listMentor = new ArrayList<>();
            Student student = new Student(
                    id,
                    result.getString("s_first_name"),
                    result.getString("s_last_name"),
                    result.getInt("s_age"),
                    result.getInt("s_group_number"),
                    listMentor
            );

            listMentor.add(new Mentor(
                    result.getLong("m_id"),
                    result.getString("m_first_name"),
                    result.getString("m_last_name"),
                    null,
                    null
            ));

            listStudent.add(student);

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
