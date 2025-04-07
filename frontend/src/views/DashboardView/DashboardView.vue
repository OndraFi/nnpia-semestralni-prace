<template>
  <Default>
    <div class="container-md">
    <h1>Dashboard</h1>
    <form @click.prevent="submitCreateForm">
      <div class="form-floating mt-3">
        <input type="text"
               minlength="4"
               id="restaurantName"
               class="form-control"
               required
               placeholder="název restaurace"
               v-model="restaurantName">
        <label for="restaurantName">Název restaurace</label>
      </div>
      <div class="form-floating mt-3">
        <input type="number"
               id="capacity"
               class="form-control"
               required
               min="0"
               placeholder="Kapacita restaurace"
               v-model="capacity">
        <label for="capacity">Kapacita restaurace</label>
      </div>
      <div class="form-floating mt-3">
        <input type="time"
               required
               id="openTime"
               class="form-control"
               placeholder="otevírací doba"
               v-model="openTime">
        <label for="openTime">Otevýrací doba</label>
      </div>
      <div class="form-floating mt-3">
        <input type="time"
               id="closeTime"
               required
               placeholder="zavírací doba"
               class="form-control"
               v-model="closeTime">
        <label for="closeTime">Čas zavření</label>
      </div>
      <input class="btn btn-primary w-100 mt-3" type="submit" value="Přidat">
    </form>
    <restaurant-card :restaurants="restaurants" :dashboard="true"/>
    </div>
  </Default>
</template>
<script lang="ts">
import Default from "@/layouts/Default.vue";
import {createRestaurant, findRestaurantsByUser, type RestaurantRequest, type Restaurant} from "@/api/restaurant.ts";
import RestaurantCard from "@/components/RestaurantCard.vue";

export default {
  components: {RestaurantCard, Default},
  data() {
    const restaurants : Restaurant[] = [];
    return {
      restaurantName: '',
      capacity: 0,
      openTime: '',
      closeTime: '',
      restaurants
    }
  },
  methods: {
    async submitCreateForm() {
      if (this.restaurantName.length > 3 && this.capacity > 0){
        const restaurant : RestaurantRequest = {
          name: this.restaurantName,
          capacity: this.capacity,
          openTime: this.openTime,
          closeTime: this.closeTime
        }
        createRestaurant(restaurant).then(response => {
          console.log(response);
          this.restaurantName = "";
          this.capacity = 0;
          this.openTime = '';
          this.closeTime = '';
          this.findUserRestaurants();
        }).catch(e => {
          console.log(e);
        })
      } else
        console.log("Název restaurace musí mít alespoň 3 znaky.");
    },
    findUserRestaurants() {
      findRestaurantsByUser().then(response => {
        console.log(response);
        this.restaurants = response;
      })
    }
  },
  mounted() {
    this.findUserRestaurants();
  }
}
</script>
<style scoped>

</style>
