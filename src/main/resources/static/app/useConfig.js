import React, { useState, useEffect } from 'react';
import axios from 'axios';

const useConfig = () => {
  const [config, setConfig] = useState(null);

  useEffect(() => {
    if (!config) {
      axios.get('/api/config')
        .then(response => {
          setConfig(response.data);
        })
        .catch(error => {
          console.error('Error fetching config data:', error);
        });
    }
  }, [config]);

  return config;
};

export default useConfig;
