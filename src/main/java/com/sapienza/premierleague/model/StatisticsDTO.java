package com.sapienza.premierleague.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StatisticsDTO {

	private String opponent;
	private String competition;
	private long matchday;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private Date matchDate;
	private long homeTeamScore;
	private long awayTeamScore;
	private MatchOutcome outcome;
	private boolean host;

}
