import { createSlice } from "@reduxjs/toolkit";


const initialLogin = JSON.parse(sessionStorage.getItem("login")) || {
    isAuth: false,
    isAdmin: false,
    user: undefined,
    isLoginLoading: false,
  };

/**
 * New function to manage all Login based on REDUX
 */
export const authSlice = createSlice({
    name: 'auth',
    initialState: initialLogin,
    reducers: {

        onLogin: (state, action) => {
            state.isAuth = true;
            state.isAdmin = action.payload.isAdmin;
            state.user =  action.payload.user;
            state.isLoginLoading = false;
        },

        onLogout: (state) => {
            state.isAuth = false;
            state.isAdmin = false;
            state.user =  undefined;
            state.isLoginLoading = false;
        },

        onStartLogin: (state) => {
            state.isLoginLoading = true;
        }
    }
});

export const { onLogin, onLogout, onStartLogin } = authSlice.actions;