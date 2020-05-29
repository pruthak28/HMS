package com.hms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.hms.pojo.DoctorBookingInfo;
import com.hms.pojo.DoctorInfo;
import com.data.orion.connection.ConnectionFactory;

public class DoctorDao {

	private Connection connection;
	Statement statement;

	public String saveSignUpDetail(DoctorInfo doctorInfo) {
		connection = ConnectionFactory.getConnection();
		PreparedStatement preparedStmt;
		String flag = "";
		ResultSet rs = null;

		try {

			String sql = "INSERT INTO DoctorInfo(doc_name,doc_email_id,specialization,address,contact,yearsOfExp)"
					+ "VALUES(?,?,?,?,?,?)";
			preparedStmt = connection.prepareStatement(sql);

			preparedStmt.setString(1, doctorInfo.getName());

			preparedStmt.setString(2, doctorInfo.getEmailId());

			preparedStmt.setString(3, doctorInfo.getSpecialization());
			preparedStmt.setString(4, doctorInfo.getAddress());
			preparedStmt.setString(5, doctorInfo.getPhoneNum());
			preparedStmt.setString(6, doctorInfo.getYearOfExp());

			preparedStmt.executeUpdate();

			String query1 = "SELECT * FROM DoctorInfo WHERE doc_email_id=?";

			PreparedStatement ps = connection.prepareStatement(query1);

			ps.setString(1, doctorInfo.getEmailId());

			rs = ps.executeQuery();
			int docId = 0;
			if (rs.next()) {
				docId = rs.getInt("doctor_id");
			}

			List<String> items = Arrays.asList(doctorInfo.getDaysOfAvail()
					.split("\\s*,\\s*"));

			for (String str : items) {

				String query = "INSERT INTO WeekAvailability(day_of_avail,doctor_id)"
						+ "VALUES(?,?)";
				preparedStmt = connection.prepareStatement(query);
				preparedStmt.setString(1, str);
				preparedStmt.setInt(2, docId);
				preparedStmt.executeUpdate();
			}
			String query = "INSERT INTO AuthenticationDetail(username,password,person_type)"
					+ "VALUES(?,?,?)";
			preparedStmt = connection.prepareStatement(query);

			preparedStmt.setString(1, doctorInfo.getEmailId());
			preparedStmt.setString(2, doctorInfo.getPassword());

			preparedStmt.setString(3, "D");

			preparedStmt.executeUpdate();
			flag = "Success";

		} catch (SQLException e) {
			flag = "Failure";
			e.printStackTrace();
		}
		return flag;
	}

