import axiosInstance from "@/api/AuthenticatedAxiosInstance.ts";
import axios from "axios";

const baseUrl = import.meta.env.VITE_API_URL + "/restaurant-images";

export const uploadRestaurantImage = async (restaurantId : number, file: FormData) => {
    return axiosInstance.post(`${baseUrl}/restaurant/${restaurantId}`,file)
}

export const getRestaurantImageUrl = (uuid: string) => {
  // axios.get(`${baseUrl}/${uuid}`).then(response=>{
  //   return response.data;
  // })
  return `${baseUrl}/${uuid}`;
}
