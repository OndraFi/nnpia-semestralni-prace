<template>
  <Default>
    <form v-if="restaurant" @submit.prevent="updateRestaurant">
      <input type="text" v-model="restaurant.name" placeholder="Název restaurace">
      <input type="number" min="0" v-model="restaurant.capacity" placeholder="Kapacita restaurace">
      <input type="time" v-model="restaurant.openTime" placeholder="Open time">
      <input type="time" v-model="restaurant.closeTime" placeholder="Close time">
      <input type="submit">
    </form>
    <form v-if="!(restaurant?.address)" @submit.prevent="createRestaurantAddress">
      <input type="text" v-model="addressCreateRequest.country" required placeholder="country">
      <input type="text" v-model="addressCreateRequest.city" required placeholder="city">
      <input type="text" v-model="addressCreateRequest.street" required placeholder="street">
      <input type="text" v-model="addressCreateRequest.buildingNumber" required placeholder="building Number">
      <input type="text" v-model="addressCreateRequest.zipCode" required placeholder="zip">
      <input type="submit">
    </form>
    <form v-if="restaurant?.address" @submit.prevent="updateRestaurantAddress">
      <input type="text" v-model="restaurant.address.country" required placeholder="country">
      <input type="text" v-model="restaurant.address.city" required placeholder="city">
      <input type="text" v-model="restaurant.address.street" required placeholder="street">
      <input type="text" v-model="restaurant.address.buildingNumber" required placeholder="building Number">
      <input type="text" v-model="restaurant.address.zipCode" required placeholder="zip">
      <input type="submit">
    </form>
  </Default>
</template>;
<script lang="ts">
import Default from "@/layouts/Default.vue";
import {findRestaurantById, type Restaurant, updateRestaurantById} from "@/api/restaurant.ts";
import {
  type Address,
  type AddressRequest,
  createAddress,
  findAddress,
  updateAddress
} from "@/api/address.ts"

export default {
  components: {Default},
  data() {
    let restaurant: Restaurant = null;
    let addressCreateRequest : AddressRequest = {
      street : "",
      city : "",
      buildingNumber: "",
      zipCode : "",
      country: ""
    }
    return {
      restaurant,
      addressCreateRequest
    }
  },
  methods: {
    async findRestaurant() {
      await findRestaurantById(this.$route.params.id).then(response => {
        this.restaurant = response;
      }).catch(e => {
        console.log(e);
      })
    },
    async updateRestaurant() {
      console.log("updating");
      await updateRestaurantById(this.restaurant).then(restaurant => {
        this.restaurant = restaurant;
        console.log("updated");
      }).catch(e => {
        console.log(e)
      })
    },
    async findRestaurantAddress() {
      console.log("finding restaurant address");
      if (this.restaurant) {
        await findAddress(this.restaurant.id).then(address => {
          console.log(address);
          this.restaurant.address = address;
        }).catch(e=>{
          console.log(e);
        })
      }
    },
    async createRestaurantAddress(){
      await createAddress(this.restaurant.id,this.addressCreateRequest).then(address=>{
        this.restaurant.address = address;
      }).catch(e=>{
        console.log(e);
      })
    },
    async updateRestaurantAddress(){
      await updateAddress(this.restaurant.id, this.restaurant.address).then(address=>{
        this.restaurant.address = address;
      }).catch(e=>{
        console.log(e);
      })
    }
  },
  async created() {
    await this.findRestaurant();
    // await this.findRestaurantAddress();
  }
}
</script>
