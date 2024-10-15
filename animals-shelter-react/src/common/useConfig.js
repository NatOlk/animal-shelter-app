import React, { useState, useEffect } from 'react';
import { apiFetch } from './api';

const useConfig = () => {
  const [config, setConfig] = useState(null);

  useEffect(() => {
    const fetchConfig = async () => {
      try {
        const data = await apiFetch('/api/config');
        setConfig(data);
      } catch (error) {
        console.error('Error fetching config data:', error);
      }
    };

    if (!config) {
      fetchConfig();
    }
  }, [config]);

  return config;
};

export default useConfig;
