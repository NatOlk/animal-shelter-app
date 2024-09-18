import React, { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import {ApolloClient, ApolloProvider, InMemoryCache} from "@apollo/client";
window.React = React
import App from "./app/app";


const client = new ApolloClient({
    uri: 'http://localhost:8080/graphql',
    cache: new InMemoryCache(),
});

const root = createRoot(document.getElementById("root"));
root.render(
    <ApolloProvider client={client}>
        <App />
    </ApolloProvider>
);