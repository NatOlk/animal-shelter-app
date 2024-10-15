import React, { createContext, useContext, useEffect, useState } from 'react';
import { createRoot } from "react-dom/client";
import { ApolloClient, ApolloProvider, InMemoryCache, HttpLink, ApolloLink } from "@apollo/client";
import { onError } from "@apollo/client/link/error";
import 'materialize-css/dist/css/materialize.min.css';
import 'materialize-css/dist/js/materialize.min.js';

import App from "./app";

const apiUrl = process.env.REACT_APP_API_URL;

const CookieContext = createContext();

export const CookieProvider = ({ children }) => {
  const [sessionId, setSessionId] = useState(getCookie('JSESSIONID'));

  useEffect(() => {
    const updateSessionId = () => {
      const newSessionId = getCookie('JSESSIONID');
      if (newSessionId !== sessionId) {
        setSessionId(newSessionId);
      }
    };

    window.addEventListener('cookieChange', updateSessionId);

    return () => {
      window.removeEventListener('cookieChange', updateSessionId);
    };
  }, [sessionId]);

  return (
    <CookieContext.Provider value={{ sessionId }}>
      {children}
    </CookieContext.Provider>
  );
};

export const useCookie = () => useContext(CookieContext);

const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
};

const errorLink = onError(({ graphQLErrors, networkError }) => {
  if (graphQLErrors) {
    graphQLErrors.forEach(({ message, locations, path }) => {
      console.error(
        `[GraphQL error]: Message: ${message}, Location: ${locations}, Path: ${path}`
      );

      if (message === 'Unauthorized') {
        window.location.href = '/login';
      }
    });
  }

  if (networkError) {
    console.error(`[Network error]: ${networkError}`);
  }
});

const httpLink = new HttpLink({
  uri: `${apiUrl}/graphql`,
  credentials: 'include',
});

const client = new ApolloClient({
  link: ApolloLink.from([errorLink, httpLink]),
  cache: new InMemoryCache(),
});

const root = createRoot(document.getElementById("root"));
root.render(
  <CookieProvider>
    <ApolloProvider client={client}>
      <App />
    </ApolloProvider>
  </CookieProvider>
);
