package com.sapienza.premierleague.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${app.token.name}")
	private String tokenName;

	@Value("${app.token.value}")
	private String tokenValue;

	public List<GoalscorerDTO> getGoalscorers() throws IOException, ParseException {
		HttpHeaders headers = new HttpHeaders();
		headers.set(tokenName, tokenValue);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		restTemplate = new RestTemplate();
		HttpEntity<String> requestJson = restTemplate.exchange(
				"https://api.football-data.org/v2/competitions/PL/scorers", HttpMethod.GET, entity, String.class);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(requestJson.getBody());
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray array = (JSONArray) jsonObject.get("scorers");
		List<GoalscorerDTO> goalscorersList = new ArrayList<>();

		for (int i = 0; i < array.size(); i++) {
			Object fields = parser.parse(array.get(i).toString());
			JSONObject jsonObject2 = (JSONObject) fields;

			Object player = jsonObject2.get("player");
			JSONObject jsonObject3 = (JSONObject) player;
			String playerName = (String) jsonObject3.get("name");

			Object team = jsonObject2.get("team");
			JSONObject jsonObject4 = (JSONObject) team;
			String teamName = (String) jsonObject4.get("name");

			long numberOfGoals = (long) jsonObject2.get("numberOfGoals");
			goalscorersList
					.add(GoalscorerDTO.builder().name(playerName).teamName(teamName).goals(numberOfGoals).build());
		}

		return goalscorersList;
	}
}
