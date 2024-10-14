import React, { useState, useEffect } from 'react';
import axios from 'axios';

const useConfig = () => {

  axios.defaults.withCredentials = true;
  const [config, setConfig] = useState(null);

  const apiUrl = process.env.REACT_APP_API_URL;

  useEffect(() => {
    if (!config) {
      axios.get(`${apiUrl}/api/config`, {
        withCredentials: true,
        credentials: 'include'
      })
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
