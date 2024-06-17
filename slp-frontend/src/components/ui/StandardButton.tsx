export const StandardButton = ({className = "", type, children, ...props}: React.HTMLAttributes<HTMLButtonElement> & {
    type: "button" | "submit" | "reset"
}) => {
    return (<button
        className={`p-2 bg-sky-500 rounded self-center text-white hover:bg-sky-600 ${className}`}
        type={type}
        {...props}>
        {children}
    </button>)
}

export const DisableButton = ({
                                  className = "",
                                  disabled,
                                  type,
                                  children,
                                  ...props
                              }: React.HTMLAttributes<HTMLButtonElement> & { type: "button" | "submit" | "reset" } & {
    disabled: boolean
}) => {
    return (<button
        className={disabled ? `p-2 bg-gray-300 rounded self-center text-gray-500 ${className}` : `p-2 bg-sky-500 rounded self-center text-white hover:bg-sky-600 ${className}`}
        disabled={disabled}
        type={type}
        {...props}>
        {children}
    </button>)
}

export const CancelButton = ({className = "", type, children, ...props}: React.HTMLAttributes<HTMLButtonElement> & {
    type: "button" | "submit" | "reset"
}) => {
    return (<button
        className={`p-2 bg-white rounded self-center text-sky-500 border-1 border-sky-500 hover:bg-blue-100 ${className}`}
        type={type}
        {...props}>
        {children}
    </button>)
}
