package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController
{
	@Autowired
	private ParticipantService participantService;

	@GetMapping("")
	public ResponseEntity<?> getParticipants(@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortingBy,
													 @RequestParam(value = "sortOrder", required = false, defaultValue = "") String sortingOrder,
													 @RequestParam(value = "key", required = false, defaultValue = "") String key) {
		Optional<Collection<Participant>> participants = Optional.ofNullable(participantService.getAll(sortingBy, sortingOrder, key));
		if(participants.isEmpty())
			return new ResponseEntity<>(NOT_FOUND);
		else
			return new ResponseEntity<>(participants.get(), OK);
	}

	@GetMapping("/{login}")
	public ResponseEntity<?> getParticipant(@PathVariable("login") String login) {
		Optional<Participant> participant = Optional.ofNullable(participantService.findById(login));
		if(participant.isEmpty())
			return new ResponseEntity<>("A participant was not fetched! The participant with login: " +
					login + " does not exist!", NOT_FOUND);
		else
			return new ResponseEntity<>(participant.get(), OK);

	}

	@PostMapping("")
	public ResponseEntity<?> addParticipant(@RequestBody Participant participant) {
		Optional<Participant> existingParticipant = Optional.ofNullable(participantService.findById(participant.getLogin()));
		if(existingParticipant.isEmpty()) {
			participantService.addParticipant(participant);
			return new ResponseEntity<>(participant, CREATED);
		} else
			return new ResponseEntity<>("A participant was not posted! The participant with login: " +
					participant.getLogin() + " already exists!", CONFLICT);
	}

	@PutMapping("/{login}")
	public ResponseEntity<?> updateParticipant(@PathVariable String login,
											   @RequestBody Participant participant) {
		Optional<Participant> existingParticipant = Optional.ofNullable(participantService.findById(login));
		if(existingParticipant.isEmpty())
			return new ResponseEntity<>("A participant was not put! The participant with login: " +
					login + " does not exist!", NOT_FOUND);
		else {
			existingParticipant.get().setPassword(participant.getPassword());
			participantService.updateParticipant(existingParticipant.get());
			return new ResponseEntity<>(existingParticipant.get(), OK);
		}
	}

	@DeleteMapping("/{login}")
	public ResponseEntity<?> deleteParticipant(@PathVariable String login) {
		Optional<Participant> existingParticipant = Optional.ofNullable(participantService.findById(login));
		if(existingParticipant.isEmpty())
			return new ResponseEntity<>("A participant was not deleted! The participant with login: " +
					login + " does not exist!", NOT_FOUND);
		else {
			participantService.deleteParticipant(existingParticipant.get());
			return new ResponseEntity<>(existingParticipant, OK);
		}
	}
}
