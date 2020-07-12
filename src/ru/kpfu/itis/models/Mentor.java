package ru.kpfu.itis.models;

/**
 * @author Leontev Roman
 * 11-905
 * 12.07.20
 */

public class Mentor {

    private Long id;
    private String firstName;
    private String lastName;
    private Student student;
    private int subject;

    public Mentor(Long id, String firstName, String lastName, Student student, int subject) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.student = student;
        this.subject = subject;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Mentor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", student=" + student.getId() +
                ", subject=" + subject +
                '}';
    }
}
