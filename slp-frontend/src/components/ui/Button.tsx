export const Button=({ className="", type, children, ...props}:React.HTMLAttributes<HTMLButtonElement>&{type:"button" | "submit" | "reset"}) => {
    return(<button 
    className={`p-2 bg-sky-500 rounded self-center text-white hover:bg-sky-600 ${className}`}
    type={type}
    {...props}>
        {children}
    </button>)
}