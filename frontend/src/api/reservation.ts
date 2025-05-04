import axiosInstance from "@/api/AuthenticatedAxiosInstance.ts";
import type {Address} from "@/api/address.ts";
import type {Desk} from "@/api/desk.ts";

const baseUrl = import.meta.env.VITE_API_URL + "/restaurants";

export interface Reservation {
  id : number;
  uuid : string;
  startTime : string;
  endTime : string;
  phone : string;
  email : string;
  reservationName : string;
  numberOfPeople : number;
  isAccepted: boolean;
  desk: Desk | null;
}

export interface ReservationRequest {
  startTime: string;
  endTime: string;
  phone: string;
  email: string;
  reservationName: string;
  numberOfPeople: number;
  desk: Desk | null;
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

export const getAvailableTimesForDesk = (restaurantId: number, date: string, deskId: number): Promise<Array<ReservationTimeRange>> => {
  return axiosInstance.get(baseUrl + `/${restaurantId}/reservations/available-times/${deskId}?date=${date}`).then(response => {
    return response.data;
  })
}

export const createReservation = (restaurantId: number, reservationRequest: ReservationRequest) : Promise<Reservation> => {
  return axiosInstance.post(baseUrl + `/${restaurantId}/reservations`, reservationRequest).then(response=>{
    return response.data;
  })
}

export const getReservationsByRestaurantId = (restaurantId: number, searchName: string, searchAccepted: boolean, searchNotAccepted: boolean) : Promise<Array<Reservation>> => {
  let url = baseUrl + `/${restaurantId}/reservations`;
  if(searchName)
    url += `?name=${searchName}`;
  if(searchAccepted){
    if(url.includes("?")){
      url += `&accepted=true`;
    }else{
      url += `?accepted=true`;
    }
  }
  if(searchNotAccepted){
    if(url.includes("?")){
      url += `&accepted=false`;
    }else{
      url += `?accepted=false`;
    }  }

  console.log(url);
  return axiosInstance.get(url).then(response=>{
    return response.data;
  })
}

export const acceptReservation = (restaurantId: number, reservationId: number) : Promise<Reservation> => {
  return axiosInstance.post(baseUrl + `/${restaurantId}/reservations/${reservationId}/accept`).then(response=>{
    return response.data;
  })
}

export const cancelReservation = (restaurantId: number, reservationUuid: string) : Promise<Reservation> => {
  return axiosInstance.delete(baseUrl + `/${restaurantId}/reservations/${reservationUuid}`).then(response=>{
    return response.data;
  })
}
