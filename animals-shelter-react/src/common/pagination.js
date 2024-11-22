import React from 'react';


const Pagination = ({ currentPage, pageCount, onPageChange }) => {
    if (pageCount <= 1) return null;

    return (
        <div className="pagination-container">
            <ul className="pagination">
                <li className={currentPage === 0 ? "disabled" : "waves-effect"}>
                    <a
                        href="#!"
                        onClick={() => {
                            if (currentPage > 0) onPageChange(currentPage - 1);
                        }}>
                        <i className="material-icons">chevron_left</i>
                    </a>
                </li>
                {Array.from({ length: pageCount }, (_, index) => (
                    <li
                        key={index}
                        className={currentPage === index ? "active" : "waves-effect"}>
                        <a href="#!" onClick={() => onPageChange(index)}>
                            {index + 1}
                        </a>
                    </li>
                ))}
                <li
                    className={
                        currentPage === pageCount - 1 ? "disabled" : "waves-effect"
                    }>
                    <a
                        href="#!"
                        onClick={() => {
                            if (currentPage < pageCount - 1) onPageChange(currentPage + 1);
                        }}>
                        <i className="material-icons">chevron_right</i>
                    </a>
                </li>
            </ul>
        </div>
    );
};

export default Pagination;