package com.example.onlinejudge.service;

import com.example.onlinejudge.dto.*;
import com.example.onlinejudge.model.*;
import com.example.onlinejudge.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionService {
    
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final ContestRepository contestRepository;
    private final JudgeService judgeService;
    
    public SubmissionDto submitCode(SubmissionRequestDto request) {
        // Validate problem and contest exist
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        
        Contest contest = contestRepository.findById(request.getContestId())
                .orElseThrow(() -> new RuntimeException("Contest not found"));
        
        // Create submission
        Submission submission = new Submission();
        submission.setCode(request.getCode());
        submission.setLanguage(request.getLanguage());
        submission.setProblem(problem);
        submission.setContest(contest);
        submission.setUsername(request.getUsername());
        submission.setStatus(Submission.SubmissionStatus.PENDING);
        
        Submission savedSubmission = submissionRepository.save(submission);
        
        // Queue for judging (asynchronous)
        judgeService.queueSubmission(savedSubmission.getId());
        
        return convertToSubmissionDto(savedSubmission);
    }
    
    public SubmissionDto getSubmissionById(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        
        return convertToSubmissionDto(submission);
    }
    
    public List<SubmissionDto> getSubmissionsByContest(Long contestId) {
        List<Submission> submissions = submissionRepository.findByContestId(contestId);
        return submissions.stream()
                .map(this::convertToSubmissionDto)
                .collect(Collectors.toList());
    }
    
    public List<SubmissionDto> getSubmissionsByUser(String username, Long contestId) {
        List<Submission> submissions = submissionRepository.findByContestIdAndUsername(contestId, username);
        return submissions.stream()
                .map(this::convertToSubmissionDto)
                .collect(Collectors.toList());
    }
    
    private SubmissionDto convertToSubmissionDto(Submission submission) {
        return new SubmissionDto(
                submission.getId(),
                submission.getCode(),
                submission.getLanguage(),
                submission.getStatus(),
                submission.getResult(),
                submission.getExecutionTime(),
                submission.getMemoryUsed(),
                submission.getErrorMessage(),
                submission.getSubmittedAt(),
                submission.getProblem().getId(),
                submission.getContest().getId(),
                submission.getUsername()
        );
    }
}
