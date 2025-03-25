import axios from "axios";

const baseUrl = import.meta.env.VITE_API_URL + "/auth";

export const login = async (email: string, password: string) => {
  return axios.post(baseUrl+ "/login",{
    email,
    password
  });
}

export const signUp = async (email: string, password: string) => {
  return axios.post(baseUrl+ "/signup",{
    email,
    password
  });
}
