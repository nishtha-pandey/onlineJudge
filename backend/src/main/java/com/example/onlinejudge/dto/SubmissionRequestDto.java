package com.example.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionRequestDto {
    @NotBlank(message = "Code is required")
    private String code;
    
    @NotBlank(message = "Language is required")
    private String language;
    
    @NotNull(message = "Problem ID is required")
    private Long problemId;
    
    @NotNull(message = "Contest ID is required")
    private Long contestId;
    
    @NotBlank(message = "Username is required")
    private String username;
}
