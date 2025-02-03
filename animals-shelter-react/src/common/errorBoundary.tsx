import React from 'react';
import { Card, CardHeader, CardBody } from "@nextui-org/react";

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError() {
    return { hasError: true };
  }

  componentDidCatch(error, errorInfo) {
    console.error("Caught an error:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="flex items-start justify-center min-h-screen bg-gray-100 p-4">
          <Card className="max-w-[340px] shadow-lg bg-white p-6 rounded-lg mt-10">
            <CardHeader className="justify-between">
              Something went wrong!
            </CardHeader>
            <CardBody>
              <p>Please try to reload the page.</p>
            </CardBody>
          </Card>
        </div>
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
