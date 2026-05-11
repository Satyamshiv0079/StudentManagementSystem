package dao;

import model.Student;
import java.sql.SQLException;
import java.util.List;

public interface StudentDAO {
    int           addStudent(Student student)  throws SQLException;
    List<Student> getAllStudents()              throws SQLException;
    Student       getStudentById(int id)       throws SQLException;
    List<Student> searchStudents(String kw)    throws SQLException;
    int           updateStudent(Student s)     throws SQLException;
    int           deleteStudent(int id)        throws SQLException;
}