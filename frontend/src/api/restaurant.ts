import axiosInstance from "@/api/AuthenticatedAxiosInstance.ts";
import type {Address} from "@/api/address.ts";

const baseUrl = import.meta.env.VITE_API_URL + "/restaurants";

export interface RestaurantRequest {
  name: string;
  capacity: number;
  openTime: string;
  closeTime: string;
}

export interface Restaurant {
  id: number;
  name: string;
  capacity: number;
  openTime: string;
  closeTime: string;
  address?: Address;
  imagesIds: Array<String>;
}

export interface RestaurantsPaginated {
  restaurants: Array<Restaurant>,
  total: number,
}

const restaurantToRequest = (restaurant: Restaurant) : RestaurantRequest => {
    return {
      name: restaurant.name,
      capacity: restaurant.capacity,
      openTime: restaurant.openTime,
      closeTime: restaurant.closeTime
    }
}

export const createRestaurant = async (restaurant: RestaurantRequest) => {
  return axiosInstance.post(baseUrl, restaurant).then(response => {
    const restaurant: Restaurant = response.data;
    return restaurant;
  })
}

export const findRestaurantsByUser = async () : Promise<Array<Restaurant>> => {
  return axiosInstance.get(baseUrl+"/byUser").then(response=>{
      return response.data;
  })
}

export const findRestaurantById = async (id : number) : Promise<Restaurant> => {
  return axiosInstance.get(baseUrl + "/" + id).then(response=> response.data);
}

export const updateRestaurantById = async (restaurant : Restaurant) : Promise<Restaurant> => {
    return axiosInstance.put(baseUrl + "/" + restaurant.id, restaurantToRequest(restaurant)).then(response=>{
      return response.data;
    })
}

export const findRestaurantsPaginated = async (page: number, size: number, searchSlug: string): Promise<RestaurantsPaginated> => {
  return axiosInstance.get(`${baseUrl}?page=${page}&size=${size}&searchSlug=${searchSlug}`).then(response => {
    return response.data;
  });
}
