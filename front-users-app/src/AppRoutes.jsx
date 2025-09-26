import { LoginPage } from "./auth/pages/LoginPage";
import { Navigate, Route, Routes } from "react-router-dom";
import { UserRoutes } from "./routes/UserRoutes";
import { useAuth } from "./auth/hooks/useAuth";

export const AppRoutes = () => {
  
  // alternative to obtain isAuth using REDUX useSelector
  // const { isAuth } = useSelector(state => state.auth);  
  const { login } = useAuth();

    if (login.isLoginLoading){
      return (
        <div className="container m-4">
          <h4>Cargando ...</h4>
          <div className="spinner-border text-warning" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      );
    }

    return (
      <Routes>
        {
          login.isAuth 
            ? ( <Route path= '/*' element={ <UserRoutes />} /> ) 
            : <>
                <Route path='/login' element={ <LoginPage  /> } />
                
                {/* path='/*' cualquier otra ruta que que no este definida se direcciona a: /login  */}
                <Route path='/*' element= { <Navigate to="/login" /> } /> 
            </>
        }
      </Routes>
    );

}
