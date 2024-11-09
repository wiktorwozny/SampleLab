import React from 'react';

interface TitleProps {
    message: string;
}

const Title: React.FC<TitleProps> = ({message}) => {
    return (
        <h1 className="text-left font-bold text-3xl w-full my-3">{message}</h1>
    );
};

export default Title;