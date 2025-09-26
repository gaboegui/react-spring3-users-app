import { Provider } from "react-redux";
import { AppRoutes } from "./AppRoutes";
import { store } from "./store/store";


export const UsersApp = () => {
  return (
    // Provider: indica que usara el store de redux
    <Provider store={store}>
      <AppRoutes/>
    </Provider>
    
  )  


};
