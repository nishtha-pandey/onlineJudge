import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export interface Contest {
  id: number;
  name: string;
  description: string;
  startTime: string;
  endTime: string;
  isActive: boolean;
  problems: Problem[];
}

export interface Problem {
  id: number;
  title: string;
  description: string;
  inputFormat: string;
  outputFormat: string;
  timeLimit: number;
  memoryLimit: number;
  difficulty: number;
  testCases: TestCase[];
}

export interface TestCase {
  id: number;
  input: string;
  expectedOutput: string;
  isHidden: boolean;
}

export interface Submission {
  id: number;
  code: string;
  language: string;
  status: 'PENDING' | 'RUNNING' | 'ACCEPTED' | 'WRONG_ANSWER' | 'TIME_LIMIT_EXCEEDED' | 'MEMORY_LIMIT_EXCEEDED' | 'RUNTIME_ERROR' | 'COMPILATION_ERROR';
  result: string;
  executionTime: number;
  memoryUsed: number;
  errorMessage: string;
  submittedAt: string;
  problemId: number;
  contestId: number;
  username: string;
}

export interface SubmissionRequest {
  code: string;
  language: string;
  problemId: number;
  contestId: number;
  username: string;
}

export interface LeaderboardEntry {
  username: string;
  solvedProblems: number;
  totalSubmissions: number;
  acceptedSubmissions: number;
  totalTime: number;
}

export const contestApi = {
  getContest: (contestId: number): Promise<Contest> =>
    api.get(`/contests/${contestId}`).then(res => res.data),
  
  getLeaderboard: (contestId: number): Promise<LeaderboardEntry[]> =>
    api.get(`/contests/${contestId}/leaderboard`).then(res => res.data),
  
  submitCode: (submission: SubmissionRequest): Promise<Submission> =>
    api.post('/submissions', submission).then(res => res.data),
  
  getSubmission: (submissionId: number): Promise<Submission> =>
    api.get(`/submissions/${submissionId}`).then(res => res.data),
  
  getContestSubmissions: (contestId: number): Promise<Submission[]> =>
    api.get(`/contests/${contestId}/submissions`).then(res => res.data),
  
  getUserSubmissions: (contestId: number, username: string): Promise<Submission[]> =>
    api.get(`/contests/${contestId}/submissions/${username}`).then(res => res.data),
};

export default api;