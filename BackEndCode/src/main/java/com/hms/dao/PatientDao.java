package com.hms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.hms.pojo.PatientInfo;
import com.data.orion.connection.ConnectionFactory;


public class PatientDao {

	private Connection connection;
	Statement statement;

	public String saveSignUpDetail(PatientInfo patientInfo) {
		connection = ConnectionFactory.getConnection();
		PreparedStatement preparedStmt;
		String flag = "";
		ResultSet rs = null;

		try {

			String sql = "INSERT INTO PatientInfo(name,email_id,DateOfBirth,sex,contact)"
					+ "VALUES(?,?,?,?,?)";
			preparedStmt = connection.prepareStatement(sql);

			preparedStmt.setString(1, patientInfo.getName());

			preparedStmt.setString(2, patientInfo.getEmailId());

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
				java.util.Date parsedDate = sdf.parse(patientInfo
						.getDateOfBirth());
				java.sql.Timestamp timestampTimeForDOB = new java.sql.Timestamp(
						parsedDate.getTime());
				preparedStmt.setTimestamp(3, timestampTimeForDOB);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			preparedStmt.setString(4, patientInfo.getSex());
			preparedStmt.setString(5, patientInfo.getPhoneNumber());

			preparedStmt.executeUpdate();

			String query = "INSERT INTO AuthenticationDetail(username,password,person_type)"
					+ "VALUES(?,?,?)";
			preparedStmt = connection.prepareStatement(query);

			preparedStmt.setString(1, patientInfo.getEmailId());
			preparedStmt.setString(2, patientInfo.getPassword());

			preparedStmt.setString(3, "P");

			preparedStmt.executeUpdate();
			flag = "Success";

		} catch (SQLException e) {
			flag = "Failure";
			e.printStackTrace();
		}
		return flag;
	}

	public Map<String, String> loginAuthenticate(PatientInfo patInfo) {
		connection = ConnectionFactory.getConnection();

		ResultSet rs = null;
		Map<String, String> result = new HashMap<String, String>();
		try {

			String query1 = "SELECT * FROM AuthenticationDetail WHERE username=?";

			PreparedStatement ps = connection.prepareStatement(query1);

			ps.setString(1, patInfo.getEmailId());

			rs = ps.executeQuery();

			if (rs.next()) {
				query1 = "SELECT * FROM AuthenticationDetail WHERE username=? and password=? and person_type= ?";

				ps = connection.prepareStatement(query1);

				ps.setString(1, patInfo.getEmailId());
				ps.setString(2, patInfo.getPassword());
				ps.setString(3, patInfo.getPersonType());
				rs = ps.executeQuery();
				if (rs.next()) {

					result.put("result", "Authentication successful");
					if(patInfo.getPersonType()=="P")
					{
					query1 = "SELECT * FROM PatientInfo WHERE email_id=?";
					String name = null;

					ps = connection.prepareStatement(query1);

					ps.setString(1, patInfo.getEmailId());
					rs = ps.executeQuery();
					if (rs.next()) {
						name = rs.getString("name");
					}
					result.put("name", name);
					}
					else
					{
						query1 = "SELECT * FROM DoctorInfo WHERE doc_email_id=?";
						String name = null;

						ps = connection.prepareStatement(query1);

						ps.setString(1, patInfo.getEmailId());
						rs = ps.executeQuery();
						if (rs.next()) {
							name = rs.getString("doc_name");
						}
						result.put("name", name);
					}
					
				} else {
					result.put("result", "Invalid Password/User Type");
					result.put("name", "Invalid");
				}

			} else {
				result.put("result", "Invalid Username");
				result.put("name", "Invalid");
			}
		} catch (SQLException e) {

		}
		return result;
	}

	public String bookDocAppointment(String patientUsername, String docId) {
		connection = ConnectionFactory.getConnection();

		String result = "";
		ResultSet rs = null;

		try {

			String query1 = "SELECT * FROM PatientInfo WHERE email_id=?";

			PreparedStatement ps = connection.prepareStatement(query1);

			ps.setString(1, patientUsername);

			rs = ps.executeQuery();
			int patId = 0;
			if (rs.next()) {
				patId = rs.getInt("patient_id");
			}

			String sql = "INSERT INTO DocBookingInfo(Date_of_doc_booking,status,doctor_id,patient_id)"
					+ "VALUES(?,?,?,?)";
			ps = connection.prepareStatement(sql);

			java.util.Date date = new java.util.Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(
					date.getTime());
			ps.setTimestamp(1, timestamp);
			ps.setString(2, "Appointment Booked");
			ps.setInt(3, Integer.parseInt(docId));
			ps.setInt(4, patId);
			ps.executeUpdate();

			result = "Success";

		} catch (SQLException e) {
			result = "Failure";
		}
		return result;
	}

	public String bookPathAppointment(String patientUsername, String pathId,
			String test) {
		connection = ConnectionFactory.getConnection();

		String result = "";
		ResultSet rs = null;

		try {

			String query1 = "SELECT * FROM PatientInfo WHERE email_id=?";

			PreparedStatement ps = connection.prepareStatement(query1);

			ps.setString(1, patientUsername);

			rs = ps.executeQuery();
			int patId = 0;
			if (rs.next()) {
				patId = rs.getInt("patient_id");
			}

			String sql = "INSERT INTO PathLabBookingInfo(Date_of_path_booking,test_selected,path_status,path_id,patient_id)"
					+ "VALUES(?,?,?,?,?)";
			ps = connection.prepareStatement(sql);

			java.util.Date date = new java.util.Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(
					date.getTime());
			ps.setTimestamp(1, timestamp);
			ps.setString(2, test);
			ps.setString(3, "Appointment Booked");

			ps.setInt(4, Integer.parseInt(pathId));
			ps.setInt(5, patId);

			ps.executeUpdate();

			result = "Success";

		} catch (SQLException e) {
			result = "Failure";
		}
		return result;
	}

}
