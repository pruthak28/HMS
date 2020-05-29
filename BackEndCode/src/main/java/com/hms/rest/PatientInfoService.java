package com.hms.rest;

import java.util.Map;
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

import com.hms.dao.PatientDao;
import com.hms.pojo.PatientInfo;

@Controller
@RequestMapping("/PatientInfoService")
public class PatientInfoService implements Observer {

	@POST
	@RequestMapping(value = "/savePatientInfo", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response savePatientInfo(@RequestBody String configRequest) {
		PatientDao patientDao = new PatientDao();

		JSONObject obj = new JSONObject();

		try {
			PatientInfo patientInfo = new PatientInfo();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			patientInfo.setName(json.get("name").toString());
			patientInfo.setDateOfBirth(json.get("dateOfBirth").toString());
			patientInfo.setEmailId(json.get("emailId").toString());
			patientInfo.setPassword(json.get("password").toString());
			patientInfo.setPhoneNumber(json.get("phoneNumber").toString());
			patientInfo.setSex(json.get("sex").toString());
			String result = patientDao.saveSignUpDetail(patientInfo);

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
	@RequestMapping(value = "/loginAutenticate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response loginAutenticate(@RequestBody String configRequest) {
		PatientDao patientDao = new PatientDao();
		JSONObject obj = new JSONObject();

		try {
			PatientInfo patientInfo = new PatientInfo();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			patientInfo.setEmailId(json.get("username").toString());
			patientInfo.setPassword(json.get("password").toString());
			patientInfo.setPersonType(json.get("personType").toString());
			String result = null, name = null;
			Map<String, String> mapData = patientDao
					.loginAuthenticate(patientInfo);

			for (Map.Entry<String, String> entry : mapData.entrySet()) {
				if (entry.getKey() == "result") {
					result = entry.getValue();

				}
				if (entry.getKey() == "name") {
					name = entry.getValue();
				}

			}
			obj.put(new String("result"), new String(result));
			obj.put(new String("name"), new String(name));

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
	@RequestMapping(value = "/bookDocAppointment", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response bookDocAppointment(@RequestBody String configRequest) {
		PatientDao patientDao = new PatientDao();
		JSONObject obj = new JSONObject();

		try {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			String patientUserName = json.get("patientUsername").toString();
			String doctorId = json.get("doctorId").toString();

			String result = patientDao.bookDocAppointment(patientUserName,
					doctorId);
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
	@RequestMapping(value = "/bookPathAppointment", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response bookPathAppointment(@RequestBody String configRequest) {
		PatientDao patientDao = new PatientDao();
		JSONObject obj = new JSONObject();

		try {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			String patientUserName = json.get("patientUsername").toString();
			String docUserName = json.get("pathId").toString();
			String testSelected = json.get("testSelected").toString();
			String result = patientDao.bookPathAppointment(patientUserName,
					docUserName, testSelected);
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

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}
}
