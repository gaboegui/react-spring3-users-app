import { useUsers } from "../hooks/useUsers";
import { useAuth } from "../auth/hooks/useAuth";

export const UserRow = ({ user }) => {
  
  // REDUX call to hooks
  const { handlerUserSelectedInForm, handlerDeleteUser } =  useUsers();
  const { login } = useAuth();

  const { id, username, email } = user;

  const onDeleteuser = (id) => {
    // viene desde UsersApp -> UsersList
    handlerDeleteUser(id);
  };

  return (
    <tr>
      <td>{id}</td>
      <td>{username}</td>
      <td>{email}</td>
      {!login.isAdmin || (
        <>
          <td>
            <button
              type="button"
              className="btn btn-secondary btn-sm"
              onClick={() => handlerUserSelectedInForm(user)}
            >
              Update
            </button>
            {/* &nbsp;
         <NavLink to={'users/edit/' + id }
            className={'btn btn-secondary btn-sm'}  >
          <i className="fa-regular fa-pen-to-square"></i>
        </NavLink> */}
          </td>
          <td>
            <button
              type="button"
              className="btn btn-danger btn-sm"
              onClick={() => onDeleteuser(user.id)}
            >
              <i className="fa-solid fa-trash"></i>
            </button>
          </td>
        </>
      )}
    </tr>
  );
};
