package com.example.onlinejudge.dto;

import com.example.onlinejudge.model.Submission.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDto {
    private Long id;
    private String code;
    private String language;
    private SubmissionStatus status;
    private String result;
    private Integer executionTime;
    private Integer memoryUsed;
    private String errorMessage;
    private LocalDateTime submittedAt;
    private Long problemId;
    private Long contestId;
    private String username;
}
