const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export interface AssetPosition {
  id?: string;
  name: string;
  category: string;
  amount: number;
  monthlyGain: number;
  totalGain: number;
}

export interface Portfolio {
  positions: AssetPosition[];
  aiSummary?: string;
  date: string;
}

export interface HistoryRecord {
  month: string;
  totalAssets: number;
  totalGain: number;
}

// Fetch portfolio for a specific month
export const getPortfolio = async (date: string): Promise<Portfolio> => {
  try {
    const response = await fetch(`${API_BASE_URL}/historical-assets/${date}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch portfolio: ${response.status} ${response.statusText}`);
    }
    const positionsData = await response.json();
    
    // Ensure positionsData is an array
    const positionsArray = Array.isArray(positionsData) ? positionsData : [];
    
    // Convert backend IDs to strings
    const positions: AssetPosition[] = positionsArray.map((p: any) => ({
      id: p.id?.toString() || Date.now().toString() + Math.random().toString(),
      name: p.assetName || p.name || '',
      category: p.category || 'Cash',
      amount: p.amount || 0,
      monthlyGain: p.monthlyGain || 0,
      totalGain: p.totalGain || 0
    }));
    
    return {
      positions,
      date
    };
  } catch (error) {
    console.error('API Error fetching portfolio:', error);
    return { positions: [], date };
  }
};

// Save entire portfolio for a month is no longer supported
export const savePortfolio = async (portfolio: Portfolio): Promise<Portfolio> => {
  try {
    // Save each position individually
    const savedPositions = [];
    for (const position of portfolio.positions) {
      const savedPosition = await addPosition(portfolio.date, position);
      savedPositions.push(savedPosition);
    }
    
    return {
      date: portfolio.date,
      positions: savedPositions,
      aiSummary: portfolio.aiSummary || ''
    };
  } catch (error) {
    console.error('API Error saving portfolio:', error);
    throw error;
  }
};

// Add a new position to a month
export const addPosition = async (
  date: string,
  position: Omit<AssetPosition, 'id'>
): Promise<AssetPosition> => {
  try {
    const response = await fetch(`${API_BASE_URL}/historical-assets/${date}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        assetName: position.name,
        category: position.category,
        amount: position.amount,
        monthlyGain: position.monthlyGain,
        totalGain: position.totalGain,
      }),
    });
    
    if (!response.ok) {
      throw new Error(`Failed to add position: ${response.status} ${response.statusText}`);
    }
    
    const data = await response.json();
    return {
      id: data.id?.toString() || Date.now().toString(),
      name: data.assetName || data.name,
      category: data.category,
      amount: data.amount,
      monthlyGain: data.monthlyGain,
      totalGain: data.totalGain,
    };
  } catch (error) {
    console.error('API Error adding position:', error);
    throw error;
  }
};

export const updatePosition = async (
  date: string,
  positionId: string,
  position: AssetPosition
): Promise<AssetPosition> => {
  try {
    const response = await fetch(`${API_BASE_URL}/historical-assets/${date}/${positionId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        assetName: position.name,
        category: position.category,
        amount: position.amount,
        monthlyGain: position.monthlyGain,
        totalGain: position.totalGain,
      }),
    });
    
    if (!response.ok) {
      throw new Error(`Failed to update position: ${response.status} ${response.statusText}`);
    }
    
    const data = await response.json();
    return {
      id: data.id?.toString() || positionId,
      name: data.assetName || data.name,
      category: data.category,
      amount: data.amount,
      monthlyGain: data.monthlyGain,
      totalGain: data.totalGain,
    };
  } catch (error) {
    console.error('API Error updating position:', error);
    throw error;
  }
};

export const deletePosition = async (date: string, positionId: string): Promise<void> => {
  try {
    const response = await fetch(`${API_BASE_URL}/historical-assets/${date}/${positionId}`, {
      method: 'DELETE',
    });
    
    if (!response.ok) {
      throw new Error(`Failed to delete position: ${response.status} ${response.statusText}`);
    }
  } catch (error) {
    console.error('API Error deleting position:', error);
    throw error;
  }
};

// Get history records
export const getHistory = async (): Promise<HistoryRecord[]> => {
  try {
    const response = await fetch(`${API_BASE_URL}/historical-assets/history`);
    if (!response.ok) {
      throw new Error(`Failed to fetch history: ${response.status} ${response.statusText}`);
    }
    const data = await response.json();
    return Array.isArray(data) ? data : [];
  } catch (error) {
    console.error('API Error fetching history:', error);
    return [];
  }
};

// Generate AI Summary placeholder (since we removed the AI service)
export const generateAiSummary = async (date: string): Promise<string> => {
  // Return a placeholder since we've removed the AI service
  return `AI摘要功能已被移除。这是${date}月份的资产报告占位符。`;
};