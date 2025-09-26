

import { authService } from "../services/authService";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { onLogin, onLogout, onStartLogin } from "../../store/slices/auth/authSlice";

/**
 * Hook to performs the login in the backend and stores the JWT token in session
 * @returns 
 */
export const useAuth = () => {

  // New REDUX loads variables from authSlice.js
  const {user, isAdmin, isAuth, isLoginLoading } = useSelector(state => state.auth);
  const dispatch = useDispatch();

  // used for redirect to a URL
  const navigate = useNavigate();

  //performs login in backend
  const handlerLogin = async ({ username, password }) => {
    try {

      //mostrar spinner mientras culmina proceso de login
      dispatch(onStartLogin());

      // login WS
      const responseWsLogin = await authService({ username, password });

      const jwtToken = responseWsLogin.data.token;
      // [0] is the header, [1] is the payload, [2] the signature
      const payload = jwtToken.split(".")[1];
      // atob decodes the JWT Base64 string
      const claims = JSON.parse(window.atob(payload));
      const user = { username: claims.sub };

      // Stores the JWT token
      sessionStorage.setItem("token", `Bearer ${jwtToken}`);

      // consumes the new REDUX slice
      dispatch(onLogin({ user, isAdmin: claims.isAdmin }));

      sessionStorage.setItem(
        "login",
        JSON.stringify({
          isAuth: true,
          isAdmin: claims.isAdmin,
          user,
        })
      );

      // defined in UserRoutes.jsx
      navigate("/users");
    } catch (error) {
      dispatch(onLogout());

      if (error.response?.status == 401) {
        Swal.fire("Error Login", "Username o password incorrectos", "error");
      } else if (error.response?.status == 403) {
        Swal.fire("Error Login", "No tiene acceso al recurso", "error");
      } else {
        throw error;
      }
    }
  };

  const handlerLogout = () => {
    dispatch(onLogout());

    sessionStorage.removeItem("login");
    sessionStorage.removeItem("token");
    sessionStorage.clear();
  };

  // values that components will use
  return {
    login : { user, isAdmin, isAuth, isLoginLoading },
    handlerLogin,
    handlerLogout,
  };
};
