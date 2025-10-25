'use client';

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import { contestApi, Contest, Problem, Submission, LeaderboardEntry } from '@/lib/api';
import toast from 'react-hot-toast';
import MonacoEditor from '@monaco-editor/react';

export default function ContestPage() {
  const params = useParams();
  const contestId = parseInt(params.contestId as string);
  
  const [contest, setContest] = useState<Contest | null>(null);
  const [selectedProblem, setSelectedProblem] = useState<Problem | null>(null);
  const [code, setCode] = useState('');
  const [language, setLanguage] = useState('java');

  const codeTemplates = {
    java: `public class Solution {
    public static void main(String[] args) {
        // Your code here
        System.out.println("Hello World");
    }
}`,
    python: `# Your code here
print("Hello World")`,
    cpp: `#include <iostream>
using namespace std;

int main() {
    // Your code here
    cout << "Hello World" << endl;
    return 0;
}`
  };
  const [submissions, setSubmissions] = useState<Submission[]>([]);
  const [leaderboard, setLeaderboard] = useState<LeaderboardEntry[]>([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [username, setUsername] = useState('');

  useEffect(() => {
    const storedUsername = localStorage.getItem('username');
    if (storedUsername) {
      setUsername(storedUsername);
    }
    
    loadContest();
    loadLeaderboard();
  }, [contestId]);

  useEffect(() => {
    const interval = setInterval(() => {
      loadLeaderboard();
    }, 15000); // Update every 15 seconds

    return () => clearInterval(interval);
  }, [contestId]);

  const loadContest = async () => {
    try {
      const contestData = await contestApi.getContest(contestId);
      setContest(contestData);
      if (contestData.problems.length > 0) {
        setSelectedProblem(contestData.problems[0]);
        loadUserSubmissions(contestData.problems[0].id);
      }
    } catch (error) {
      toast.error('Failed to load contest');
    } finally {
      setLoading(false);
    }
  };

  const loadLeaderboard = async () => {
    try {
      const leaderboardData = await contestApi.getLeaderboard(contestId);
      setLeaderboard(leaderboardData);
    } catch (error) {
      console.error('Failed to load leaderboard:', error);
    }
  };

  const loadUserSubmissions = async (problemId: number) => {
    try {
      const submissionsData = await contestApi.getUserSubmissions(contestId, username);
      const problemSubmissions = submissionsData.filter(s => s.problemId === problemId);
      setSubmissions(problemSubmissions);
    } catch (error) {
      console.error('Failed to load submissions:', error);
    }
  };

  const handleProblemSelect = (problem: Problem) => {
    setSelectedProblem(problem);
    loadUserSubmissions(problem.id);
    // Set default code template
    setCode(codeTemplates[language as keyof typeof codeTemplates]);
  };

  const handleLanguageChange = (newLanguage: string) => {
    setLanguage(newLanguage);
    setCode(codeTemplates[newLanguage as keyof typeof codeTemplates]);
  };

  const handleSubmit = async () => {
    if (!selectedProblem || !code.trim()) {
      toast.error('Please select a problem and write some code');
      return;
    }

    setSubmitting(true);
    
    try {
      const submission = await contestApi.submitCode({
        code,
        language,
        problemId: selectedProblem.id,
        contestId,
        username,
      });

      toast.success('Code submitted successfully!');
      
      // Poll for submission status
      pollSubmissionStatus(submission.id);
      
    } catch (error) {
      toast.error('Failed to submit code');
    } finally {
      setSubmitting(false);
    }
  };

  const pollSubmissionStatus = async (submissionId: number) => {
    const pollInterval = setInterval(async () => {
      try {
        const submission = await contestApi.getSubmission(submissionId);
        
        if (submission.status === 'ACCEPTED' || submission.status === 'WRONG_ANSWER' || 
            submission.status === 'TIME_LIMIT_EXCEEDED' || submission.status === 'RUNTIME_ERROR' ||
            submission.status === 'COMPILATION_ERROR') {
          clearInterval(pollInterval);
          
          if (submission.status === 'ACCEPTED') {
            toast.success('Accepted! 🎉');
          } else {
            toast.error(`${submission.status}: ${submission.result}`);
          }
          
          // Reload submissions and leaderboard
          loadUserSubmissions(selectedProblem!.id);
          loadLeaderboard();
        }
      } catch (error) {
        console.error('Failed to poll submission status:', error);
        clearInterval(pollInterval);
      }
    }, 2000); // Poll every 2 seconds
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'ACCEPTED': return 'text-green-600 bg-green-100';
      case 'WRONG_ANSWER': return 'text-red-600 bg-red-100';
      case 'TIME_LIMIT_EXCEEDED': return 'text-yellow-600 bg-yellow-100';
      case 'RUNTIME_ERROR': return 'text-red-600 bg-red-100';
      case 'COMPILATION_ERROR': return 'text-red-600 bg-red-100';
      case 'RUNNING': return 'text-blue-600 bg-blue-100';
      case 'PENDING': return 'text-gray-600 bg-gray-100';
      default: return 'text-gray-600 bg-gray-100';
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading contest...</p>
        </div>
      </div>
    );
  }

  if (!contest) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <h1 className="text-2xl font-bold text-gray-900 mb-4">Contest not found</h1>
          <p className="text-gray-600">Please check the contest ID and try again.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">{contest.name}</h1>
              <p className="text-gray-600">Welcome, {username}</p>
            </div>
            <div className="text-sm text-gray-500">
              Contest ID: {contestId}
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Problems List */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow">
              <div className="p-6">
                <h2 className="text-lg font-semibold text-gray-900 mb-4">Problems</h2>
                <div className="space-y-2">
                  {contest.problems.map((problem) => (
                    <button
                      key={problem.id}
                      onClick={() => handleProblemSelect(problem)}
                      className={`w-full text-left p-3 rounded-lg border transition-colors ${
                        selectedProblem?.id === problem.id
                          ? 'border-blue-500 bg-blue-50'
                          : 'border-gray-200 hover:border-gray-300'
                      }`}
                    >
                      <div className="font-medium text-gray-900">{problem.title}</div>
                      <div className="text-sm text-gray-500">
                        Difficulty: {problem.difficulty}/5
                      </div>
                    </button>
                  ))}
                </div>
              </div>
            </div>

            {/* Leaderboard */}
            <div className="bg-white rounded-lg shadow mt-6">
              <div className="p-6">
                <h2 className="text-lg font-semibold text-gray-900 mb-4">Leaderboard</h2>
                <div className="space-y-2">
                  {leaderboard.slice(0, 10).map((entry, index) => (
                    <div key={entry.username} className="flex justify-between items-center p-2 rounded bg-gray-50">
                      <div className="flex items-center">
                        <span className="text-sm font-medium text-gray-600 w-6">
                          {index + 1}
                        </span>
                        <span className="text-sm font-medium text-gray-900 ml-2">
                          {entry.username}
                        </span>
                      </div>
                      <div className="text-sm text-gray-600">
                        {entry.solvedProblems} solved
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>

          {/* Main Content */}
          <div className="lg:col-span-2">
            {selectedProblem ? (
              <div className="space-y-6">
                {/* Problem Description */}
                <div className="bg-white rounded-lg shadow">
                  <div className="p-6">
                    <h2 className="text-xl font-semibold text-gray-900 mb-4">
                      {selectedProblem.title}
                    </h2>
                    <div className="prose max-w-none">
                      <p className="text-gray-700 mb-4">{selectedProblem.description}</p>
                      
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                        <div>
                          <h3 className="font-medium text-gray-900 mb-2">Input Format</h3>
                          <p className="text-sm text-gray-600">{selectedProblem.inputFormat}</p>
                        </div>
                        <div>
                          <h3 className="font-medium text-gray-900 mb-2">Output Format</h3>
                          <p className="text-sm text-gray-600">{selectedProblem.outputFormat}</p>
                        </div>
                      </div>

                      <div className="flex space-x-4 text-sm text-gray-600">
                        <span>Time Limit: {selectedProblem.timeLimit}s</span>
                        <span>Memory Limit: {selectedProblem.memoryLimit}MB</span>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Code Editor */}
                <div className="bg-white rounded-lg shadow">
                  <div className="p-6">
                    <div className="flex justify-between items-center mb-4">
                      <h2 className="text-lg font-semibold text-gray-900">Code Editor</h2>
                      <div className="flex items-center space-x-4">
                        <select
                          value={language}
                          onChange={(e) => handleLanguageChange(e.target.value)}
                          className="px-3 py-1 border border-gray-300 rounded-md text-sm"
                        >
                          <option value="java">Java</option>
                          <option value="python">Python</option>
                          <option value="cpp">C++</option>
                        </select>
                        <button
                          onClick={handleSubmit}
                          disabled={submitting}
                          className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        >
                          {submitting ? 'Submitting...' : 'Submit'}
                        </button>
                      </div>
                    </div>
                    
                    <div className="border border-gray-300 rounded-lg overflow-hidden">
                      <MonacoEditor
                        height="400px"
                        language={language}
                        value={code}
                        onChange={(value) => setCode(value || '')}
                        theme="vs-light"
                        options={{
                          minimap: { enabled: false },
                          fontSize: 14,
                          lineNumbers: 'on',
                          roundedSelection: false,
                          scrollBeyondLastLine: false,
                          automaticLayout: true,
                        }}
                      />
                    </div>
                  </div>
                </div>

                {/* Submissions */}
                <div className="bg-white rounded-lg shadow">
                  <div className="p-6">
                    <h2 className="text-lg font-semibold text-gray-900 mb-4">Your Submissions</h2>
                    <div className="space-y-2">
                      {submissions.length === 0 ? (
                        <p className="text-gray-500 text-center py-4">No submissions yet</p>
                      ) : (
                        submissions.map((submission) => (
                          <div key={submission.id} className="flex justify-between items-center p-3 border border-gray-200 rounded-lg">
                            <div className="flex items-center space-x-4">
                              <span className="text-sm text-gray-600">
                                {new Date(submission.submittedAt).toLocaleTimeString()}
                              </span>
                              <span className="text-sm text-gray-600">
                                {submission.language.toUpperCase()}
                              </span>
                            </div>
                            <span className={`px-2 py-1 rounded text-xs font-medium ${getStatusColor(submission.status)}`}>
                              {submission.status}
                            </span>
                          </div>
                        ))
                      )}
                    </div>
                  </div>
                </div>
              </div>
            ) : (
              <div className="bg-white rounded-lg shadow p-6 text-center">
                <p className="text-gray-500">Select a problem to start coding</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}