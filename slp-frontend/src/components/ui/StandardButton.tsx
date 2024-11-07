export const StandardButton = ({className = "", type, children, ...props}: React.HTMLAttributes<HTMLButtonElement> & {
    type: "button" | "submit" | "reset"
}) => {
    return (<button
        className={`py-2 px-3 bg-sky-500 hover:bg-sky-600 rounded self-center font-semibold text-white ${className}`}
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
        className={disabled ? `py-2 px-3 bg-gray-300 rounded self-center text-gray-500 font-semibold ${className}` : `py-2 px-3 bg-sky-500 rounded self-center text-white font-semibold hover:bg-sky-600 ${className}`}
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
        className={`py-2 px-3 bg-white-0 rounded self-center text-sky-500 font-semibold border-1 border-sky-500 hover:bg-sky-100 ${className}`}
        type={type}
        {...props}>
        {children}
    </button>)
}
