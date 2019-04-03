package com.sapienza.premierleague.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sapienza.premierleague.model.GoalscorerDTO;
import com.sapienza.premierleague.service.GoalscorersService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("plgoalscorers")
public class GoalscorersController {

	@Autowired
	private GoalscorersService goalscorersService;

	@GetMapping("")
	public ResponseEntity<List<GoalscorerDTO>> getAllGoalscorers() throws IOException, ParseException {
		return ResponseEntity.ok(goalscorersService.getGoalscorers());
	}
}
