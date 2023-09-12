package com.myplanet.userservice.controller;

import com.myplanet.userservice.domain.Challenge;
import com.myplanet.userservice.domain.ChallengeRequest;
import com.myplanet.userservice.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenge")
@CrossOrigin
public class ChallengeController {

    @Autowired
    ChallengeService service;

    @GetMapping("{id}")
    public ResponseEntity<Challenge> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(service.getChallengeById(id));
    }

    @GetMapping("/organization")
    public ResponseEntity<?> getChallengesAtOrganization(@RequestParam(name = "organizationName") String organizationName) {
        return ResponseEntity.ok(service.getChallengesAtOrganizationLevel(organizationName));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Challenge>> getAll(@RequestParam(name = "isOrganizationLevel") Boolean isOrganizationLevel) {
        return ResponseEntity.ok(service.getAllChallenges(isOrganizationLevel));
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestParam(name = "isOrganizationLevel") Boolean isOrganizationLevel,@RequestBody ChallengeRequest challengeRequest) {
        try {
            return ResponseEntity.ok(service.saveChallenge(isOrganizationLevel,challengeRequest));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Challenge challenge) {
        try {
            return ResponseEntity.ok(service.updateChallenge(challenge));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
        try {
            return ResponseEntity.ok(service.deleteChallenge(id));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @PutMapping("/join")
    public ResponseEntity<?> join(@RequestBody List<Long> challengeId) {
        try {
            service.joinChallenge(challengeId);
            return ResponseEntity.ok("Success");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Nope");
        }
    }

    @PutMapping("/unjoin")
    public ResponseEntity<?> unjoin(@RequestParam(name = "challengeId") Long challengeId, @RequestParam(name = "username") String username) {
        try {
            service.unjoinChallenge(challengeId, username);
            return ResponseEntity.ok("Successs");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Nope");
        }
    }

    @GetMapping("/ongoing")
    public ResponseEntity<?> getOngoingChallengesForUser(@RequestParam(name = "isOrganizationLevel") Boolean isOrganizationLevel) {
        try {
            return ResponseEntity.ok(service.getOngoingChallengesForUser(isOrganizationLevel));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("There was a problem");
        }
    }

    @GetMapping("/completed")
    public ResponseEntity<?> getCompletedChallengesForUser(@RequestParam(name = "username") String username) {
        try {
            return ResponseEntity.ok(service.getCompletedChallengesForUser(username));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("There was a problem");
        }
    }

    @PutMapping("/complete")
    public ResponseEntity<?> completeChallenge(@RequestParam(name = "challengeID") Long id) {
        service.completeChallenge(id);
        return ResponseEntity.ok("OK");
    }
}
