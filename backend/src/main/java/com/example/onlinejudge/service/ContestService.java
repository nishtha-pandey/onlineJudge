package com.example.onlinejudge.service;

import com.example.onlinejudge.dto.*;
import com.example.onlinejudge.model.*;
import com.example.onlinejudge.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContestService {
    
    private final ContestRepository contestRepository;
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;
    private final TestCaseRepository testCaseRepository;
    
    public ContestDto getContestById(Long contestId) {
        Contest contest = contestRepository.findByIdWithProblems(contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found"));
        
        return convertToContestDto(contest);
    }
    
    public List<LeaderboardEntryDto> getLeaderboard(Long contestId) {
        List<Submission> acceptedSubmissions = submissionRepository.findAcceptedSubmissionsByContest(contestId);
        
        Map<String, LeaderboardEntryDto> leaderboardMap = new HashMap<>();
        
        for (Submission submission : acceptedSubmissions) {
            String username = submission.getUsername();
            LeaderboardEntryDto entry = leaderboardMap.computeIfAbsent(username, 
                k -> new LeaderboardEntryDto(username, 0, 0, 0, 0));
            
            entry.setAcceptedSubmissions(entry.getAcceptedSubmissions() + 1);
            
            // Calculate time penalty (simplified)
            Duration duration = Duration.between(submission.getSubmittedAt(), LocalDateTime.now());
            entry.setTotalTime(entry.getTotalTime() + duration.toMinutes());
        }
        
        // Count total submissions per user
        List<Submission> allSubmissions = submissionRepository.findByContestId(contestId);
        Map<String, Long> totalSubmissionsMap = allSubmissions.stream()
                .collect(Collectors.groupingBy(Submission::getUsername, Collectors.counting()));
        
        for (LeaderboardEntryDto entry : leaderboardMap.values()) {
            entry.setTotalSubmissions(totalSubmissionsMap.getOrDefault(entry.getUsername(), 0L).intValue());
            entry.setSolvedProblems(entry.getAcceptedSubmissions()); // Simplified: one problem per accepted submission
        }
        
        return leaderboardMap.values().stream()
                .sorted((a, b) -> {
                    if (a.getSolvedProblems() != b.getSolvedProblems()) {
                        return Integer.compare(b.getSolvedProblems(), a.getSolvedProblems());
                    }
                    return Long.compare(a.getTotalTime(), b.getTotalTime());
                })
                .collect(Collectors.toList());
    }
    
    private ContestDto convertToContestDto(Contest contest) {
        List<ProblemDto> problemDtos = contest.getProblems().stream()
                .map(this::convertToProblemDto)
                .collect(Collectors.toList());
        
        return new ContestDto(
                contest.getId(),
                contest.getName(),
                contest.getDescription(),
                contest.getStartTime(),
                contest.getEndTime(),
                contest.isActive(),
                problemDtos
        );
    }
    
    private ProblemDto convertToProblemDto(Problem problem) {
        List<TestCaseDto> testCaseDtos = problem.getTestCases().stream()
                .map(tc -> new TestCaseDto(tc.getId(), tc.getInput(), tc.getExpectedOutput(), tc.isHidden()))
                .collect(Collectors.toList());
        
        return new ProblemDto(
                problem.getId(),
                problem.getTitle(),
                problem.getDescription(),
                problem.getInputFormat(),
                problem.getOutputFormat(),
                problem.getTimeLimit(),
                problem.getMemoryLimit(),
                problem.getDifficulty(),
                testCaseDtos
        );
    }
}
