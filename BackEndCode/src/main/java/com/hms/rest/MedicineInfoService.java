package com.hms.rest;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hms.dao.MedicineDao;
import com.hms.dao.PatientDao;
import com.hms.pojo.MedicineInfo;
import com.hms.pojo.PatientInfo;

@Controller
@RequestMapping("/MedicineInfoService")
public class MedicineInfoService implements Observer {

	@POST
	@RequestMapping(value = "/searchMedicineInfo", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response searchMedicineInfo(@RequestBody String configRequest) {
		MedicineDao medicineDao = new MedicineDao();
		String jsonArray = "";
		JSONObject obj = new JSONObject();
		int count = 0;
		try {
			MedicineInfo medicineInfo = new MedicineInfo();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			if (json.containsKey("pharmacyName")
					&& org.springframework.util.StringUtils.hasLength((json
							.get("pharmacyName").toString()))) {

				medicineInfo.setPharmacyName(json.get("pharmacyName")
						.toString());
				count = count + 1;
			}

			if (json.containsKey("location")
					&& org.springframework.util.StringUtils.hasLength((json
							.get("location").toString()))) {

				medicineInfo.setLocation(json.get("location").toString());
				count = count + 1;
			}

			if (json.containsKey("medicineName")
					&& org.springframework.util.StringUtils.hasLength((json
							.get("medicineName").toString()))) {

				medicineInfo.setMedicineName(json.get("medicineName")
						.toString());
				count = count + 1;
			}

			List<MedicineInfo> medList = medicineDao.searchMedicineInfo(
					medicineInfo, count);

			if (medList.size() > 0) {
				jsonArray = new Gson().toJson(medList);
				obj.put(new String("ListOfMedicine"), jsonArray);
			}

			else {
				obj.put(new String("ListOfMedicine"), "No Result Found");
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return Response
				.status(200)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers",
						"origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods",
						"GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(obj)
				.build();

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
