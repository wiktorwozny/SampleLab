import React from 'react';

const Footer: React.FC = () => {
    return (
        <footer className="fixed bottom-0 bg-gray-800 text-white py-4 mt-auto w-full">
            <div className="px-4 text-right">
                <p>&copy; {new Date().getFullYear()} SampleLab. All Rights Reserved.</p>
            </div>
        </footer>
    );
};

export default Footer;
