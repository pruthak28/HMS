package com.hms.rest;

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
import com.hms.dao.PathLabDao;
import com.hms.pojo.PathLabInfo;

@Controller
@RequestMapping("/PathLabInfoService")
public class PathLabInfoService implements Observer {

	@POST
	@RequestMapping(value = "/searchPathLabInfo", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response searchPathLabInfo(@RequestBody String configRequest) {
		PathLabDao pathLabDao = new PathLabDao();
		String jsonArray = "";
		JSONObject obj = new JSONObject();
		int count = 0;
		try {
			PathLabInfo pathLabInfo = new PathLabInfo();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(configRequest);

			if (json.containsKey("location")
					&& org.springframework.util.StringUtils.hasLength((json
							.get("location").toString()))) {

				pathLabInfo.setLocation(json.get("location").toString());
				count = count + 1;
			}

			if (json.containsKey("labTest")
					&& org.springframework.util.StringUtils.hasLength((json
							.get("labTest").toString()))) {

				pathLabInfo.setLabTest(json.get("labTest").toString());
				count = count + 1;
			}

			List<PathLabInfo> pathList = pathLabDao.searchLabInfo(pathLabInfo,
					count);

			if (pathList.size() > 0) {
				jsonArray = new Gson().toJson(pathList);
				obj.put(new String("ListOfPathLabs"), jsonArray);
			}

			else {
				obj.put(new String("ListOfPathLab"), "No Result Found");
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
