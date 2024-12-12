import React from 'react';
import { Pagination } from "@nextui-org/react";

const PaginationComponent = ({ currentPage, pageCount, onPageChange }) => {
  if (pageCount <= 1) return null;

  return (
    <Pagination
      total={pageCount}
      page={currentPage + 1}
      onChange={(page) => onPageChange(page - 1)}
      showControls
      loop
      size="md"
      showShadow
    />
  );
};

export default PaginationComponent;
