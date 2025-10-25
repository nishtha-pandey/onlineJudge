package com.example.onlinejudge.controller;

import com.example.onlinejudge.dto.*;
import com.example.onlinejudge.service.ContestService;
import com.example.onlinejudge.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContestController {
    
    private final ContestService contestService;
    private final SubmissionService submissionService;
    
    @GetMapping("/contests/{contestId}")
    public ResponseEntity<ContestDto> getContest(@PathVariable Long contestId) {
        try {
            ContestDto contest = contestService.getContestById(contestId);
            return ResponseEntity.ok(contest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/submissions")
    public ResponseEntity<SubmissionDto> submitCode(@RequestBody SubmissionRequestDto request) {
        try {
            SubmissionDto submission = submissionService.submitCode(request);
            return ResponseEntity.ok(submission);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/submissions/{submissionId}")
    public ResponseEntity<SubmissionDto> getSubmission(@PathVariable Long submissionId) {
        try {
            SubmissionDto submission = submissionService.getSubmissionById(submissionId);
            return ResponseEntity.ok(submission);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/contests/{contestId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboard(@PathVariable Long contestId) {
        try {
            List<LeaderboardEntryDto> leaderboard = contestService.getLeaderboard(contestId);
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/contests/{contestId}/submissions")
    public ResponseEntity<List<SubmissionDto>> getContestSubmissions(@PathVariable Long contestId) {
        try {
            List<SubmissionDto> submissions = submissionService.getSubmissionsByContest(contestId);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/contests/{contestId}/submissions/{username}")
    public ResponseEntity<List<SubmissionDto>> getUserSubmissions(
            @PathVariable Long contestId, 
            @PathVariable String username) {
        try {
            List<SubmissionDto> submissions = submissionService.getSubmissionsByUser(username, contestId);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
