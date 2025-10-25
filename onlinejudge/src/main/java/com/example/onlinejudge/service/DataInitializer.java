package com.example.onlinejudge.service;

import com.example.onlinejudge.model.*;
import com.example.onlinejudge.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final ContestRepository contestRepository;
    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample contest
        Contest contest = new Contest();
        contest.setName("Beginner Coding Contest");
        contest.setDescription("A beginner-friendly coding contest with simple problems");
        contest.setStartTime(LocalDateTime.now().minusHours(1));
        contest.setEndTime(LocalDateTime.now().plusHours(2));
        contest.setActive(true);
        
        Contest savedContest = contestRepository.save(contest);
        
        // Create Problem 1: Hello World
        Problem problem1 = new Problem();
        problem1.setTitle("Hello World");
        problem1.setDescription("Write a program that prints 'Hello World' to the console.");
        problem1.setInputFormat("No input required.");
        problem1.setOutputFormat("Output should be exactly: Hello World");
        problem1.setTimeLimit(2);
        problem1.setMemoryLimit(128);
        problem1.setDifficulty(1);
        problem1.setContest(savedContest);
        
        Problem savedProblem1 = problemRepository.save(problem1);
        
        // Test case for Problem 1
        TestCase testCase1 = new TestCase();
        testCase1.setInput("");
        testCase1.setExpectedOutput("Hello World");
        testCase1.setProblem(savedProblem1);
        testCase1.setHidden(false);
        testCaseRepository.save(testCase1);
        
        // Create Problem 2: Sum of Two Numbers
        Problem problem2 = new Problem();
        problem2.setTitle("Sum of Two Numbers");
        problem2.setDescription("Given two integers, calculate and print their sum.");
        problem2.setInputFormat("Two integers separated by a space.");
        problem2.setOutputFormat("Print the sum of the two numbers.");
        problem2.setTimeLimit(2);
        problem2.setMemoryLimit(128);
        problem2.setDifficulty(1);
        problem2.setContest(savedContest);
        
        Problem savedProblem2 = problemRepository.save(problem2);
        
        // Test cases for Problem 2
        TestCase testCase2a = new TestCase();
        testCase2a.setInput("5 3");
        testCase2a.setExpectedOutput("8");
        testCase2a.setProblem(savedProblem2);
        testCase2a.setHidden(false);
        testCaseRepository.save(testCase2a);
        
        TestCase testCase2b = new TestCase();
        testCase2b.setInput("10 20");
        testCase2b.setExpectedOutput("30");
        testCase2b.setProblem(savedProblem2);
        testCase2b.setHidden(false);
        testCaseRepository.save(testCase2b);
        
        // Create Problem 3: Even or Odd
        Problem problem3 = new Problem();
        problem3.setTitle("Even or Odd");
        problem3.setDescription("Given a number, determine if it's even or odd and print 'Even' or 'Odd'.");
        problem3.setInputFormat("A single integer.");
        problem3.setOutputFormat("Print 'Even' if the number is even, 'Odd' if it's odd.");
        problem3.setTimeLimit(2);
        problem3.setMemoryLimit(128);
        problem3.setDifficulty(2);
        problem3.setContest(savedContest);
        
        Problem savedProblem3 = problemRepository.save(problem3);
        
        // Test cases for Problem 3
        TestCase testCase3a = new TestCase();
        testCase3a.setInput("4");
        testCase3a.setExpectedOutput("Even");
        testCase3a.setProblem(savedProblem3);
        testCase3a.setHidden(false);
        testCaseRepository.save(testCase3a);
        
        TestCase testCase3b = new TestCase();
        testCase3b.setInput("7");
        testCase3b.setExpectedOutput("Odd");
        testCase3b.setProblem(savedProblem3);
        testCase3b.setHidden(false);
        testCaseRepository.save(testCase3b);
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("Contest ID: " + savedContest.getId());
        System.out.println("Problem IDs: " + savedProblem1.getId() + ", " + savedProblem2.getId() + ", " + savedProblem3.getId());
    }
}
