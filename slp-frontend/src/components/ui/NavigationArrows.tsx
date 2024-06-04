import React from 'react';

import arrowIcon from '../../assets/play.png';

interface NavigationArrowsProps {
    onPrevious: () => void;
    onNext: () => void;
}

const NavigationArrows: React.FC<NavigationArrowsProps> = ({onPrevious, onNext}) => {
    return (
        <div className="absolute flex justify-between w-full">
            <button
                className="flex justify-center items-center bg-blue-500 text-white rounded-md w-10 h-10 hover:bg-blue-300 m-1"
                onClick={onPrevious}
            >
                <img className="rotate-180 invert" src={arrowIcon}/>
            </button>
            <button
                className="flex justify-center items-center bg-blue-500 text-white rounded-md w-10 h-10 hover:bg-blue-300 m-1"
                onClick={onNext}
            >
                <img className="invert" src={arrowIcon}/>
            </button>
        </div>
    );
};

export default NavigationArrows;