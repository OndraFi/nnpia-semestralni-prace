<template>
  <div v-for="restaurant in restaurants"
       class="my-4 shadow restaurant-card border-bottom border-primary border-4">
    <component class="row text-decoration-none text-black" :is="dashboard ? 'div' : 'router-link'"
               :to="`/restaurant/${restaurant.id}`">
<!--      <img v-for="(image,index) in restaurant.imagesIds"-->
<!--           :key="image"-->
<!--           :src="getRestaurantImageUrl(image)"-->
<!--           alt="restaurant image"-->
<!--           style="max-width: 200px;"-->
<!--           class="d-block">-->
      <div class="col-md-2">
        <div class="restaurant-img" v-for="(image,index) in restaurant.imagesIds" :style="`background-image: url(${getRestaurantImageUrl(image)})`">

        </div>
      </div>
      <div class="col-md-4 py-4">
        <router-link :to="`/dashboard/restaurant/${restaurant.id}`" v-if="dashboard">
          <span>id:{{ restaurant.id }}</span>
        </router-link>
        <br>
        <span>name:{{ restaurant.name }}</span>
        <br>
        <span>capacity:{{ restaurant.capacity }}</span>
      </div>
      <div class="col-md-4 py-4" v-if="restaurant.address">
        <span>Země: {{ restaurant.address.country }}</span>
        <br>
        <span>město: {{ restaurant.address.city }}</span>
        <br>
        <span>ulice: {{ restaurant.address.street }}</span>
        <br>
        <span>číslo popisné: {{ restaurant.address.buildingNumber }}</span>
        <br>
        <span>psč: {{ restaurant.address.zipCode }}</span>
      </div>
      <div class="col-md-6" v-else> #</div>
    </component>
  </div>
</template>
<script lang="ts">
import {type Restaurant} from "@/api/restaurant.ts";
import {getRestaurantImageUrl} from "@/api/restaurantImage.ts";

export default {
  methods: {getRestaurantImageUrl},
  props: {
    restaurants: {
      type: Array<Restaurant>,
      required: true,
    },
    dashboard: {
      type: Boolean,
      default: false,
    }
  }
}
</script>
<style scoped>
.restaurant-img{
  width: 200px;
  height: 200px;
  background-position: center;
  background-size: cover;
}
</style>
