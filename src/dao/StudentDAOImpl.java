package dao;

import db.DatabaseConnection;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    private static final String SQL_INSERT =
            "INSERT INTO students (name, roll_no, course, branch, semester, email, phone) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL   = "SELECT * FROM students ORDER BY name";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String SQL_SEARCH =
            "SELECT * FROM students WHERE name LIKE ? OR roll_no LIKE ? ORDER BY name";
    private static final String SQL_UPDATE =
            "UPDATE students SET name=?, roll_no=?, course=?, branch=?, semester=?, email=?, phone=? WHERE id=?";
    private static final String SQL_DELETE = "DELETE FROM students WHERE id=?";

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("roll_no"),
                rs.getString("course"),
                rs.getString("branch"),
                rs.getInt("semester"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }

    @Override
    public int addStudent(Student s) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement(
                SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getRollNo());
            ps.setString(3, s.getCourse());
            ps.setString(4, s.getBranch());
            ps.setInt   (5, s.getSemester());
            ps.setString(6, s.getEmail());
            ps.setString(7, s.getPhone());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    s.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    @Override
    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public Student getStudentById(int id) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<Student> searchStudents(String keyword) throws SQLException {
        List<Student> list = new ArrayList<>();
        String pattern = "%" + keyword + "%";
        try (PreparedStatement ps = getConn().prepareStatement(SQL_SEARCH)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public int updateStudent(Student s) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement(SQL_UPDATE)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getRollNo());
            ps.setString(3, s.getCourse());
            ps.setString(4, s.getBranch());
            ps.setInt   (5, s.getSemester());
            ps.setString(6, s.getEmail());
            ps.setString(7, s.getPhone());
            ps.setInt   (8, s.getId());
            return ps.executeUpdate();
        }
    }

    @Override
    public int deleteStudent(int id) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}