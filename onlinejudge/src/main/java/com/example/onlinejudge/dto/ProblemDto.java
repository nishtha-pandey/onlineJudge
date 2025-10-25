package com.example.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDto {
    private Long id;
    private String title;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Integer difficulty;
    private List<TestCaseDto> testCases;
}
