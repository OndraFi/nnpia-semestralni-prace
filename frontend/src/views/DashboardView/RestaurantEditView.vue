<template>
  <Default>

    <div class="container-md">
      <span class="h3">Rezervace</span>
      <div v-if="reservations" v-for="reservation in reservations" class="card my-3 shadow">
        <div class="card-body row align-items-center">
          <!-- Stav rezervace jako ikona a badge -->
          <div class="col-md-2 text-center">
            <i
              v-if="reservation.isAccepted"
              class="bi bi-check-circle-fill text-success fs-3"
              title="Schváleno"></i>
            <i
              v-else
              class="bi bi-clock-fill text-primary fs-3"
              title="Nová"></i>
          </div>

          <!-- Základní info o rezervaci -->
          <div class="col-md-7">
            <h5 class="card-title mb-1">
              Rezervace na jméno <strong>{{ reservation.reservationName }}</strong>
            </h5>
            <p class="mb-1">
              Datum: {{ new Date(reservation.startTime).toLocaleDateString() }}<br>
              Čas: {{ new Date(reservation.startTime).toLocaleTimeString() }} – {{ new Date(reservation.endTime).toLocaleTimeString() }}
            </p>
            <p class="mb-1">
              Počet osob:
              {{ reservation.numberOfPeople }}
              <span v-if="reservation.numberOfPeople === 1">osoba</span>
              <span v-else-if="reservation.numberOfPeople < 5">osoby</span>
              <span v-else>osob</span>
            </p>
          </div>

          <!-- Kontaktní informace -->
          <div class="col-md-3">
            <p class="mb-1"><i class="bi bi-telephone-fill"></i> {{ reservation.phone }}</p>
            <p class="mb-2"><i class="bi bi-envelope-fill"></i> {{ reservation.email }}</p>

            <div class="d-flex gap-2">
              <button
                v-if="!reservation.isAccepted"
                class="btn btn-sm btn-outline-success"
                @click="doAcceptReservation(reservation)">
                <i class="bi bi-check-circle"></i> Potvrdit
              </button>
              <button
                class="btn btn-sm btn-outline-danger"
                @click="doCancelReservation(reservation)">
                <i class="bi bi-x-circle"></i> Zrušit
              </button>
            </div>
          </div>
        </div>
      </div>
      <div v-if="reservations && reservations.length == 0"
           class="text-center p-3 fw-bold shadow bg-light mt-3">
        <img src="../../assets/img/warning.svg"
             alt="no restaurant found"
             style="height: 50px">
        Nemáte žádné rezervace!
      </div>

      <hr>
      <span class="h3">Základní nastavení</span>
      <form v-if="restaurant" @submit.prevent="updateRestaurant">
        <div class="form-floating mt-3">
          <input type="text" id="name" class="form-control" v-model="restaurant.name"
                 placeholder="Název restaurace">
          <label for="name">Název restaurace</label>
        </div>
        <div class="form-floating mt-3">
          <input type="number" id="capacity" class="form-control" min="0"
                 v-model="restaurant.capacity" placeholder="Kapacita restaurace">
          <label for="capacity">Kapacita restaurace</label>
        </div>
        <div class="form-floating mt-3">
          <input type="time" id="openTime" class="form-control" v-model="restaurant.openTime"
                 placeholder="Open time">
          <label for="openTime">Otevírací doba</label>
        </div>
        <div class="form-floating mt-3">
          <input type="time" id="closeTime" class="form-control" v-model="restaurant.closeTime"
                 placeholder="Close time">
          <label for="closeTime">Zavírací doba</label>
        </div>
        <input class="btn btn-primary w-100 mt-3" type="submit" value="Upravit Restauraci">
      </form>
      <hr>
      <span class="h3">Adresa</span>
      <form v-if="!(restaurant?.address)" @submit.prevent="createRestaurantAddress">
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="country"
                 v-model="addressCreateRequest.country"
                 required
                 placeholder="country">
          <label for="country">Země</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="city"
                 v-model="addressCreateRequest.city"
                 required
                 placeholder="city">
          <label for="city">Město</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="street"
                 v-model="addressCreateRequest.street"
                 required
                 placeholder="street">
          <label for="street">Ulice</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="buildingNumber"
                 v-model="addressCreateRequest.buildingNumber"
                 required
                 placeholder="building Number">
          <label for="buildingNumber">Číslo popisné</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="zip"
                 v-model="addressCreateRequest.zipCode"
                 required
                 placeholder="zip">
          <label for="zip">PSČ</label>
        </div>
        <input class="btn btn-primary w-100 mt-3" value="Upravit adresu" type="submit">
      </form>
      <form v-if="restaurant?.address" @submit.prevent="updateRestaurantAddress">
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="country"
                 v-model="restaurant.address.country"
                 required
                 placeholder="country">
          <label for="country">Země</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="city"
                 v-model="restaurant.address.city"
                 required
                 placeholder="city">
          <label for="city">Město</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="street"
                 v-model="restaurant.address.street"
                 required
                 placeholder="street">
          <label for="street">Ulice</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="buildingNumber"
                 v-model="restaurant.address.buildingNumber"
                 required
                 placeholder="building Number">
          <label for="buildingNumber">Číslo popisné</label>
        </div>
        <div class="form-floating">
          <input type="text"
                 class="form-control mt-3"
                 id="zip"
                 v-model="restaurant.address.zipCode"
                 required
                 placeholder="zip">
          <label for="zip">PSČ</label>
        </div>
        <input class="btn btn-primary w-100 mt-3" value="Upravit adresu" type="submit">
      </form>

      <hr>
      <span class="h3">Obrázky</span>
      <br>
      <span
        class="small text-muted">První obrázek bude použit jako náhledový v seznamu restaurací</span>
      <form @submit.prevent="uploadImage">
        <input type="file" class="form-control" @change="onFileChange">
        <input type="submit"
               class="btn btn-primary w-100 mt-3"
               value="Přidat obrázek">
      </form>
      <div v-if="restaurant && restaurant.imagesIds.length > 0" class="d-flex flex-wrap justify-content-start">
        <div
          v-for="image in restaurant.imagesIds"
          :key="image"
          class="card shadow-sm m-2 border-0"
          style="width: 200px;"
        >
          <img
            :src="getRestaurantImageUrl(image)"
            alt="restaurant image"
            class="card-img-top rounded"
            style="height: 150px; object-fit: cover;"
          />
          <div class="card-body text-center p-2">
            <button
              class="btn btn-sm btn-outline-danger"
              @click="deleteImage(image)"
              title="Smazat obrázek"
            >
              <i class="bi bi-trash-fill"></i>
            </button>
          </div>
        </div>
      </div>


      <hr>
      <span class="h-3">Widget</span>
      <div v-if="restaurant">
        <router-link :to="`/restaurant/${restaurant.id}/widget`"
                     class="btn btn-primary">
          Widget
        </router-link>
      </div>

    </div>
  </Default>
