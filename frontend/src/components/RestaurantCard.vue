<template>
  <div v-for="restaurant in restaurants"
       class="my-4 shadow restaurant-card border-4">
    <router-link class="row text-decoration-none text-black"
               :to="dashboard ? `/dashboard/restaurant/${restaurant.id}` :`/restaurant/${restaurant.id}`">
      <div class="col-md-4">
        <div class="restaurant-img"
             v-if="restaurant.imagesIds && restaurant.imagesIds.length > 0"
             :style="`background-image: url(${getRestaurantImageUrl(restaurant.imagesIds[0])})`">

        </div>
        <img v-else src="../assets/img/breakfast.svg" alt="restaurant image" class="img-fluid">

      </div>
      <div class="col-md-8 ps-4 py-4">
<!--        <router-link :to="`/dashboard/restaurant/${restaurant.id}`" v-if="dashboard">-->
<!--          <span>id:{{ restaurant.id }}</span>-->
<!--        </router-link>-->
        <h4 class="mb-0">{{ restaurant.name }}</h4>
        <small v-if="restaurant.address" class="text-muted">{{ restaurant.address.street }} {{ restaurant.address.buildingNumber }}, {{ restaurant.address.zipCode }} {{ restaurant.address.city }}, {{ restaurant.address.country }}</small>
        <small v-else>#</small>
        <!--        <span>capacity:{{ restaurant.capacity }}</span>-->
<!--        <div class="" v-if="restaurant.address">-->
<!--          <span>Země: {{ restaurant.address.country }}</span>-->
<!--          <br>-->
<!--          <span>město: {{ restaurant.address.city }}</span>-->
<!--          <br>-->

          <!--          <br>-->
<!--          <span>číslo popisné: {{ restaurant.address.buildingNumber }}</span>-->
<!--          <br>-->
<!--          <span>psč: {{ restaurant.address.zipCode }}</span>-->
<!--        </div>-->
<!--        <div class="col-md-6" v-else> #</div>-->

      </div>
    </router-link>
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
  width: 100%;
  height: 200px;
  background-position: center;
  background-size: cover;
}
.restaurant-card{
  border-right: 1px solid white;
  &:hover{
    border-right: 1px solid #071952;
  }
}
</style>
