<template>
  <div class="" v-if="restaurant" @submit.prevent="sendReservation">
    <form class="shadow p-3" v-if="!reservation">
      <div class="row">
        <div class="col-12">
          <div class="mb-3">
            <label for="date">Datum rezervace</label>
            <input type="date"
                   id="date"
                   class="form-control"
                   :min="new Date().toISOString().split('T')[0]" @change="getAvailableTimes"
                   v-model="reservationDate">
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-floating mb-3">
            <input type="number"
                   @input="searchAvailableTimesDebounce"
                   min="1"
                   id="numberOfPople"
                   class="form-control"
                   :max="restaurant.capacity"
                   placeholder="Počet lidí" v-model="numberOfPeople">
            <label for="numberOfPeople">Počet lidí (max {{ restaurant.capacity }})</label>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-floating mb-3">
            <input type="text"
                   id="reservatioinName"
                   class="form-control"
                   v-model="reservationName"
                   minlength="3"
                   required
                   placeholder="Rezervace na jméno">
            <label for="reservatioinName">Rezervace na jméno</label>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-floating mb-3">
            <input type="tel"
                   id="phone"
                   class="form-control"
                   required
                   v-model="phone"
                   placeholder="">
            <label for="phone">Telefon</label>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-floating mb-3">
            <input type="email"
                   id="email"
                   class="form-control"
                   v-model="email"
                   required
                   placeholder="Email">
            <label for="email">Email</label>
          </div>
        </div>
      </div>
      <div class="" v-if="availableTimes">
        <ReservationTimePicker :restaurant="restaurant" :available-times="availableTimes"
                               @time-selected="handleTimeSelected"/>
      </div>
      <div v-else class="text-center">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
      <input type="submit" class="btn btn-primary w-100" :disabled="selectedTime === null" value="REZERVOVAT!">
    </form>
    <div v-else class="p-3 shadow">
      <h2>Vašer rezervace na jméno <b>{{reservation.reservationName}}</b> čeká na potvrzení!</h2>
      <p>Vyčkejte na email a rezervaci potvrdťke kliknutím na link.</p>
    </div>
  </div>

</template>
<script lang="ts">
import {
  createReservation,
  getAvailableTimes, type Reservation, type ReservationRequest,
  type ReservationTimeRange
} from "@/api/reservation.ts";
import {debounce} from "lodash";
import {findRestaurantById, type Restaurant} from "@/api/restaurant.ts";
import ReservationTimePicker from "@/components/ReservationTimePicker.vue";

export default {
  components: {ReservationTimePicker},
  data() {
    return {
      reservationDate: new Date().toISOString().split('T')[0],
      numberOfPeople: 1,
      phone: '',
      email: '',
      reservationName: '',
      availableTimes: null as Array<ReservationTimeRange> | null,
      restaurant: null as Restaurant | null,
      selectedTime: null as ReservationTimeRange | null,
      reservation: null as Reservation | null,
    }
  },
  methods: {
    findRestaurant() {
      findRestaurantById(this.$route.params.id).then(restaurant => {
        this.restaurant = restaurant;
      })
    },
    getAvailableTimes() {
      this.availableTimes = null;
      console.log(this.reservationDate);
      getAvailableTimes(this.$route.params.id, this.reservationDate, this.numberOfPeople).then(response => {
        console.log(response);
        this.availableTimes = response;
      })
    },
    searchAvailableTimesDebounce(){
      this.availableTimes = null;
      this.search();
    },
    search: debounce(function () {
      this.getAvailableTimes();
    }, 300),
    handleTimeSelected(selectedTime: ReservationTimeRange | null) {
      this.selectedTime = selectedTime;
    },
    sendReservation() {
      if (this.restaurant && this.selectedTime) {
        const reservationRequest: ReservationRequest = {
          startTime: this.reservationDate + "T" + this.selectedTime.start,
          endTime: this.reservationDate + "T" + this.selectedTime.end,
          email: this.email,
          phone: this.phone,
          reservationName: this.reservationName,
          numberOfPeople: this.numberOfPeople
        }
        createReservation(this.restaurant.id, reservationRequest).then(reservation => {
          this.reservation = reservation;
          console.log(reservation);
        }).catch(e => {
          console.log(e);
        })
      }
    }
  },
  mounted() {
    this.findRestaurant();
    this.getAvailableTimes();
  }
}
</script>
