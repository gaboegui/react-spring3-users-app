# Users App - Frontend

This project is the frontend for a user management application, built with React. It provides a user-friendly interface for interacting with the user data, allowing for creation, retrieval, updating, and deletion of users.

## Features

*   **User Authentication:** Secure login system to protect user data.
*   **User Management:**
    *   View a paginated list of users.
    *   Create new users.
    *   Update existing users.
    *   Delete users.
*   **Responsive Design:** The application is designed to work on various screen sizes.
*   **State Management:** Utilizes Redux for predictable state management.
*   **Routing:** Uses React Router for client-side routing.

## Technologies Used

*   **React:** A JavaScript library for building user interfaces.
*   **Vite:** A fast build tool and development server for modern web projects.
*   **Redux Toolkit:** The official, opinionated, batteries-included toolset for efficient Redux development.
*   **React Router:** For declarative routing in React applications.
*   **Axios:** A promise-based HTTP client for the browser and Node.js.
*   **SweetAlert2:** A beautiful, responsive, customizable, and accessible replacement for JavaScript's popup boxes.
*   **ESLint:** A tool for identifying and reporting on patterns found in ECMAScript/JavaScript code.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

*   Node.js and npm installed on your machine.

### Installation

1.  Clone the repo
    ```sh
    git clone https://your-repository-link.com
    ```
2.  Navigate to the `front-users-app` directory
    ```sh
    cd front-users-app
    ```
3.  Install NPM packages
    ```sh
    npm install
    ```

### Running the Application

To run the application in development mode, execute the following command:

```sh
npm run dev
```

This will start the development server, and you can view the application by navigating to `http://localhost:5173` in your browser.

## Project Structure

The project structure is organized as follows:

```
front-users-app/
├── src/
│   ├── apis/
│   ├── assets/
│   ├── auth/
│   ├── components/
│   ├── hooks/
│   ├── pages/
│   ├── routes/
│   ├── services/
│   ├── store/
│   ├── App.jsx
│   ├── main.jsx
│   └── styles.css
├── .env
├── .eslintrc.cjs
├── .gitignore
├── index.html
├── package.json
└── vite.config.js
```

*   **`src/apis`**: Contains the Axios instance configuration for making API requests.
*   **`src/auth`**: Includes hooks, pages, and services related to user authentication.
*   **`src/components`**: Reusable React components used throughout the application.
*   **`src/hooks`**: Custom React hooks for managing state and logic.
*   **`src/pages`**: Components that represent the different pages of the application.
*   **`src/routes`**: Defines the application's routing structure.
*   **`src/services`**: Contains services for interacting with the backend API.
*   **`src/store`**: Redux store configuration, including slices for different parts of the application state.