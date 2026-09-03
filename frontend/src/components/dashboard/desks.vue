<template>
  <div>
    <span class="h3">Stoly</span>
    <form @submit.prevent="addDesk">
      <div class="form-floating">
        <input type="text"
               class="form-control mt-3"
               id="name"
               v-model="deskRequest.name"
               required
               placeholder="Název">
        <label for="country">Název</label>
      </div>
      <div class="form-floating">
        <input type="number"
               class="form-control mt-3"
               id="people"
               v-model="deskRequest.capacity"
               required
               :min="1"
               :max="restaurant?.capacity"
               placeholder="kapacita stolu">
        <label for="country">Kapacita stolu</label>
      </div>
      <input v-if="!isLoading" type="submit" value="Přidat stůl" class="btn btn-primary w-100 mt-3">
      <div v-else class="text-center mt-3"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div></div>
    </form>
    <div class="row mt-3">
      <div
        class="col-md-4 mb-3"
        v-for="desk in desks"
        :key="desk.name"
      >
        <div class="card shadow-sm border-0 h-100 position-relative">
          <!-- Tlačítko smazání v pravém horním rohu -->
          <button
            class="btn btn-sm btn-outline-danger position-absolute top-0 end-0 m-2"
            @click="deleteDesk(desk)"
            title="Smazat stůl"
          >
            <i class="bi bi-trash-fill"></i>
          </button>

          <div class="card-body">
            <h5 class="card-title">
              <i class="bi bi-table me-2"></i>{{ desk.name }}
            </h5>
            <p class="card-text">
              <strong>Kapacita:</strong> {{ desk.capacity }}
            </p>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>
<script lang="ts">

import {
  createDesk,
  deleteDeskById,
  type Desk,
  type DeskRequest,
  getDesksByRestaurantId
} from "@/api/desk.ts";

import {type Restaurant} from "@/api/restaurant.ts";

export default {
  props:{
    restaurant: {
      type: Object as () => Restaurant,
      required: true
    }
  },
  data(){
    let deskRequest: DeskRequest = {
      name: "",
      capacity: 1,
    }
    return{
      isLoading: false,
      desks: null as Array<Desk> | null,
      deskRequest,
    }
  },
  methods:{
    async addDesk() {
      this.isLoading = true;
      if (this.restaurant)
        createDesk(this.restaurant?.id, this.deskRequest).then((desk: Desk) => {
          this.$notify({
            type: 'success',
            text: 'Stůl přidán.'
          })
          this.deskRequest.name = "";
          this.deskRequest.capacity = 1;
          if (this.desks)
            this.desks.push(desk)
        }).catch(e => {
          this.$notify({
            type: 'error',
            text: 'Nepovedlo se přidat stůl.'
          })
          console.log(e);
        }).finally(() => {
          this.isLoading = false;
        })
    },
    async deleteDesk(desk: Desk) {
      if (this.restaurant) {
        await deleteDeskById(this.restaurant.id, desk.id).then(response => {
          if (this.desks)
            this.desks = this.desks.filter(_desk => _desk !== desk);
        })
      }
    },
    async findDesks() {
      if (this.restaurant)
        getDesksByRestaurantId(this.restaurant.id).then(desks => {
          this.desks = desks;
          console.log(desks);
        }).catch(e => {
          this.$notify(
            {
              type: 'error',
              text: 'Nepovedlo se načíst stoly.'
            }
          )
        })
    },
  },
  mounted() {
    this.findDesks();
  },
}
</script>
