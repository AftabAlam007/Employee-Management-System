import React, { useState } from 'react';
import { executeAICommand } from '../../services/aiService';

const AIChatbot = ({ onActionSuccess }) => {
  const [prompt, setPrompt] = useState('');
  const [isSending, setIsSending] = useState(false);
  const [messages, setMessages] = useState([]);

  const handleSend = async () => {
    const text = prompt.trim();
    if (!text || isSending) {
      return;
    }

    setIsSending(true);
    setMessages((prev) => [...prev, { role: 'user', content: text }]);
    setPrompt('');

    try {
      const response = await executeAICommand(text);
      setMessages((prev) => [
        ...prev,
        {
          role: 'assistant',
          content: JSON.stringify(response, null, 2),
        },
      ]);
      if (onActionSuccess) {
        onActionSuccess();
      }
    } catch (error) {
      const message =
        error?.response?.data?.error || 'Failed to execute AI command.';
      setMessages((prev) => [
        ...prev,
        {
          role: 'assistant',
          content: JSON.stringify({ error: message }, null, 2),
        },
      ]);
    } finally {
      setIsSending(false);
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-md border border-slate-200 p-5 mt-8">
      <h2 className="text-xl font-bold text-slate-900 mb-4">HR Assistant</h2>
      <div className="h-72 overflow-y-auto bg-slate-50 rounded-lg p-3 border border-slate-200 mb-4 space-y-3">
        {messages.length === 0 ? (
          <p className="text-sm text-slate-500">
            Try: Show all employees, Add employee Rahul salary 50000, Who has highest salary?
          </p>
        ) : (
          messages.map((message, idx) => (
            <div
              key={`${message.role}-${idx}`}
              className={`p-3 rounded-lg ${
                message.role === 'user'
                  ? 'bg-purple-600 text-white ml-10'
                  : 'bg-white border border-slate-200 mr-10'
              }`}
            >
              <pre className="whitespace-pre-wrap break-words text-sm font-mono">
                {message.content}
              </pre>
            </div>
          ))
        )}
      </div>

      <div className="flex gap-3">
        <input
          type="text"
          value={prompt}
          onChange={(e) => setPrompt(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              handleSend();
            }
          }}
          placeholder="Type a command..."
          className="flex-1 border border-slate-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-purple-400"
        />
        <button
          type="button"
          disabled={isSending}
          onClick={handleSend}
          className="bg-purple-600 text-white px-5 py-2 rounded-lg font-semibold hover:bg-purple-700 disabled:opacity-60"
        >
          {isSending ? 'Sending...' : 'Send'}
        </button>
      </div>
    </div>
  );
};

export default AIChatbot;
