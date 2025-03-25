<template>
  <Default>
    <h1 class="text-center my-5">Restaurace</h1>
    <div class="my-5 container-md">
      <div class="d-flex">
        <div class="form-floating">
          <input type="text" class="form-control" id="search" v-model="searchSlug" @input="search"
                 placeholder="Název restaurace"/>
          <label for="search">Název restaurace</label>
        </div>
        <div class="dropdown ms-auto">
          <button class="btn btn-secondary dropdown-toggle" type="button"
                  data-bs-toggle="dropdown" aria-expanded="false">
            {{ size }}
          </button>
          <ul class="dropdown-menu">
            <li v-for="s in sizes">
              <button class="dropdown-item" @click="selectPageSize(s)">{{ s }}</button>
            </li>
          </ul>
        </div>
      </div>
      <restaurant-card :restaurants="restaurants"/>
      <div v-if="total  === 0 && !loadingRestaurants" class="text-center p-3 bg-warning">Žádná restaurace nebyla nalezena!</div>

      <div class="row" v-if="total > 0">
        <nav class="col-12 text-center btn">
          <ul class="pagination justify-content-center">
            <li class="page-item">
              <button class="page-link" @click="goToFirstPage()">
                1
              </button>
            </li>
            <li class="page-item">
              <button class="page-link" @click="goToPreviousPage()">
                předchozí
              </button>
            </li>
            <li class="page-item">
              <span class="page-link disabled">{{page+1}}</span>
            </li>
            <li class="page-item">
              <button class="page-link" @click="goToNextPage()">
                další
              </button>
            </li>
            <li class="page-item">
              <button class="page-link" @click="goToLastPage()">
                {{ maxPages }}
              </button>
            </li>
          </ul>




        </nav>
      </div>
    </div>
  </Default>
</template>
<script lang="ts">
import Default from "@/layouts/Default.vue";
import {
  findRestaurantsPaginated,
  type Restaurant,
  type RestaurantsPaginated
} from "@/api/restaurant.ts";
import RestaurantCard from "@/components/RestaurantCard.vue";
import { debounce } from "lodash";

export default {
  components: {RestaurantCard, Default},
  data() {
    const restaurants: Array<Restaurant> = [];
    const sizes = [5, 10, 25, 50, 100];
    return {
      restaurants,
      total: 0,
      page: 0,
      sizes,
      size: sizes[0],
      searchSlug: "",
      searchDelay: null as number | null,
      loadingRestaurants: false,
    }
  },
  methods: {
    getUrlQueryParams() {
      const params = new URLSearchParams(window.location.search);
      if (params.has('page')) {
        this.page = parseInt(params.get('page') as string, 10);
      }
      if (params.has('size')) {
        this.size = parseInt(params.get('size') as string, 10);
      }
      if (params.has('searchSlug')) {
        this.searchSlug = params.get('searchSlug') as string;
      }
    },
    setUrlParams() {
      const params = new URLSearchParams();
      params.append('page', this.page.toString());
      params.append('size', this.size.toString());
      params.append('searchSlug', this.searchSlug);
      window.history.replaceState({}, '', `${window.location.pathname}?${params.toString()}`);
    },
    async findRestaurants() {
      this.loadingRestaurants = true;
      this.setUrlParams();
      this.restaurants = [];
      this.total = 0;
      await findRestaurantsPaginated(this.page, this.size, this.searchSlug).then((restaurantsPaginated: RestaurantsPaginated) => {
        this.restaurants = restaurantsPaginated.restaurants;
        this.total = restaurantsPaginated.total;
      })
      this.loadingRestaurants = false;
    },
    goToFirstPage() {
      this.page = 0;
      this.findRestaurants();
    },
    goToLastPage() {
      this.page = this.maxPages -1;
      this.findRestaurants();
    },
    goToPreviousPage() {
      if (this.page > 0){
        this.page--;
        this.findRestaurants()
      }
    },
    goToNextPage() {
      if(this.page < this.maxPages -1){
        this.page++;
        this.findRestaurants();
      }
    },
    selectPageSize(s: number) {
      this.size = s;
      this.findRestaurants();
    },
    search: debounce(function() {
      this.findRestaurants();
    }, 500),
    canGoToPage(page: number){
      if(page >= 0 && page <= this.maxPages -1)
        return true;
      return false;
    }
  },
  computed:{
    maxPages(){
      return Math.ceil(this.total/this.size);
    },
  },
  created() {
    this.getUrlQueryParams();
    this.setUrlParams();
    this.findRestaurants();
  }
}
</script>
