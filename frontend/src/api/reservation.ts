import axiosInstance from "@/api/AuthenticatedAxiosInstance.ts";
import type {Address} from "@/api/address.ts";

const baseUrl = import.meta.env.VITE_API_URL + "/restaurants";

export interface Reservation {
  id : number;
  startTime : string;
  endTime : string;
  phone : string;
  email : string;
  reservationName : string;
  numberOfPeople : number;
  isAccepted: boolean;
}

export interface ReservationRequest {
  startTime: string;
  endTime: string;
  phone: string;
  email: string;
  reservationName: string;
  numberOfPeople: number;
}

export interface ReservationTimeRange {
  start: string,
  end: string,
}

export const getAvailableTimes = (restaurantId: number, date: string, numberOfPeople: number): Promise<Array<ReservationTimeRange>> => {
  return axiosInstance.get(baseUrl + `/${restaurantId}/reservations/available-times?date=${date}&numberOfPeople=${numberOfPeople}`).then(response => {
    return response.data;
  })
}

export const createReservation = (restaurantId: number, reservationRequest: ReservationRequest) : Promise<Reservation> => {
  return axiosInstance.post(baseUrl + `/${restaurantId}/reservations`, reservationRequest).then(response=>{
    return response.data;
  })
}

export const getReservationsByRestaurantId = (restaurantId: number) : Promise<Array<Reservation>> => {
  return axiosInstance.get(baseUrl + `/${restaurantId}/reservations`).then(response=>{
    return response.data;
  })
}

export const acceptReservation = (restaurantId: number, reservationId: number) : Promise<Reservation> => {
  return axiosInstance.post(baseUrl + `/${restaurantId}/reservations/${reservationId}/accept`).then(response=>{
    return response.data;
  })
}

export const cancelReservation = (restaurantId: number, reservationUuid: number) : Promise<Reservation> => {
  return axiosInstance.delete(baseUrl + `/${restaurantId}/reservations/${reservationUuid}`).then(response=>{
    return response.data;
  })
}
