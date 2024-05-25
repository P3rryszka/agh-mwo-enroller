package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    private MeetingService meetingService;

    @GetMapping("")
    public ResponseEntity<?> getMeetings() {
        Optional<Collection<Meeting>> meetings = Optional.ofNullable(meetingService.getAllMeetings());
        if(meetings.isEmpty())
            return new ResponseEntity<>(NOT_FOUND);
        else
            return new ResponseEntity<>(meetings.get(), OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Optional<Meeting> meeting = Optional.ofNullable(meetingService.findById(id));
        if(meeting.isEmpty())
            return new ResponseEntity<>("A meeting was not fetched! The meeting with id: " +
                    id + " does not exist!", NOT_FOUND);
        else
            return new ResponseEntity<>(meeting.get(), OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addMeeting(@RequestBody Meeting newMeeting) {
        Optional<Meeting> meeting = Optional.ofNullable(meetingService.findById(newMeeting.getId()));
        if(meeting.isEmpty()) {
            meetingService.addMeeting(newMeeting);
            return new ResponseEntity<>(newMeeting, OK);
        } else
            return new ResponseEntity<>("A meeting was not posted! The meeting with id: " +
                    newMeeting + " already exists!", CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
        Optional<Meeting> meeting = Optional.ofNullable(meetingService.findById(id));
        if(meeting.isEmpty())
            return new ResponseEntity<>("A meeting was not deleted! The meeting with id: " +
                    id + "does not exist!", NOT_FOUND);
        else {
            meetingService.deleteMeeting(meeting.get());
            return new ResponseEntity<>(meeting.get(), OK);
        }
    }
}
