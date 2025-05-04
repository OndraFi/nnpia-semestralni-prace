import axiosInstance from "@/api/AuthenticatedAxiosInstance.ts";
const baseUrl = import.meta.env.VITE_API_URL + "/restaurants";


export interface Desk{
  id: number,
  name: string,
  capacity: number,
}

export interface DeskRequest{
  name: string,
  capacity: number,
}

export const deskToRequest = (desk: Desk) : DeskRequest => {
  return {
    name: desk.name,
    capacity: desk.capacity,
  }
}


export const createDesk = async (restaurantId: number, desk: DeskRequest) : Promise<Desk> => {
  return axiosInstance.post(baseUrl + `/${restaurantId}/desks`, desk).then(response => {
    return response.data;
  })
}

export const getDesksByRestaurantId = async (restaurantId: number) : Promise<Array<Desk>> => {
  return axiosInstance.get(baseUrl + `/${restaurantId}/desks`).then(response => {
    return response.data;
  })
}

export const updateDeskById = async (restaurantId: number, desk: Desk) : Promise<Desk> => {
  return axiosInstance.put(baseUrl + `/${restaurantId}/desks/${desk.id}`, deskToRequest(desk)).then(response => {
    return response.data;
  })
}

export const deleteDeskById = async (restaurantId: number, deskId: number) : Promise<void> => {
  return axiosInstance.delete(baseUrl + `/${restaurantId}/desks/${deskId}`).then(response => {
    return response.data;
  })
}
