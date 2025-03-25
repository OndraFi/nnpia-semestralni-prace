<template>
  <Default>
    <div class="container-md">
    Dashboard
    <form @click.prevent="submitCreateForm">
      <input type="text" minlength="4" required placeholder="název restaurace"
             v-model="restaurantName">
      <input type="number" required min="0" placeholder="Kapacita restaurace"
        v-model="capacity">
      <input type="time" required placeholder="otevírací doba"
      v-model="openTime">
      <input type="time" required placeholder="zavírací doba"
      v-model="closeTime">
      <input type="submit">
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