</template>
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
import {
  deleteRestaurantImage,
  getRestaurantImageUrl,
  uploadRestaurantImage
} from "@/api/restaurantImage.ts";
import {
  acceptReservation, cancelReservation,
  getReservationsByRestaurantId,
  type Reservation
} from "@/api/reservation.ts";

export default {
  components: {Default},
  data() {
    let addressCreateRequest: AddressRequest = {
      street: "",
      city: "",
      buildingNumber: "",
      zipCode: "",
      country: ""
    }
    return {
      restaurant: null as Restaurant | null,
      file: null as File | null,
      addressCreateRequest,
      reservations: null as Array<Reservation> | null,
    }
  },
  methods: {
    deleteRestaurantImage,
    acceptReservation,
    getRestaurantImageUrl,
    async findRestaurant() {
      await findRestaurantById(this.$route.params.id).then(response => {
        this.restaurant = response;
        console.log(response);
      }).catch(e => {
        console.log(e);
      })
    },
    async updateRestaurant() {
      console.log("updating");
      if (this.restaurant)
        await updateRestaurantById(this.restaurant).then(restaurant => {
          this.restaurant = restaurant;
          this.$notify({
            type: 'success',
            text: 'Úprava proběhla v pořádku.'
          })
          console.log("updated");
        }).catch(e => {
          this.$notify({
            type: 'error',
            text: 'Nepovedlo se uložit změny.'
          })
          console.log(e)
        })
    },
    async createRestaurantAddress() {
      await createAddress(this.restaurant.id, this.addressCreateRequest).then(address => {
        this.restaurant.address = address;
        this.$notify({
          type: 'success',
          text: 'Adresa upravena.'
        })
      }).catch(e => {
        this.$notify({
          type: 'error',
          text: 'Nepovedlo se upravit adresu.'
        })
        console.log(e);
      })
    },
    async updateRestaurantAddress() {
      await updateAddress(this.restaurant.id, this.restaurant.address).then(address => {
        this.restaurant.address = address;
        this.$notify({
          type: 'success',
          text: 'Adresa upravena.'
        })
      }).catch(e => {
        this.$notify({
          type: 'error',
          text: 'Nepovedlo se upravit adresu.'
        })
        console.log(e);
      })
    },
    onFileChange(event: Event) {
      const target = event.target as HTMLInputElement;
      if (target.files && target.files.length > 0) {
        this.file = target.files[0];
      }
    },
    async uploadImage() {
      if (this.restaurant && this.file) {
        const formData = new FormData();
        formData.append('file', this.file);
        await uploadRestaurantImage(this.restaurant.id, formData).then(response => {
          this.$notify({
            type: 'success',
            text: 'Obrázek přidán.'
          })
          this.findRestaurant();
        }).catch(e => {
          this.$notify({
            type: 'error',
            text: 'Nepovedlo se přidat obrázek.'
          })
        })
        this.file = null;
      }
    },
    async deleteImage(uuid: string){
      await deleteRestaurantImage(uuid).then(response=>{
        this.$notify({
          type: 'success',
          text: 'Obrázek odstraněn.'
        })
        this.findRestaurant();
      }).catch(e => {
        this.$notify({
          type: 'error',
          text: 'Nepovedlo se odstranit obrázek.'
        })
        console.log(e);
      })
    },
    async findReservations() {
      if (this.restaurant) {
        getReservationsByRestaurantId(this.restaurant.id).then(response => {
          this.reservations = response;
          console.log(response);
        }).catch(e => {
          console.log(e);
        })
      }
    },
    async doAcceptReservation(reservation: Reservation) {
      if (this.restaurant) {
        acceptReservation(this.restaurant.id, reservation.id).then(response => {
          this.findReservations();
          this.$notify({
            type: 'success',
            text: 'Rezervace potvrzena.'
          })
        }).catch(e => {
          this.$notify({
            type: 'error',
            text: 'Nepovedlo se potvrdit rezervaci.'
          })
          console.log(e);
        })
      }
    },
    async doCancelReservation(reservation: Reservation) {
      if (this.restaurant) {
        cancelReservation(this.restaurant.id, reservation.uuid).then(response => {
          this.findReservations();
          this.$notify({
            type: 'success',
            text: 'Rezervace zrušena.'
          })
        }).catch(e => {
          this.$notify({
            type: 'error',
            text: 'Nepovedlo se zrušit rezervaci.'
          })
          console.log(e);
        })
      }
    }
  },
  async created() {
    await this.findRestaurant();
    await this.findReservations();
    // await this.findRestaurantAddress();
  }
}
</script>
