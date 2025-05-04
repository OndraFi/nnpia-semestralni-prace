import axiosInstance from "@/api/AuthenticatedAxiosInstance.ts";
import axios from "axios";

const baseUrl = import.meta.env.VITE_API_URL + "/restaurant-images";

export const uploadRestaurantImage = async (restaurantId : number, file: FormData) => {
    return axiosInstance.post(`${baseUrl}/restaurant/${restaurantId}`,file)
}

export const getRestaurantImageUrl = (uuid: string) => {
  return `${baseUrl}/${uuid}`;
}

export const deleteRestaurantImage = async (uuid: string) => {
  return axiosInstance.delete(`${baseUrl}/${uuid}`)
}
