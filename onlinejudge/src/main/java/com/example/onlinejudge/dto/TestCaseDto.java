package com.example.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseDto {
    private Long id;
    private String input;
    private String expectedOutput;
    private boolean isHidden;
}
