package com.hms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.data.orion.connection.ConnectionFactory;
import com.hms.pojo.MedicineInfo;

public class MedicineDao {

	private Connection connection;
	Statement statement;

	@SuppressWarnings("resource")
	public List<MedicineInfo> searchMedicineInfo(MedicineInfo medInfo, int count) {
		connection = ConnectionFactory.getConnection();
		List<MedicineInfo> medList = new ArrayList<MedicineInfo>();
		String result = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query1 = "Select * from MedicineInfoSearch where";

			if (count == 3) {
				query1 = query1
						+ " pharma_name like ? and location LIKE ? and medicine_name like ?";
				ps = connection.prepareStatement(query1);
				ps.setString(1, "%" + medInfo.getPharmacyName() + "%");
				ps.setString(2, "%" + medInfo.getLocation() + "%");
				ps.setString(3, "%" + medInfo.getMedicineName() + "%");
			}

			if (count == 2) {
				if (!(medInfo.getMedicineName() == null)
						&& !(medInfo.getPharmacyName() == null)) {

					query1 = query1
							+ " pharma_name like ? and medicine_name like ?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + medInfo.getPharmacyName() + "%");
					ps.setString(2, "%" + medInfo.getMedicineName() + "%");
				} else if (!(medInfo.getMedicineName() == null)
						&& !(medInfo.getLocation() == null)) {
					query1 = query1
							+ " medicine_name like ? and location like ?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + medInfo.getLocation() + "%");
					ps.setString(2, "%" + medInfo.getMedicineName() + "%");
				} else if (!(medInfo.getPharmacyName() == null)
						&& !(medInfo.getLocation() == null)) {

					query1 = query1 + " pharma_name like ? and location like ?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + medInfo.getPharmacyName() + "%");
					ps.setString(2, "%" + medInfo.getLocation() + "%");
				}
			}

			if (count == 1) {
				if (!(medInfo.getMedicineName() == null)) {
					query1 = query1 + " medicine_name like ?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + medInfo.getMedicineName() + "%");
				} else if (!(medInfo.getPharmacyName() == null)) {
					query1 = query1 + " pharma_name like?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + medInfo.getPharmacyName() + "%");
				} else if (!(medInfo.getLocation() == null)) {
					query1 = query1 + " location like ?";
					ps = connection.prepareStatement(query1);
					ps.setString(1, "%" + medInfo.getLocation() + "%");
				}
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				MedicineInfo med = new MedicineInfo();
				med.setLocation(rs.getString("location"));
				med.setMedicineName(rs.getString("medicine_name"));
				med.setPharmacyName(rs.getString("pharma_name"));
				med.setPrice(String.valueOf(rs.getDouble("price")));
				med.setQuantity(String.valueOf(rs.getInt("available_quantity")));
				medList.add(med);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return medList;
	}

}
