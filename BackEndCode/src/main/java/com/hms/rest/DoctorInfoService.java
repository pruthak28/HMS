package com.hms.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hms.dao.DoctorDao;
import com.hms.dao.PathLabDao;
import com.hms.pojo.DoctorBookingInfo;
import com.hms.pojo.DoctorInfo;
import com.hms.pojo.PathBookingInfo;

@Controller
@RequestMapping("/DoctorInfoService")
public class DoctorInfoService implements Observer {

	@POST
	@RequestMapping(value = "/saveDoctorInfo", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response saveDoctorInfo(@RequestBody String configRequest) {
		DoctorDao doctorDao = new DoctorDao();

		JSONObject obj = new JSONObject();

		try {
			DoctorInfo doctorInfo = new DoctorInfo();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			doctorInfo.setName(json.get("name").toString());
			doctorInfo.setEmailId(json.get("email").toString());
			doctorInfo.setSpecialization(json.get("specialization").toString());
			doctorInfo.setYearOfExp(json.get("exp").toString());
			doctorInfo.setPhoneNum(json.get("phoneNumber").toString());
			doctorInfo.setAddress(json.get("address").toString());
			doctorInfo.setDaysOfAvail(json.get("availability").toString());
			doctorInfo.setPassword(json.get("password").toString());
			String result = doctorDao.saveSignUpDetail(doctorInfo);

			/*
			 * Date date = new Date(); SimpleDateFormat sdf = new
			 * SimpleDateFormat("dd/MM/yyyy");
			 * 
			 * String strDate = sdf.format(date);
			 * configReqToSendToC3pCode.setRequestCreatedOn(strDate);
			 */

			/*
			 * Map<String, String> result = dcmConfigService
			 * .updateAlldetailsOnModify(configReqToSendToC3pCode);
			 * 
			 * for (Map.Entry<String, String> entry : result.entrySet()) { if
			 * (entry.getKey() == "requestID") { requestIdForConfig =
			 * entry.getValue();
			 * 
			 * } if (entry.getKey() == "result") { res = entry.getValue(); if
			 * (res.equalsIgnoreCase("true")) { data = "Success"; }
			 * 
			 * }
			 * 
			 * }
			 */
			obj.put(new String("result"), new String(result));

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

	@POST
	@RequestMapping(value = "/searchDocAvailable", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response searchDocAvailable(@RequestBody String configRequest) {
		DoctorDao doctorDao = new DoctorDao();
		String jsonArray = "";
		JSONObject obj = new JSONObject();
		int count = 0;
		String dateForAvail = "";
		try {
			DoctorInfo doctorInfo = new DoctorInfo();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			if (json.containsKey("specialization")
					&& org.springframework.util.StringUtils.hasLength((json
							.get("specialization").toString()))) {

				doctorInfo.setSpecialization(json.get("specialization")
						.toString());
				count = count + 1;
			}

			if (json.containsKey("address")
					&& org.springframework.util.StringUtils.hasLength((json
							.get("address").toString()))) {

				doctorInfo.setAddress(json.get("address").toString());
				count = count + 1;
			}

			if (json.containsKey("dateForAvail")
					&& org.springframework.util.StringUtils.hasLength((json
							.get("dateForAvail").toString()))) {

				dateForAvail = json.get("dateForAvail").toString();
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
				java.util.Date parsedDate = sdf.parse(dateForAvail);
				DateFormat format2 = new SimpleDateFormat("EEEE");
				String finalDay = format2.format(parsedDate);
				System.out.println("" + finalDay);
				doctorInfo.setDaysOfAvail(finalDay);
				count = count + 1;
			}

			List<DoctorInfo> medList = doctorDao.searchDocAvailInfo(doctorInfo,
					count);

			if (medList.size() > 0) {
				jsonArray = new Gson().toJson(medList);
				obj.put(new String("ListOfDoctors"), jsonArray);
				obj.put(new String("dateForAvail"), dateForAvail);

			}

			else {
				obj.put(new String("ListOfDoctors"), "No Result Found");
				obj.put(new String("dateForAvail"), dateForAvail);
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

	@POST
	@RequestMapping(value = "/getHistoryForPatient", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response getHistoryForPatient(@RequestBody String configRequest) {
		DoctorDao doctorDao = new DoctorDao();
		PathLabDao pathDao = new PathLabDao();
		String jsonArray = "";
		JSONObject obj = new JSONObject();
		int count = 0;
		try {
			DoctorInfo doctorInfo = new DoctorInfo();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			String patientUserName = json.get("userNamePatient").toString();

			List<DoctorBookingInfo> docList = doctorDao
					.getDoctorHistForPatient(patientUserName);

			if (docList.size() > 0) {
				jsonArray = new Gson().toJson(docList);
				obj.put(new String("ListOfDocHistory"), jsonArray);
			}

			else {
				obj.put(new String("ListOfDocHistory"), "No Result Found");
			}

			List<PathBookingInfo> pathList = pathDao
					.getPathHistForPatient(patientUserName);

			if (pathList.size() > 0) {
				jsonArray = new Gson().toJson(pathList);
				obj.put(new String("ListOfPathHistory"), jsonArray);
			}

			else {
				obj.put(new String("ListOfPathHistory"), "No Result Found");
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

	@POST
	@RequestMapping(value = "/getPatientListForDoctor", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response getPatientListForDoctor(@RequestBody String configRequest) {
		DoctorDao doctorDao = new DoctorDao();
		PathLabDao pathDao = new PathLabDao();
		String jsonArray = "";
		JSONObject obj = new JSONObject();
		int count = 0;
		try {
			DoctorInfo doctorInfo = new DoctorInfo();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			String patientUserName = json.get("userNameDoctor").toString();

			List<DoctorBookingInfo> patList = doctorDao
					.getPatientHistForDoc(patientUserName);

			if (patList.size() > 0) {
				jsonArray = new Gson().toJson(patList);
				obj.put(new String("ListOfPatientBooked"), jsonArray);
			}

			else {
				obj.put(new String("ListOfPatientBooked"), "No Result Found");
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

	@POST
	@RequestMapping(value = "/updateDoctorDiagnosisAndTest", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response updateDoctorDiagnosisAndTest(
			@RequestBody String configRequest) {
		DoctorDao doctorDao = new DoctorDao();
		PathLabDao pathDao = new PathLabDao();
		String jsonArray = "";
		JSONObject obj = new JSONObject();
		int count = 0;
		try {
			DoctorBookingInfo doctorInfo = new DoctorBookingInfo();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			doctorInfo.setBookingId(json.get("docBookingId").toString());
			doctorInfo.setDiagnosis(json.get("diagnosis").toString());
			doctorInfo.setTest_suggested(json.get("testSuggested").toString());
			doctorInfo.setMedicine(json.get("medicine").toString());

			String result = doctorDao.updateDocDiganosis(doctorInfo);

			obj.put(new String("result"), result);

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
