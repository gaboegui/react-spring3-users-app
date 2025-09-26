import axios from "axios";

export const authService = async({username, password}) => {
    try {
            // login to backend        
            return await axios.post (`${import.meta.env.VITE_API_BASE_URL}/login`, {
                username,
                password } );
    } catch (error) {
        throw error;
    }
}