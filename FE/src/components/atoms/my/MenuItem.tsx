import styled from "@emotion/styled";
import { ReactNode } from "react";

interface MenuItemProps{
    children? : ReactNode,
    className?: string,
    disabled?: boolean,
    key?: string,
    onClick?: React.MouseEventHandler<HTMLElement>,
    selected?: boolean,
}
const MenuItem : React.FC<MenuItemProps> = ({
    children,
    className,
    disabled,
    onClick,
    selected,
    ...rest
})=>{
    return (<>
        <Atom
            className={className}
            onClick={onClick}
            {...rest}
        >
            {children}
        </Atom>
    </>);
}
const Atom = styled.li`

`
export default MenuItem;