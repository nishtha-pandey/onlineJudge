package com.example.onlinejudge.repository;

import com.example.onlinejudge.model.Submission;
import com.example.onlinejudge.model.Submission.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    
    List<Submission> findByContestId(Long contestId);
    
    List<Submission> findByProblemId(Long problemId);
    
    List<Submission> findByUsername(String username);
    
    List<Submission> findByContestIdAndUsername(Long contestId, String username);
    
    @Query("SELECT s FROM Submission s WHERE s.status IN :statuses")
    List<Submission> findByStatusIn(@Param("statuses") List<SubmissionStatus> statuses);
    
    @Query("SELECT s FROM Submission s WHERE s.contest.id = :contestId AND s.status = 'ACCEPTED' ORDER BY s.submittedAt ASC")
    List<Submission> findAcceptedSubmissionsByContest(@Param("contestId") Long contestId);
    
    @Query("SELECT DISTINCT s.username FROM Submission s WHERE s.contest.id = :contestId AND s.status = 'ACCEPTED'")
    List<String> findUsersWithAcceptedSubmissions(@Param("contestId") Long contestId);
}