	@SuppressWarnings("resource")
	public List<DoctorInfo> searchDocAvailInfo(DoctorInfo docInfo, int count) {
		connection = ConnectionFactory.getConnection();
		List<DoctorInfo> docList = new ArrayList<DoctorInfo>();
		String result = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query1 = "Select d.doctor_id,d.doc_email_id,d.doc_name,d.specialization,d.address,d.contact,d.yearsOfExp,a.day_of_avail from doctorinfo d inner join WeekAvailability a on d.doctor_id=a.doctor_id where";

			if (count == 3) {
				query1 = query1
						+ " d.specialization like ? and d.address LIKE ? and a.day_of_avail=?";
				ps = connection.prepareStatement(query1);
				ps.setString(1, "%" + docInfo.getSpecialization() + "%");
				ps.setString(2, "%" + docInfo.getAddress());
				ps.setString(3, docInfo.getDaysOfAvail());
			}

			if (count == 2) {
				if (!(docInfo.getSpecialization() == null)
						&& !(docInfo.getDaysOfAvail() == null)) {

					query1 = query1
							+ " d.specialization like ? and a.day_of_avail=?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + docInfo.getSpecialization() + "%");
					ps.setString(2, docInfo.getDaysOfAvail());
				} else if (!(docInfo.getDaysOfAvail() == null)
						&& !(docInfo.getAddress() == null)) {
					query1 = query1 + " a.day_of_avail=? and d.address like ?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, docInfo.getDaysOfAvail());
					ps.setString(2, "%" + docInfo.getAddress() + "%");
				} else if (!(docInfo.getSpecialization() == null)
						&& !(docInfo.getAddress() == null)) {

					query1 = query1
							+ " d.specialization like ? and d.address like ?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + docInfo.getSpecialization() + "%");
					ps.setString(2, "%" + docInfo.getAddress() + "%");

				}
			}

			if (count == 1) {
				if (!(docInfo.getSpecialization() == null)) {
					query1 = query1 + " d.specialization like ?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + docInfo.getSpecialization() + "%");
				} else if (!(docInfo.getDaysOfAvail() == null)) {
					query1 = query1 + " a.day_of_avail=?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, docInfo.getDaysOfAvail());
				} else if (!(docInfo.getAddress() == null)) {
					query1 = query1 + " d.address like ?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + docInfo.getAddress() + "%");
				}
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				DoctorInfo doc = new DoctorInfo();
				doc.setDocId(rs.getString("d.doctor_id"));
				doc.setName(rs.getString("d.doc_name"));
				doc.setEmailId(rs.getString("d.doc_email_id"));
				doc.setAddress(rs.getString("d.address"));
				doc.setSpecialization(rs.getString("d.specialization"));
				doc.setYearOfExp(String.valueOf(rs.getInt("d.yearsOfExp")));
				doc.setDaysOfAvail(rs.getString("a.day_of_avail"));
				doc.setPhoneNum(rs.getString("d.contact"));
				docList.add(doc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return docList;
	}

	@SuppressWarnings("resource")
	public List<DoctorBookingInfo> getDoctorHistForPatient(String username) {
		connection = ConnectionFactory.getConnection();
		List<DoctorBookingInfo> docList = new ArrayList<DoctorBookingInfo>();
		String result = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			String query1 = "SELECT * FROM PatientInfo WHERE email_id=?";

			ps = connection.prepareStatement(query1);

			ps.setString(1, username);

			rs = ps.executeQuery();
			int patId = 0;
			if (rs.next()) {
				patId = rs.getInt("patient_id");
			}
			query1 = "select * from DocBookingInfo d inner join DoctorInfo df where d.patient_id=? and d.doctor_id=df.doctor_id";

			ps = connection.prepareStatement(query1);

			ps.setInt(1, patId);

			rs = ps.executeQuery();

			while (rs.next()) {
				DoctorBookingInfo doc = new DoctorBookingInfo();
				Date date;
				doc.setDoctorName(rs.getString("doc_name"));
				Timestamp timestamp = rs.getTimestamp("date_of_doc_booking");
				date = new java.util.Date(timestamp.getTime());
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String strDate = sdf.format(date);
				doc.setDate_of_doc_booking(strDate);
				doc.setStatus(rs.getString("status"));
				doc.setDiagnosis(rs.getString("diagnosis"));
				doc.setTest_suggested(rs.getString("test_suggested"));
				doc.setMedicine(rs.getString("medicine"));
				docList.add(doc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return docList;
	}

	@SuppressWarnings("resource")
	public List<DoctorBookingInfo> getPatientHistForDoc(String username) {
		connection = ConnectionFactory.getConnection();
		List<DoctorBookingInfo> docList = new ArrayList<DoctorBookingInfo>();
		String result = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			String query1 = "SELECT * FROM DoctorInfo WHERE doc_email_id=?";

			ps = connection.prepareStatement(query1);

			ps.setString(1, username);

			rs = ps.executeQuery();
			int docId = 0;
			if (rs.next()) {
				docId = rs.getInt("doctor_id");
			}
			query1 = "select * from DocBookingInfo d inner join PatientInfo df where d.doctor_id=? and d.patient_id=df.patient_id";

			ps = connection.prepareStatement(query1);

			ps.setInt(1, docId);

			rs = ps.executeQuery();

			while (rs.next()) {
				DoctorBookingInfo doc = new DoctorBookingInfo();
				Date date;
				doc.setPatientName(rs.getString("name"));
				doc.setPtientEmailId(rs.getString("email_id"));
				Timestamp timestamp = rs.getTimestamp("date_of_doc_booking");
				date = new java.util.Date(timestamp.getTime());
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String strDate = sdf.format(date);
				doc.setDate_of_doc_booking(strDate);

				doc.setDoctor_id(rs.getString("doctor_id"));
				doc.setPatient_id(rs.getString("patient_id"));
				doc.setBookingId(rs.getString("doc_book_id"));
				docList.add(doc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return docList;
	}

	public String updateDocDiganosis(DoctorBookingInfo docInfo) {
		connection = ConnectionFactory.getConnection();
		PreparedStatement preparedStmt;
		String flag = "";
		ResultSet rs = null;

		try {

			String sql = "update DocBookingInfo set diagnosis = ?, test_suggested= ?,medicine=?,status=?"
					+ "where doc_book_id = ?";
			preparedStmt = connection.prepareStatement(sql);

			preparedStmt.setString(1, docInfo.getDiagnosis());

			preparedStmt.setString(2, docInfo.getTest_suggested());

			preparedStmt.setString(3, docInfo.getMedicine());
			preparedStmt.setString(4, "Completed");
			preparedStmt.setString(5, docInfo.getBookingId());

			preparedStmt.executeUpdate();

			flag = "Success";

		} catch (SQLException e) {
			flag = "Failure";
			e.printStackTrace();
		}
		return flag;
	}

}
