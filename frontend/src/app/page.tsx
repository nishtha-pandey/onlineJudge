'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { contestApi } from '@/lib/api';
import toast from 'react-hot-toast';

export default function JoinPage() {
  const [contestId, setContestId] = useState('');
  const [username, setUsername] = useState('');
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  const handleJoin = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!contestId || !username) {
      toast.error('Please fill in all fields');
      return;
    }

    setLoading(true);
    
    try {
      // Verify contest exists
      await contestApi.getContest(parseInt(contestId));
      
      // Store username in localStorage for the session
      localStorage.setItem('username', username);
      localStorage.setItem('contestId', contestId);
      
      toast.success('Successfully joined contest!');
      router.push(`/contest/${contestId}`);
    } catch (error) {
      toast.error('Contest not found. Please check the Contest ID.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-xl p-8 w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Online Judge</h1>
          <p className="text-gray-600">Join a coding contest</p>
        </div>

        <form onSubmit={handleJoin} className="space-y-6">
          <div>
            <label htmlFor="contestId" className="block text-sm font-medium text-gray-700 mb-2">
              Contest ID
            </label>
            <input
              type="number"
              id="contestId"
              value={contestId}
              onChange={(e) => setContestId(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              placeholder="Enter contest ID"
              required
            />
          </div>

          <div>
            <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-2">
              Username
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              placeholder="Enter your username"
              required
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            {loading ? 'Joining...' : 'Join Contest'}
          </button>
        </form>

        <div className="mt-6 text-center">
          <p className="text-sm text-gray-500">
            Sample Contest ID: <span className="font-mono bg-gray-100 px-2 py-1 rounded">1</span>
          </p>
        </div>
      </div>
    </div>
  );
}