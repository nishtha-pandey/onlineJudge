package com.example.onlinejudge.service;

import com.example.onlinejudge.model.*;
import com.example.onlinejudge.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class JudgeService {
    
    private final SubmissionRepository submissionRepository;
    private final TestCaseRepository testCaseRepository;
    
    @Async
    public CompletableFuture<Void> queueSubmission(Long submissionId) {
        try {
            judgeSubmission(submissionId);
        } catch (Exception e) {
            log.error("Error judging submission {}: {}", submissionId, e.getMessage());
            updateSubmissionStatus(submissionId, Submission.SubmissionStatus.RUNTIME_ERROR, 
                    "Judge error: " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }
    
    private void judgeSubmission(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        
        updateSubmissionStatus(submissionId, Submission.SubmissionStatus.RUNNING, null);
        
        try {
            List<TestCase> testCases = testCaseRepository.findByProblemId(submission.getProblem().getId());
            
            for (TestCase testCase : testCases) {
                JudgeResult result = executeCode(submission, testCase);
                
                if (!result.isAccepted()) {
                    updateSubmissionStatus(submissionId, result.getStatus(), result.getErrorMessage());
                    return;
                }
            }
            
            // All test cases passed
            updateSubmissionStatus(submissionId, Submission.SubmissionStatus.ACCEPTED, "Accepted");
            
        } catch (Exception e) {
            log.error("Error during code execution: {}", e.getMessage());
            updateSubmissionStatus(submissionId, Submission.SubmissionStatus.RUNTIME_ERROR, 
                    "Execution error: " + e.getMessage());
        }
    }
    
    private JudgeResult executeCode(Submission submission, TestCase testCase) {
        try {
            // Create temporary files
            Path tempDir = Files.createTempDirectory("judge_" + submission.getId());
            Path codeFile = tempDir.resolve(getFileName(submission.getLanguage()));
            Path inputFile = tempDir.resolve("input.txt");
            Path outputFile = tempDir.resolve("output.txt");
            
            // Write code and input
            Files.write(codeFile, submission.getCode().getBytes());
            Files.write(inputFile, testCase.getInput().getBytes());
            
            // Build Docker command
            String dockerCommand = buildDockerCommand(submission, tempDir, codeFile, inputFile, outputFile);
            
            // Execute Docker command
            ProcessBuilder pb = new ProcessBuilder("docker", "run", "--rm", 
                    "--memory=" + submission.getProblem().getMemoryLimit() + "m",
                    "--cpus=1",
                    "--network=none",
                    "-v", tempDir.toString() + ":/workspace",
                    "onlinejudge-judge:latest",
                    "sh", "-c", dockerCommand);
            
            Process process = pb.start();
            
            // Set timeout
            boolean finished = process.waitFor(submission.getProblem().getTimeLimit(), 
                    java.util.concurrent.TimeUnit.SECONDS);
            
            if (!finished) {
                process.destroyForcibly();
                return new JudgeResult(Submission.SubmissionStatus.TIME_LIMIT_EXCEEDED, 
                        "Time limit exceeded", false);
            }
            
            int exitCode = process.exitValue();
            
            if (exitCode != 0) {
                return new JudgeResult(Submission.SubmissionStatus.RUNTIME_ERROR, 
                        "Runtime error (exit code: " + exitCode + ")", false);
            }
            
            // Read output and compare
            String actualOutput = Files.readString(outputFile).trim();
            String expectedOutput = testCase.getExpectedOutput().trim();
            
            if (actualOutput.equals(expectedOutput)) {
                return new JudgeResult(Submission.SubmissionStatus.ACCEPTED, "Accepted", true);
            } else {
                return new JudgeResult(Submission.SubmissionStatus.WRONG_ANSWER, 
                        "Wrong Answer", false);
            }
            
        } catch (Exception e) {
            log.error("Error executing code: {}", e.getMessage());
            return new JudgeResult(Submission.SubmissionStatus.RUNTIME_ERROR, 
                    "Execution error: " + e.getMessage(), false);
        }
    }
    
    private String buildDockerCommand(Submission submission, Path tempDir, 
                                    Path codeFile, Path inputFile, Path outputFile) {
        String language = submission.getLanguage().toLowerCase();
        
        switch (language) {
            case "java":
                return String.format("cd /workspace && javac %s && java %s < %s > %s", 
                        codeFile.getFileName(), 
                        codeFile.getFileName().toString().replace(".java", ""),
                        inputFile.getFileName(), 
                        outputFile.getFileName());
            case "python":
                return String.format("cd /workspace && python %s < %s > %s", 
                        codeFile.getFileName(), inputFile.getFileName(), outputFile.getFileName());
            case "cpp":
                return String.format("cd /workspace && g++ -o main %s && ./main < %s > %s", 
                        codeFile.getFileName(), inputFile.getFileName(), outputFile.getFileName());
            default:
                throw new RuntimeException("Unsupported language: " + language);
        }
    }
    
    private String getFileName(String language) {
        switch (language.toLowerCase()) {
            case "java":
                return "Solution.java";
            case "python":
                return "solution.py";
            case "cpp":
                return "solution.cpp";
            default:
                throw new RuntimeException("Unsupported language: " + language);
        }
    }
    
    @Transactional
    private void updateSubmissionStatus(Long submissionId, Submission.SubmissionStatus status, String result) {
        Submission submission = submissionRepository.findById(submissionId).orElse(null);
        if (submission != null) {
            submission.setStatus(status);
            submission.setResult(result);
            submissionRepository.save(submission);
        }
    }
    
    private static class JudgeResult {
        private final Submission.SubmissionStatus status;
        private final String errorMessage;
        private final boolean accepted;
        
        public JudgeResult(Submission.SubmissionStatus status, String errorMessage, boolean accepted) {
            this.status = status;
            this.errorMessage = errorMessage;
            this.accepted = accepted;
        }
        
        public Submission.SubmissionStatus getStatus() { return status; }
        public String getErrorMessage() { return errorMessage; }
        public boolean isAccepted() { return accepted; }
    }
}
