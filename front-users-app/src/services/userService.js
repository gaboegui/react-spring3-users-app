import usersApi from "../apis/usersApi";

/**
 * CRUD Methods to consume User microservice
 */
const BASE_WS_URL = '';

const config_headers = () => {
  return {
    headers: {
      Authorization: sessionStorage.getItem("token"),
      "Content-Type": "application/json",
    }
  }
};

export const getUsersWs = async () => {
  try {
    // consume ws using Axios library
    const response = await usersApi.get(BASE_WS_URL);
    return response;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const getUsersbyPagesWs = async (page = 0) => {
  try {
    // consume ws using Axios library
    const response = await usersApi.get(`${BASE_WS_URL}/page/${page}` );
    return response;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const saveUsersWs = async ({ username, email, password, admin }) => {

    // POST request using axios
    return await usersApi.post(
      BASE_WS_URL, { username, email, password, admin },  config_headers()
    );

};

export const updateUsersWs = async ({ id, username, email, admin }) => {

    // PUT request using axios, note: password field in backend controller is not stored by this method
    return await usersApi.put(
      `${BASE_WS_URL}/${id}`,
      { username, email, password: "anything", admin },
      config_headers()
    );
  
};

export const deleteUsersWs = async (id) => {

    // DELETE request using axios
    await usersApi.delete(`${BASE_WS_URL}/${id}`, config_headers());
  
};
