import React from "react";
import { createRoot } from "react-dom/client";
import { ApolloClient, ApolloProvider, InMemoryCache, HttpLink, ApolloLink } from "@apollo/client";
import { onError } from "@apollo/client/link/error";
import { NextUIProvider } from "@nextui-org/react";
import "./styles/output.css";
import "./styles/global.css";
import App from "./app";

const errorLink = onError(({ graphQLErrors, networkError }) => {
  if (graphQLErrors) {
    graphQLErrors.forEach(({ message, locations, path }) => {
      if (message === "Unauthorized") {
        window.location.href = "/login";
      }
    });
  }

  if (networkError) {
    window.location.href = "/login";
  }
});

const authLink = new ApolloLink((operation, forward) => {
  const token = localStorage.getItem("jwt");

  operation.setContext(({ headers = {} }) => ({
    headers: {
      ...headers,
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
  }));

  return forward(operation);
});

const httpLink = new HttpLink({
  uri: `/ansh/api/graphql`,
  credentials: "include",
});

const client = new ApolloClient({
  link: ApolloLink.from([authLink, errorLink, httpLink]),
  cache: new InMemoryCache(),
});

const rootElement = document.getElementById("root");
if (!rootElement) {
  throw new Error("Root element not found");
}

const root = createRoot(rootElement);
root.render(
  <React.StrictMode>
    <ApolloProvider client={client}>
      <NextUIProvider>
        <App />
      </NextUIProvider>
    </ApolloProvider>
  </React.StrictMode>
);
