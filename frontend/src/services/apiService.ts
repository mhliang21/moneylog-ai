import { AssetPosition, PortfolioData, HistoryRecord } from '../types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const getPortfolio = async (date: string): Promise<PortfolioData> => {
  const response = await fetch(`${API_BASE_URL}/portfolios/${date}`);
  if (!response.ok) {
    throw new Error('Failed to fetch portfolio');
  }
  const data = await response.json();
  return {
    date: data.date,
    positions: data.positions || [],
    aiSummary: data.aiSummary,
  };
};

export const savePortfolio = async (portfolio: PortfolioData): Promise<PortfolioData> => {
  const response = await fetch(`${API_BASE_URL}/portfolios`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      date: portfolio.date,
      positions: portfolio.positions,
      aiSummary: portfolio.aiSummary,
    }),
  });
  if (!response.ok) {
    throw new Error('Failed to save portfolio');
  }
  const data = await response.json();
  return {
    date: data.date,
    positions: data.positions || [],
    aiSummary: data.aiSummary,
  };
};

export const addPosition = async (date: string, position: AssetPosition): Promise<AssetPosition> => {
  const response = await fetch(`${API_BASE_URL}/portfolios/${date}/positions`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      name: position.name,
      category: position.category,
      amount: position.amount,
      monthlyGain: position.monthlyGain,
      totalGain: position.totalGain,
    }),
  });
  if (!response.ok) {
    throw new Error('Failed to add position');
  }
  const data = await response.json();
  return {
    id: data.id?.toString() || Date.now().toString(),
    name: data.name,
    category: data.category,
    amount: data.amount,
    monthlyGain: data.monthlyGain,
    totalGain: data.totalGain,
  };
};

export const updatePosition = async (
  date: string,
  positionId: string,
  position: AssetPosition
): Promise<AssetPosition> => {
  const response = await fetch(`${API_BASE_URL}/portfolios/${date}/positions/${positionId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      name: position.name,
      category: position.category,
      amount: position.amount,
      monthlyGain: position.monthlyGain,
      totalGain: position.totalGain,
    }),
  });
  if (!response.ok) {
    throw new Error('Failed to update position');
  }
  const data = await response.json();
  return {
    id: data.id?.toString() || positionId,
    name: data.name,
    category: data.category,
    amount: data.amount,
    monthlyGain: data.monthlyGain,
    totalGain: data.totalGain,
  };
};

export const deletePosition = async (date: string, positionId: string): Promise<void> => {
  const response = await fetch(`${API_BASE_URL}/portfolios/${date}/positions/${positionId}`, {
    method: 'DELETE',
  });
  if (!response.ok) {
    throw new Error('Failed to delete position');
  }
};

export const generateAiSummary = async (date: string): Promise<string> => {
  const response = await fetch(`${API_BASE_URL}/portfolios/${date}/ai-summary`, {
    method: 'POST',
  });
  if (!response.ok) {
    throw new Error('Failed to generate AI summary');
  }
  return await response.text();
};

export const getHistory = async (): Promise<HistoryRecord[]> => {
  const response = await fetch(`${API_BASE_URL}/portfolios/history`);
  if (!response.ok) {
    throw new Error('Failed to fetch history');
  }
  const data = await response.json();
  return data.map((record: any) => ({
    month: record.month,
    totalAssets: record.totalAssets,
    totalGain: record.totalGain,
  }));
};


