package com.example.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntryDto {
    private String username;
    private int solvedProblems;
    private int totalSubmissions;
    private int acceptedSubmissions;
    private long totalTime; // in minutes
}
