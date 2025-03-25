import axiosInstance from "@/api/AuthenticatedAxiosInstance.ts";

const baseUrl = import.meta.env.VITE_API_URL + "/restaurants";

export interface AddressRequest {
  street : String;
  city : String;
  zipCode : String;
  country : String;
  buildingNumber : String;
}

export interface Address {
  id : number;
  street : String;
  city : String;
  zipCode : String;
  country : String;
  buildingNumber : String;
}

const addressToRequest = (address : Address) : AddressRequest => {
    return {
      street: address.street,
      city: address.city,
      zipCode: address.zipCode,
      country: address.country,
      buildingNumber: address.buildingNumber
    }
}

export const createAddress = async (restaurantId: number, address : AddressRequest) : Promise<Address> => {
  return axiosInstance.post(baseUrl + `/${restaurantId}/addresses`, address).then(response=>{
    return response.data;
  })
}

export const updateAddress = async (restaurantId: number, address: Address) : Promise<Address> => {
  return axiosInstance.put(baseUrl + `/${restaurantId}/addresses`, addressToRequest(address)).then(response=>{
    return response.data;
  })
}

export const findAddress = async (restaurantId: number) : Promise<Address> => {
  return axiosInstance.get(baseUrl+`/${restaurantId}/addresses`).then(response=>{
    return response.data;
  })
}

