package com.sapienza.premierleague.controller;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sapienza.premierleague.model.StatisticsDTO;
import com.sapienza.premierleague.service.StatisticsService;

@RestController
@RequestMapping("liverpoolstats")
public class StatisticsController {

	@Autowired
	private StatisticsService statisticsService;

	@GetMapping("")
	public ResponseEntity<List<StatisticsDTO>> getAllGoalscorers()
			throws IOException, ParseException, java.text.ParseException {
		return ResponseEntity.ok(statisticsService.getLiverpoolStatistics());
	}
}