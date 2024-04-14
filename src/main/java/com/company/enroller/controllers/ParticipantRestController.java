package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@GetMapping("/{login}")
	public ResponseEntity<?> displayParticipant(@PathVariable String login)
	{
		Participant participant = Optional.ofNullable(participantService.findById(login))
				.orElseGet(() ->
						new ResponseEntity<Participant>(HttpStatus.NOT_FOUND).getBody());

		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@DeleteMapping("/{login}")
	public ResponseEntity<?> deleteParticipant(@PathVariable String login)
	{
		Participant checkParticipant = participantService.findById(login);
		if(checkParticipant == null)
		{
			return new ResponseEntity<>("Unable to delete. A participant with login" +
					login + " does not exist", HttpStatus.CONFLICT);
		}

		participantService.deleteParticipant(checkParticipant);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{login}")
	public ResponseEntity<?> updateParticipant(@PathVariable String login,
											   @RequestBody Participant participant)
	{
		Participant checkParticipant = participantService.findById(login);
		if(checkParticipant == null)
		{
			return new ResponseEntity<>("Unable to update. A participant with login" +
					login + "does not exist", CONFLICT);
		}

		checkParticipant.setPassword(participant.getPassword());
		checkParticipant.setLogin(participant.getLogin());

		participantService.updateParticipant(checkParticipant);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<?> addParticipant(@RequestBody Participant participant)
	{
		Participant checkParticipant = participantService.findById(participant.getLogin());

		if(checkParticipant != null)
		{
			return new ResponseEntity<>("Unable to create. A participant with login" +
					participant.getLogin() + " already exists", HttpStatus.CONFLICT);
		}

		participantService.addParticipant(participant);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
