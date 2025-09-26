import { useNavigate } from "react-router-dom";
import { deleteUsersWs, getUsersbyPagesWs, saveUsersWs, updateUsersWs } from "../services/userService";

import Swal from "sweetalert2";
import { useDispatch, useSelector } from "react-redux";
import { addUser, deleteUser, updateUser, loadingUsers, 
  onUserSelectedInForm, onOpenForm, onCloseForm, fillError, initialUserFormValues } from "../store/slices/users/usersSlice";
import { useAuth } from "../auth/hooks/useAuth";

/**
 * Hook to manage functions for User
 */
export const useUsers = () => {

  // to control if has admin priviligies and logout on 401
  const { login, handlerLogout } = useAuth();

  // OLD: useReducer declare variables and functions for the CRUD of users
  // const [users, dispatch] = useReducer(usersReducer, initialUsers);
  // const [userSelected, setUserSelected] = useState(initialUserFormValues);
  // const [visibleForm, setVisibleForm] = useState(false);
  
  // NEW: alternativa para Redux
  const dispatch = useDispatch();
  // Se obtiene las variables directamente del STORE
  const {users, userSelected, visibleForm, errors, isLoading, paginator } = useSelector(state => state.users);

  const navigate = useNavigate();  

  // Functions to manage forms
  const handlerUserSelectedInForm = (user) => {
    dispatch(onUserSelectedInForm({...user}));
  };

  const handlerOpenForm = () => {
    dispatch(onOpenForm());
  };

  const handlerCloseForm = () => {
    dispatch(onCloseForm());
    dispatch(fillError({}));
  };

  // Funtions to interact with Web Services
  const getusers = async (page = 0) => {
    try {

      // consult data in WS
      //const result = await getUsersWs();
      const result = await getUsersbyPagesWs(page);


      // store in the usersSlice.JS
      dispatch(loadingUsers( result.data));
      
    } catch (error) {
      if (error.response?.status == 401 ) {
        handlerLogout();
      } 
    }
  };
 
  // save data of new User consuming WS
  const handlerAddUser = async (user) => {
    
    //check admin access
    if (!login.isAdmin) return;

    let response;

    try {
      if (user.id === 0) {
        
        response = await saveUsersWs(user);
        // call the new function of the slice (Redux)
        dispatch(addUser(response.data));
      } else {
        
        response = await updateUsersWs(user);
        // call the new function of the slice (Redux)
        dispatch(updateUser(response.data));
      }

      Swal.fire({
        title: user.id === 0 ? "Usuario Creado!" : "Usuario Actualizado!",
        text: "El usuario ha sido almacenado!",
        icon: "success",
      });

      handlerCloseForm();
      // defined in UserRoutes.jsx
      navigate("/users");

      //capture validation errors on 400 bad request response
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(fillError(error.response.data));
      // capture 500 server errors due to Unike key DB restrictions
      } else if ( error.response && error.response.status == 500 &&
        error.response.data?.message?.includes("constraint")
      ) {
        if (error.response.data?.message?.includes("UK_USER_NAME")) {
          dispatch(fillError({ username: "Username already exists" }));
        }

        if (error.response.data?.message?.includes("UK_EMAIL")) {
          dispatch(fillError({ email: "Email already exists" }));
        }
        // expired or invalid JWT token
      } else if (error.response?.status == 401 ) {
        handlerLogout();
      }  else {
        throw error;
      }
    }
  };

  // delete data of User consuming WS
  const handlerDeleteUser = (id) => {

    //check admin access
    if (!login.isAdmin) return;

    Swal.fire({
      title: "Esta seguro?",
      text: "Esta acción no se puede deshacer!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Sí, borrarlo!",
    }).then( async(result) => {
      
      if (result.isConfirmed) {
        
        try {
          //call the WS
          await deleteUsersWs(id);

          //lanza el evento del Slice (REDUX)
          dispatch(deleteUser(id));

          Swal.fire({
            title: "Eliminado!",
            text: "El usuario ha sido borrado.",
            icon: "success",
          });          
        } catch (error) {
          if (error.response?.status == 401 ) {
            handlerLogout();
          }
        }
      }
    });
  };



  //variables and functions that components will use like in UsersApp.jsx
  return {
    users,
    userSelected,
    initialUserFormValues,
    visibleForm,
    errors,
    isLoading,
    paginator,

    handlerUserSelectedInForm,
    handlerAddUser,
    handlerDeleteUser,
    handlerOpenForm,
    handlerCloseForm,
    getusers,
  };
};
