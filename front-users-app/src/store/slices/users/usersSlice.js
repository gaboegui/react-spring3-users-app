import { createSlice } from '@reduxjs/toolkit'
import { act } from 'react';

export const initialUserFormValues = { id: 0, username: "", password: "", email: "", admin: false };

const initialErrors = { username: '', password: '', email: '',};

export const usersSlice = createSlice({
    
    name: 'users',
    
      // similar a useUsers.js
    initialState: {
        users: [],
        paginator: {},
        userSelected: initialUserFormValues,
        visibleForm: false,
        errors: initialErrors,
        isLoading: true,
    },
    // similar a usersReducer.js, en vez de opciones de case, son funciones
    reducers: {
        addUser: (state, action) => {
            state.users = [ 
                ...state.users,
                {
                    ...action.payload,
                }
            ];
            state.userSelected = initialUserFormValues;
            state.visibleForm = false;

        },

        deleteUser: (state, action) => {
            state.users = state.users.filter(user => user.id !== action.payload);

        },

        updateUser: (state, action) => {
            state.users = state.users.map ( u =>{
                if (u.id === action.payload.id){

                    return {
                        ...action.payload   // clono el objeto debido a que reducer maneja obj inmutables 
                   } 
                }
                return u;
            });
            state.userSelected = initialUserFormValues;
            state.visibleForm = false;
        },

        loadingUsers: (state, action) => {            
            //with pagination the users come inside JSON content
            //state.users = action.payload; 
            state.users = action.payload.content;
            state.paginator = action.payload;
            state.isLoading = false;
        },

        onUserSelectedInForm: (state, action) => {
            state.userSelected = action.payload;
            state.visibleForm = true;
        },

        onOpenForm: (state) => {
            state.visibleForm = true;
        },

        onCloseForm: (state) => {
            state.visibleForm = false;
            state.userSelected = initialUserFormValues;
        },

        fillError: (state, action) => {
            state.errors = action.payload;
        }

    }
});

export const {
    addUser,
    deleteUser,
    updateUser,
    loadingUsers,
    onUserSelectedInForm,
    onOpenForm,
    onCloseForm,
    fillError,
} = usersSlice.actions;