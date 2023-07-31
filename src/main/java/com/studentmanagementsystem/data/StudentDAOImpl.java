package main.java.com.studentmanagementsystem.data;

import java.util.Date;
import main.java.com.studentmanagementsystem.model.Student;
import main.java.com.studentmanagementsystem.util.DatabaseManager;
import main.java.com.studentmanagementsystem.data.query.StudentQueryConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

  private DatabaseManager databaseManager;

  public StudentDAOImpl() {
    databaseManager = DatabaseManager.getInstance();
  }

  @Override
  public void addStudent(Student student) {
    Connection connection = null;
    try {
      connection = databaseManager.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(StudentQueryConstants.INSERT_STUDENT);

      preparedStatement.setInt(1, student.getStudentId());
      preparedStatement.setString(2, student.getName());
      preparedStatement.setString(3, student.getEmail());
      preparedStatement.setString(4, student.getContact());
      preparedStatement.setDate(5, new java.sql.Date(student.getEnrollYear().getTime()));
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      // Handle or log the exception, don't just print the stack trace.
      e.printStackTrace();
    } finally {
      if (connection != null) {
        databaseManager.releaseConnection(connection);
      }
    }
  }


  @Override
  public void updateStudent(Student student) {
    Connection connection = null;
    try {
      connection = databaseManager.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(StudentQueryConstants.UPDATE_STUDENT);

      preparedStatement.setString(1, student.getName());
      preparedStatement.setString(2, student.getEmail());
      preparedStatement.setString(3, student.getContact());
      preparedStatement.setDate(4, new java.sql.Date(student.getEnrollYear().getTime()));
      preparedStatement.setInt(5, student.getStudentId());
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      // Handle or log the exception, don't just print the stack trace.
      e.printStackTrace();
    } finally {
      databaseManager.releaseConnection(connection);
    }
  }

  @Override
  public void deleteStudent(int id) {
    Connection connection = null;
    try {
      connection = databaseManager.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(StudentQueryConstants.DELETE_STUDENT);

      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      // Handle or log the exception, don't just print the stack trace.
      e.printStackTrace();
    } finally {
      databaseManager.releaseConnection(connection);
    }
  }

  @Override
  public Student getStudentById(int id) {
    Student student = null;
    Connection connection = null;
    try {
      connection = databaseManager.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(StudentQueryConstants.GET_STUDENT_BY_ID);

      preparedStatement.setInt(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          student = extractStudentFromResultSet(resultSet);
        }
      }
    } catch (SQLException e) {
      // Handle or log the exception, don't just print the stack trace.
      e.printStackTrace();
    } finally {
      databaseManager.releaseConnection(connection);
    }

    return student;
  }

  @Override
  public List<Student> getAllStudents() {
    List<Student> students = new ArrayList<>();
    Connection connection = null;
    try {
      connection = databaseManager.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(StudentQueryConstants.GET_ALL_STUDENTS);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Student student = extractStudentFromResultSet(resultSet);
          students.add(student);
        }
      }
    } catch (SQLException e) {
      // Handle or log the exception, don't just print the stack trace.
      e.printStackTrace();
    } finally {
      databaseManager.releaseConnection(connection);
    }

    return students;
  }

  private Student extractStudentFromResultSet(ResultSet resultSet) throws SQLException {
    int studentId = resultSet.getInt("S_ID");
    String name = resultSet.getString("S_name");
    String email = resultSet.getString("S_mail");
    String contact = resultSet.getString("S_contact");
    java.sql.Date enrollDate = resultSet.getDate("EnrollYear");
    Date enrollYear = new Date(enrollDate.getTime());

    return new Student(studentId, name, contact, email, enrollYear);
  }
}
