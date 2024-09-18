import React from 'react';
import {createRoot} from "react-dom/client";


function ShowErrors({error}) {
    const errorDiv = createRoot(document.getElementById("error"));

    errorDiv.render(

           <div>
              <pre>
                  {
                  error.graphQLErrors.map(({ message, locations, path, extensions }, i) => (
                     <span key={i}>
                         {message}
                         {extensions && ` - More details: ${extensions.exception}`}
                     </span>
                 ))
                 }
               </pre>
        </div>
    );
}

export default ShowErrors;
