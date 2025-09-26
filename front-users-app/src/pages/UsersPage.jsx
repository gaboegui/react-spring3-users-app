import { useEffect } from "react";
import { UserModalForm } from "../components/UserModalForm";
import { UsersList } from "../components/UsersList";    
import { useUsers } from "../hooks/useUsers";
import { useAuth } from "../auth/hooks/useAuth";
import { Paginator } from "../components/Paginator";
import { useParams } from "react-router-dom";

export const UsersPage = () => {

  // obtain page number from URL 
  const { page } = useParams();

  // using the hook with REDUX
  const { users, visibleForm, isLoading, paginator, handlerOpenForm, getusers } = useUsers();

  const { login } = useAuth();
  
  // obtain users when component is created []
  useEffect(()=>{
    getusers(page);
  }, [ , page]);  //triggers not only onLoad also when clicks in another page number
  
  if(isLoading){
    return (
      <div className="container m-4">
        <h4>Cargando ...</h4>
        <div className="spinner-border text-warning" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    )
  }
  return (
    <>
      
      {!visibleForm === true || 
        <UserModalForm />
      }
      <div className="container m-4">
        <h2>Users App</h2>
        <div className="row">
          <div className="col">
            { (visibleForm || !login.isAdmin ) === false ? (
              <button
                className="btn btn-primary my-2"
                onClick={handlerOpenForm}
              >
                Nuevo Usuario
              </button>
            ) : (
              ""
            )}

            {users.length === 0 ? (
              <div className="alert alert-warning">
                No hay usuarios en el Sistema
              </div>
              ) : 
                <>
                  <UsersList />
                  <Paginator url="/users/page" paginator={paginator} />
                </>  
            }
          </div>
        </div>
      </div>
    </>
  );
};
