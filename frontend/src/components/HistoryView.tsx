import React, { useEffect, useState } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, BarChart, Bar, Cell } from 'recharts';
import { HistoryRecord } from '../types';
import { getHistory } from '../services/apiService';

const HistoryView: React.FC = () => {
  const [historyData, setHistoryData] = useState<HistoryRecord[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchHistory = async () => {
      try {
        setLoading(true);
        const data = await getHistory();
        setHistoryData(data);
        setError(null);
      } catch (err) {
        console.error('Failed to fetch history:', err);
        setError('获取历史数据失败');
      } finally {
        setLoading(false);
      }
    };

    fetchHistory();
  }, []);

  if (loading) {
    return (
      <div className="max-w-[800px] mx-auto bg-white rounded-[32px] shadow-xl border border-white ring-1 ring-gray-100 p-8">
        <div className="text-center py-8 text-gray-500">加载中...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-[800px] mx-auto bg-white rounded-[32px] shadow-xl border border-white ring-1 ring-gray-100 p-8">
        <div className="text-center py-8 text-red-500">{error}</div>
      </div>
    );
  }

  return (
    <div className="max-w-[800px] mx-auto bg-white rounded-[32px] shadow-xl border border-white ring-1 ring-gray-100 p-8">
      <div className="flex items-center gap-3 mb-10">
        <div className="w-1.5 h-8 bg-piggy-500 rounded-full shadow-lg shadow-piggy-200"></div>
        <h2 className="text-2xl font-bold text-gray-800 tracking-tight">历史资产走势</h2>
      </div>

      <div className="mb-12">
        <h3 className="text-sm font-bold text-gray-500 mb-6 flex items-center gap-2 uppercase tracking-wider">
          <span className="bg-piggy-100 p-1 rounded text-piggy-500">📈</span> 总资产变化 (近{historyData.length}个月)
        </h3>
        <div className="h-[300px] w-full bg-gradient-to-b from-piggy-50/50 to-white rounded-2xl p-4 border border-piggy-50">
          <ResponsiveContainer width="100%" height="100%">
            <AreaChart data={historyData} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
              <defs>
                <linearGradient id="colorAssets" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#f43f78" stopOpacity={0.2}/>
                  <stop offset="95%" stopColor="#f43f78" stopOpacity={0}/>
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f0f0f0" />
              <XAxis 
                dataKey="month" 
                tick={{fontSize: 10, fill: '#9ca3af'}} 
                tickFormatter={(val) => {
                  const parts = val.split('-');
                  return `${parts[0].slice(2)}/${parts[1]}`;
                }}
                axisLine={false}
                tickLine={false}
                dy={10}
              />
              <YAxis 
                tick={{fontSize: 10, fill: '#9ca3af'}} 
                axisLine={false}
                tickLine={false}
                tickFormatter={(val) => `${(val/10000).toFixed(0)}w`}
                dx={-10}
              />
              <Tooltip 
                contentStyle={{borderRadius: '16px', border: 'none', boxShadow: '0 10px 15px -3px rgb(0 0 0 / 0.1)', padding: '12px'}}
                formatter={(val: number) => [`¥${val.toLocaleString()}`, '总资产']}
                labelFormatter={(label) => `日期: ${label}`}
              />
              <Area 
                type="monotone" 
                dataKey="totalAssets" 
                stroke="#f43f78" 
                strokeWidth={3}
                fillOpacity={1} 
                fill="url(#colorAssets)" 
              />
            </AreaChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div>
        <h3 className="text-sm font-bold text-gray-500 mb-6 flex items-center gap-2 uppercase tracking-wider">
          <span className="bg-piggy-100 p-1 rounded text-piggy-500">📊</span> 月度收益波动
        </h3>
        <div className="h-[250px] w-full bg-white rounded-2xl p-4 border border-gray-100 shadow-inner">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={historyData} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
              <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f0f0f0" />
              <XAxis 
                dataKey="month" 
                tick={{fontSize: 10, fill: '#9ca3af'}} 
                tickFormatter={(val) => {
                  const parts = val.split('-');
                  return `${parts[1]}月`;
                }}
                axisLine={false}
                tickLine={false}
                dy={10}
              />
              <YAxis 
                tick={{fontSize: 10, fill: '#9ca3af'}} 
                axisLine={false}
                tickLine={false}
                tickFormatter={(val) => `${(val/1000).toFixed(0)}k`}
                dx={-10}
              />
              <Tooltip 
                cursor={{fill: '#fdf2f8'}}
                contentStyle={{borderRadius: '12px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)'}}
                formatter={(val: number) => [`¥${val.toLocaleString()}`, '当月收益']}
              />
              <Bar dataKey="totalGain" radius={[4, 4, 0, 0]}>
                {historyData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={entry.totalGain >= 0 ? '#fb719a' : '#4ade80'} />
                ))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default HistoryView;