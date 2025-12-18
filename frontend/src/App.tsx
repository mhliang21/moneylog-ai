import React, { useState, useEffect } from 'react';
import { AssetPosition } from './types';
import AssetForm from './components/AssetForm';
import ReportView from './components/ReportView';
import HistoryView from './components/HistoryView';
import { 
  getPortfolio, 
  savePortfolio, 
  getHistory,
  generateAiSummary
} from './services/apiService';
import { LayoutDashboard, History, PenTool, Wallet } from 'lucide-react';

function App() {
  const [activeTab, setActiveTab] = useState<'edit' | 'view' | 'history'>('view');
  const [date, setDate] = useState<string>(new Date().toISOString().split('T')[0].substring(0, 7)); 
  const [positions, setPositions] = useState<AssetPosition[]>([]);
  const [aiSummary, setAiSummary] = useState<string>('');
  const [isGeneratingAi, setIsGeneratingAi] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const loadPortfolio = async () => {
      setIsLoading(true);
      try {
        const portfolio = await getPortfolio(date);
        setPositions(portfolio.positions || []);
        setAiSummary(portfolio.aiSummary || '');
      } catch (error) {
        console.error('Failed to load portfolio:', error);
        setPositions([]);
        setAiSummary('');
      } finally {
        setIsLoading(false);
      }
    };
    loadPortfolio();
  }, [date]);

  const handleDateChange = async (newDate: string) => {
    if (positions.length > 0) {
      try {
        await savePortfolio({
          date,
          positions,
          aiSummary,
        });
      } catch (error) {
        console.error('Failed to save portfolio:', error);
      }
    }
    setDate(newDate);
  };

  const handleSavePortfolio = async () => {
    try {
      const savedPortfolio = await savePortfolio({
        date,
        positions,
        aiSummary,
      });
      // Update state with saved data
      setPositions(savedPortfolio.positions);
      setAiSummary(savedPortfolio.aiSummary || '');
    } catch (error) {
      console.error('Failed to save portfolio:', error);
      alert('保存失败，请重试');
    }
  };

  const handleUpdatePositions = async (newPositions: AssetPosition[]) => {
    setPositions(newPositions);
    try {
      const savedPortfolio = await savePortfolio({
        date,
        positions: newPositions,
        aiSummary,
      });
      // Update with saved data
      setPositions(savedPortfolio.positions);
      setAiSummary(savedPortfolio.aiSummary || '');
    } catch (error) {
      console.error('Failed to auto-save:', error);
    }
  };

  const handleGenerateSummary = async () => {
    setIsGeneratingAi(true);
    try {
      const summary = await generateAiSummary(date);
      setAiSummary(summary);
      const savedPortfolio = await savePortfolio({
        date,
        positions,
        aiSummary: summary,
      });
      // Update with saved data
      setPositions(savedPortfolio.positions);
      setAiSummary(savedPortfolio.aiSummary || '');
    } catch (error) {
      console.error('Failed to generate summary:', error);
      alert('生成 AI 摘要失败，请重试');
    } finally {
      setIsGeneratingAi(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 pb-20 font-sans selection:bg-piggy-200">
      <nav className="bg-white/80 backdrop-blur-md shadow-sm sticky top-0 z-50 border-b border-gray-100">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center gap-2">
              <div className="bg-gradient-to-br from-piggy-400 to-piggy-600 text-white p-2 rounded-xl shadow-lg shadow-piggy-200">
                <Wallet size={20} />
              </div>
              <span className="font-bold text-xl text-gray-700 tracking-tight">小猪理财</span>
            </div>
            <div className="flex items-center gap-1 md:gap-2 bg-gray-100/50 p-1 rounded-full border border-gray-200/50">
              <button
                onClick={() => setActiveTab('edit')}
                className={`flex items-center gap-1.5 px-4 py-1.5 rounded-full text-sm font-medium transition-all duration-300 ${
                  activeTab === 'edit' 
                    ? 'bg-white text-piggy-600 shadow-sm ring-1 ring-black/5' 
                    : 'text-gray-500 hover:text-gray-700 hover:bg-gray-200/50'
                }`}
              >
                <PenTool size={14} /> <span className="hidden sm:inline">记账</span>
              </button>
              <button
                onClick={() => setActiveTab('view')}
                className={`flex items-center gap-1.5 px-4 py-1.5 rounded-full text-sm font-medium transition-all duration-300 ${
                  activeTab === 'view' 
                    ? 'bg-white text-piggy-600 shadow-sm ring-1 ring-black/5' 
                    : 'text-gray-500 hover:text-gray-700 hover:bg-gray-200/50'
                }`}
              >
                <LayoutDashboard size={14} /> <span className="hidden sm:inline">月报</span>
              </button>
              <button
                onClick={() => setActiveTab('history')}
                className={`flex items-center gap-1.5 px-4 py-1.5 rounded-full text-sm font-medium transition-all duration-300 ${
                  activeTab === 'history' 
                    ? 'bg-white text-piggy-600 shadow-sm ring-1 ring-black/5' 
                    : 'text-gray-500 hover:text-gray-700 hover:bg-gray-200/50'
                }`}
              >
                <History size={14} /> <span className="hidden sm:inline">历史</span>
              </button>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="transition-all duration-500 ease-in-out">
          {activeTab === 'edit' && (
            <div className="max-w-4xl mx-auto animate-fade-in-up">
              {isLoading ? (
                <div className="text-center py-8 text-gray-500">加载中...</div>
              ) : (
                <AssetForm 
                  positions={positions} 
                  onUpdate={handleUpdatePositions} 
                  date={date}
                  onDateChange={handleDateChange}
                />
              )}
            </div>
          )}
          
          {activeTab === 'view' && (
            <div className="animate-fade-in-up">
              {isLoading ? (
                <div className="text-center py-8 text-gray-500">加载中...</div>
              ) : (
                <ReportView 
                  date={date} 
                  positions={positions} 
                  aiSummary={aiSummary}
                  onGenerateSummary={handleGenerateSummary}
                  isGeneratingAi={isGeneratingAi}
                />
              )}
            </div>
          )}

          {activeTab === 'history' && (
            <div className="animate-fade-in-up">
              <HistoryView />
            </div>
          )}
        </div>
      </main>
      
      <style>{`
        @keyframes fade-in-up {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .animate-fade-in-up {
            animation: fade-in-up 0.5s ease-out forwards;
        }
      `}</style>
    </div>
  );
}

export default App;