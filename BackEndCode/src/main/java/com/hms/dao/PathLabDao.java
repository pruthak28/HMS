package com.hms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hms.pojo.PathBookingInfo;
import com.hms.pojo.PathLabInfo;
import com.data.orion.connection.ConnectionFactory;

public class PathLabDao {

	private Connection connection;
	Statement statement;

	@SuppressWarnings("resource")
	public List<PathLabInfo> searchLabInfo(PathLabInfo pathInfo, int count) {
		connection = ConnectionFactory.getConnection();
		List<PathLabInfo> pathList = new ArrayList<PathLabInfo>();
		String result = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query1 = "Select pathinfo.path_id,pathinfo.lab_name,pathinfo.city,pathavail.tests from PathologyLabInfo pathinfo inner join PathTestAvailability pathavail on pathinfo.path_id=pathavail.path_id where";

			if (count == 2) {
				query1 = query1 + " pathinfo.city Like ? and pathavail.tests=?";
				ps = connection.prepareStatement(query1);
				ps.setString(1, "%" + pathInfo.getLocation());
				ps.setString(2, pathInfo.getLabTest());
			}

			if (count == 1) {
				if (!(pathInfo.getLabTest() == null)) {
					query1 = query1 + " pathavail.tests=?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, pathInfo.getLabTest());
				} else if (!(pathInfo.getLocation() == null)) {
					query1 = query1 + " pathinfo.city like ?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + pathInfo.getLocation());
				}
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				PathLabInfo pathinf = new PathLabInfo();
				pathinf.setPathId(rs.getString("pathinfo.path_id"));
				pathinf.setLocation(rs.getString("pathinfo.city"));
				pathinf.setLabName(rs.getString("pathinfo.lab_name"));
				pathinf.setLabTest(rs.getString("pathavail.tests"));
				pathList.add(pathinf);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pathList;
	}

	public List<PathBookingInfo> getPathHistForPatient(String username) {
		connection = ConnectionFactory.getConnection();
		List<PathBookingInfo> pathList = new ArrayList<PathBookingInfo>();
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
			query1 = "select * from PathLabBookingInfo p inner join PathologyLabInfo pf where p.patient_id=? and p.path_id=pf.path_id";

			ps = connection.prepareStatement(query1);

			ps.setInt(1, patId);

			rs = ps.executeQuery();

			while (rs.next()) {
				PathBookingInfo path = new PathBookingInfo();
				path.setPathLabName(rs.getString("lab_name"));
				Date date;
				Timestamp timestamp = rs.getTimestamp("Date_of_path_booking");
				date = new java.util.Date(timestamp.getTime());
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String strDate = sdf.format(date);
				path.setDateOfPathBooking(strDate);
				path.setPathStatus(rs.getString("path_status"));
				path.setTestSelected(rs.getString("test_selected"));
				path.setPathResult(rs.getString("path_result"));
				pathList.add(path);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pathList;
	}

}
