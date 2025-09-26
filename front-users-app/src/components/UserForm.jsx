import { useEffect, useState } from "react";
import { useUsers } from "../hooks/useUsers";


export const UserForm = ({ userSelected,  handlerCloseForm }) => {

  const {handlerAddUser, initialUserFormValues, errors} = useUsers();

  const [userForm, setUserForm] = useState(initialUserFormValues);
  //const [checked, setChecked] = useState(userForm.admin);

  const { id, username, password, email, admin } = userForm;
  
  useEffect(() => {
    // cuando cambie el objeto: userSelected se pobla el form
    setUserForm({...userSelected});

  }, [userSelected] );

  const onInputChange = ({ target }) => {
    const { name, value } = target;

    setUserForm({
      ...userForm,
      [name]: value,
    });
  };

  const onCheckboxChange = () => {

    // change the value from state;  
    setUserForm({
      ...userForm,
      admin: !userForm.admin,
    });

  };

  const onSubmitUserForm = (event) =>{
    event.preventDefault(); // para evitar que se haga refresh de la pagina cuando se envie el form

    //validate empty form input at JS level
    // if(!username || !password || !email){
    //     Swal.fire("Debe llenar todos los campos!");
    //     return;
    // }

    //guardar los dators del userForm
    handlerAddUser(userForm);
    //setUserForm(initialUserFormValues);
  }

  const onCloseForm = () => {
    handlerCloseForm();
    setUserForm(initialUserFormValues);
  }
  

  return (
    <>
      <form onSubmit={onSubmitUserForm}>
        <input
          className="form-control my-2 w-75"
          placeholder="Username"
          name="username"
          value={username}
          onChange={onInputChange}
        />
        <p className="text-danger">{ errors?.username}</p>

        { id > 0 ? '' :  <input
            className="form-control my-2 w-75"
            type="password"
            placeholder="Password"
            name="password"
            value={password}
            onChange={onInputChange}
            />
        }
        <p className="text-danger">{ errors?.password}</p>
        
        <input
          className="form-control my-2 w-75"
          type="email"
          placeholder="Email"
          name="email"
          value={email}
          onChange={onInputChange}
        />
        <p className="text-danger">{ errors?.email}</p>

        <div className="my-3 form-check">
          <input type="checkbox" className="form-check-input" 
            name="admin" 
            checked ={userForm.admin}
            onChange={onCheckboxChange}
          />
          <label className="form-check-label">Admin</label>
        </div>

        <input type="hidden" name="id" value={id}  />
        <button className="btn btn-primary" type="submit">
          { id > 0 ? 'Update' : 'Create' }
        </button>
        { !handlerCloseForm || 
          <button className="btn btn-success mx-2" type="button"
            onClick={()=> onCloseForm()}> 
              Cerrar
          </button> }
      </form>
    </>
  );
};
