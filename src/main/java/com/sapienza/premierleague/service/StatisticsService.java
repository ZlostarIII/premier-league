package com.sapienza.premierleague.service;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;

import com.sapienza.premierleague.model.MatchOutcome;
import com.sapienza.premierleague.model.StatisticsDTO;

@Service
public class StatisticsService {

	private RestTemplate restTemplate;

	@Value("${app.token.name}")
	private String tokenName;

	@Value("${app.token.value}")
	private String tokenValue;

	@Value("${app.team.name}")
	private String teamName;

	public List<StatisticsDTO> getLiverpoolStatistics() throws ParseException, java.text.ParseException {
		HttpHeaders headers = new HttpHeaders();
		headers.set(tokenName, tokenValue);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		restTemplate = new RestTemplate();
		HttpEntity<String> requestJson = restTemplate.exchange("https://api.football-data.org/v2/teams/64/matches",
				HttpMethod.GET, entity, String.class);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(requestJson.getBody());
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray matches = (JSONArray) jsonObject.get("matches");

		List<StatisticsDTO> matchesList = new ArrayList<>();

		for (int i = 0; i < matches.size(); i++) {
			boolean hasWon = false;
			boolean hasDrawn = false;
			boolean notPlayed = false;
			boolean isHost = false;
			String opponent = "";

			Object matchesFields = parser.parse(matches.get(i).toString());

			JSONObject matchesFieldsObject = (JSONObject) matchesFields;
			Object scores = matchesFieldsObject.get("score");
			Object homeTeamNames = matchesFieldsObject.get("homeTeam");
			Object awayTeamNames = matchesFieldsObject.get("awayTeam");
			Object competitions = matchesFieldsObject.get("competition");

			JSONObject scoreFieldsObject = (JSONObject) scores;

			String winner = scoreFieldsObject.get("winner") != null ? (String) scoreFieldsObject.get("winner") : "";

			Object fullTimeScore = scoreFieldsObject.get("fullTime");
			JSONObject teamScoreObject = (JSONObject) fullTimeScore;

			long homeTeamScore = teamScoreObject.get("homeTeam") != null ? (long) teamScoreObject.get("homeTeam") : 0;
			long awayTeamScore = teamScoreObject.get("awayTeam") != null ? (long) teamScoreObject.get("awayTeam") : 0;

			JSONObject homeTeamNameObject = (JSONObject) homeTeamNames;
			String homeTeamName = (String) homeTeamNameObject.get("name");

			JSONObject awayTeamNameObject = (JSONObject) awayTeamNames;
			String awayTeamName = (String) awayTeamNameObject.get("name");

			if (homeTeamName.equals(teamName)) {
				if (winner.equals("HOME_TEAM")) {
					hasWon = true;
				} else if (winner.equals("DRAW")) {
					hasDrawn = true;
				} else if (winner.equals("")) {
					notPlayed = true;
				}
				opponent = awayTeamName;
				isHost = true;
			} else {
				if (winner.equals("AWAY_TEAM")) {
					hasWon = true;
				} else if (winner.equals("DRAW")) {
					hasDrawn = true;
				} else if (winner.equals("")) {
					notPlayed = true;
				}
				opponent = homeTeamName;
			}

			JSONObject competitionObject = (JSONObject) competitions;
			String competitionName = (String) competitionObject.get("name");

			long actualMatchDay = matchesFieldsObject.get("matchday") != null
					? (long) matchesFieldsObject.get("matchday")
					: 0;

			Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date matchDate = (Date) ((DateFormat) formatter).parse((String) matchesFieldsObject.get("utcDate"));

			matchesList.add(
					StatisticsDTO.builder().opponent(opponent).competition(competitionName).matchday(actualMatchDay)
							.matchDate(matchDate).homeTeamScore(homeTeamScore).awayTeamScore(awayTeamScore)
							.outcome(notPlayed ? MatchOutcome.NOT_PLAYED_YET
									: (hasWon ? MatchOutcome.WIN : (hasDrawn ? MatchOutcome.DRAW : MatchOutcome.LOSS)))
							.host(isHost).build());
		}

		return matchesList;
	}
}
