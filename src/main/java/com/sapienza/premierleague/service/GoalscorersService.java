package com.sapienza.premierleague.service;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sapienza.premierleague.model.GoalscorerDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoalscorersService {

	private RestTemplate restTemplate;

	public List<GoalscorerDTO> getGoalscorers() throws IOException, ParseException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Auth-Token", "905778d1ed2446a191c18750f4691bd3");
		HttpEntity<?> entity = new HttpEntity<>(headers);

		restTemplate = new RestTemplate();
		HttpEntity<String> requestJson = restTemplate.exchange(
				"https://api.football-data.org/v2/competitions/PL/scorers", HttpMethod.GET, entity, String.class);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(requestJson.getBody());
		JSONArray array = (JSONArray) obj;
		List<GoalscorerDTO> goalscorersList = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			goalscorersList.add(GoalscorerDTO.builder().name(array.get(17).toString())
					.goals(Integer.valueOf(array.get(28).toString())).build());
		}

		return goalscorersList;
	}
}
