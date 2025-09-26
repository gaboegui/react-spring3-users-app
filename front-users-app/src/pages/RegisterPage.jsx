import { useEffect, useState } from "react"
import { UserForm } from "../components/UserForm"
import { useParams } from "react-router-dom";
import { useUsers } from "../hooks/useUsers";

export const RegisterPage = () => {

    // we obtain the data from context instead of props from parent
    const { initialUserFormValues, users=[] } = useUsers();
    
    const [userSelected, setUserSelected] = useState(initialUserFormValues);

    //obtengo el param que viene en el URL
    const { id } = useParams();

    useEffect(() => {

        if(id){
            const user = users.find(u => u.id == id) || initialUserFormValues;
            setUserSelected(user);
        }
    }, [id])
  
    return (
    <div className="container my-4">
        <h4>Register Page</h4>
        <div className="row">
            <div className="col">
                <UserForm  userSelected={userSelected} // para poblar y editar
                />
            </div>

        </div>

    </div>
  )
}
