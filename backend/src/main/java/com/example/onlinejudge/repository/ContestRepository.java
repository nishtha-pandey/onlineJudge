package com.example.onlinejudge.repository;

import com.example.onlinejudge.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    
    List<Contest> findByIsActiveTrue();
    
    @Query("SELECT c FROM Contest c LEFT JOIN FETCH c.problems WHERE c.id = :id")
    Optional<Contest> findByIdWithProblems(@Param("id") Long id);
    
    @Query("SELECT c FROM Contest c LEFT JOIN FETCH c.submissions WHERE c.id = :id")
    Optional<Contest> findByIdWithSubmissions(@Param("id") Long id);
}
