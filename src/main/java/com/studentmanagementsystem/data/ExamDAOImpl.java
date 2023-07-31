package main.java.com.studentmanagementsystem.data;

import main.java.com.studentmanagementsystem.model.Exam;
import main.java.com.studentmanagementsystem.util.DatabaseManager;
import main.java.com.studentmanagementsystem.data.query.ExamQueryConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamDAOImpl implements ExamDAO {

  private DatabaseManager databaseManager;

  public ExamDAOImpl() {
    databaseManager = DatabaseManager.getInstance();
  }

  @Override
  public void addExam(Exam exam) {
    try (Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(ExamQueryConstants.INSERT_EXAM)) {

      preparedStatement.setInt(1, exam.getExamID());
      preparedStatement.setDate(2, new java.sql.Date(exam.getExamDate().getTime()));
      preparedStatement.setString(3, exam.getExamType());
      preparedStatement.setString(4, exam.getExamRoomNo());
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateExam(Exam exam) {
    try (Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(ExamQueryConstants.UPDATE_EXAM)) {

      preparedStatement.setDate(1, new java.sql.Date(exam.getExamDate().getTime()));
      preparedStatement.setString(2, exam.getExamType());
      preparedStatement.setString(3, exam.getExamRoomNo());
      preparedStatement.setInt(4, exam.getExamID());
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteExam(Date examDate, String examType) {
    try (Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(ExamQueryConstants.DELETE_EXAM)) {

      preparedStatement.setDate(1, new java.sql.Date(examDate.getTime()));
      preparedStatement.setString(2, examType);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Exam getExamByDateAndType(Date examDate, String examType) {
    Exam exam = null;

    try (Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(ExamQueryConstants.GET_EXAM_BY_DATE_AND_TYPE)) {

      preparedStatement.setDate(1, new java.sql.Date(examDate.getTime()));
      preparedStatement.setString(2, examType);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          exam = extractExamFromResultSet(resultSet);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return exam;
  }

  @Override
  public List<Exam> getAllExams() {
    List<Exam> exams = new ArrayList<>();

    try (Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(ExamQueryConstants.GET_ALL_EXAMS);
        ResultSet resultSet = preparedStatement.executeQuery()) {

      while (resultSet.next()) {
        Exam exam = extractExamFromResultSet(resultSet);
        exams.add(exam);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return exams;
  }

  private Exam extractExamFromResultSet(ResultSet resultSet) throws SQLException {
    int examID = resultSet.getInt("E_ID");
    Date examDate = resultSet.getDate("E_date");
    int totalMark = resultSet.getInt("totalMark");
    String examType = resultSet.getString("E_type");
    String examRoomNo = resultSet.getString("examLocation");

    return new Exam(examID, examDate, totalMark, examType, examRoomNo);
  }
}
