<template>
  <div class="" v-if="restaurant" @submit.prevent="sendReservation">
    <form class="p-3" v-if="!reservation">
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
          <div v-if="!restaurant.usesDesks" class="form-floating mb-3">
            <input type="number"
                   @input="searchAvailableTimesDebounce"
                   min="1"
                   id="numberOfPople"
                   class="form-control"
                   :max="restaurant.capacity"
                   placeholder="Počet lidí" v-model="numberOfPeople">
            <label for="numberOfPeople">Počet lidí (max {{ restaurant.capacity }})</label>
          </div>
          <div class="" v-else>
            <div class="dropdown w-100" v-if="selectedDesk">
              <button class="btn btn-outline-primary dropdown-toggle dropdown w-100"
                      style="height: 58px; border-color: #dee2e6"
                      type="button"
                      data-bs-toggle="dropdown" aria-expanded="false">
                {{ selectedDesk.name }} - pro {{ selectedDesk.capacity }}
              </button>
              <ul class="dropdown-menu w-100">
                <li v-for="desk in desks" :key="desk.id">
                  <a class="dropdown-item" href="#" @click="changeDesk(desk)">{{ desk.name }} -
                    pro {{ desk.capacity }}</a>
                </li>
              </ul>
            </div>
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
                   pattern="[0-9]{9+}"
                   required
                   v-model="phone"
                   placeholder="">
            <label for="phone">Telefon (min 9 čísel)</label>
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

      <input v-if="!sending" type="submit" class="btn btn-primary w-100"
             :disabled="selectedTime === null" value="REZERVOVAT!">
      <div v-else class="text-center">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
    </form>
    <div v-else class="p-3 shadow">
      <h2>Vašer rezervace na jméno <b>{{ reservation.reservationName }}</b> čeká na potvrzení!</h2>
      <p>Vyčkejte na email a rezervaci potvrdťke kliknutím na link.</p>
    </div>
  </div>

</template>
<script lang="ts">
import {
  createReservation,
  getAvailableTimes, getAvailableTimesForDesk, type Reservation, type ReservationRequest,
  type ReservationTimeRange
} from "@/api/reservation.ts";
import {debounce} from "lodash";
import {findRestaurantById, type Restaurant} from "@/api/restaurant.ts";
import ReservationTimePicker from "@/components/ReservationTimePicker.vue";
import {type Desk, getDesksByRestaurantId} from "@/api/desk.ts";

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
      sending: false,
      desks: null as Array<Desk> | null,
      selectedDesk: null as Desk | null,
    }
  },
  methods: {
    changeDesk(desk: Desk) {
      this.selectedDesk = desk;
      this.getAvailableTimes();
    },
    async findRestaurant() {
      await findRestaurantById(this.$route.params.id).then(restaurant => {
        this.restaurant = restaurant;
      })
    },
    async getAvailableTimes() {
      this.availableTimes = null;
      console.log(this.reservationDate);
      if (this.restaurant?.usesDesks && this.selectedDesk) {
        await getAvailableTimesForDesk(this.$route.params.id, this.reservationDate, this.selectedDesk.id).then(response => {
          console.log(response);
          this.availableTimes = response;
        })
      } else {
        await getAvailableTimes(this.$route.params.id, this.reservationDate, this.numberOfPeople).then(response => {
          console.log(response);
          this.availableTimes = response;
        })
      }
    },
    searchAvailableTimesDebounce() {
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
      if (this.restaurant?.usesDesks)
        this.sendReservationForDesk();
      else
        this.sendReservationForNumberOfPeople();
    },
    sendReservationForDesk() {
      if (this.restaurant && this.selectedTime && this.selectedDesk && !this.sending) {
        this.sending = true;
        const reservationRequest: ReservationRequest = {
          startTime: this.reservationDate + "T" + this.selectedTime.start,
          endTime: this.reservationDate + "T" + this.selectedTime.end,
          email: this.email,
          phone: this.phone,
          reservationName: this.reservationName,
          numberOfPeople: this.selectedDesk.capacity,
          desk: this.selectedDesk,
        }
        createReservation(this.restaurant.id, reservationRequest).then(reservation => {
          this.reservation = reservation;
          console.log(reservation);
          this.sending = false;
        }).catch(e => {
          this.sending = false;
          console.log(e);
        })
      }
    },
    sendReservationForNumberOfPeople() {
      if (this.restaurant && this.selectedTime && !this.sending) {
        this.sending = true;
        const reservationRequest: ReservationRequest = {
          startTime: this.reservationDate + "T" + this.selectedTime.start,
          endTime: this.reservationDate + "T" + this.selectedTime.end,
          email: this.email,
          phone: this.phone,
          reservationName: this.reservationName,
          numberOfPeople: this.numberOfPeople,
          desk: null,
        }
        createReservation(this.restaurant.id, reservationRequest).then(reservation => {
          this.reservation = reservation;
          console.log(reservation);
          this.sending = false;
        }).catch(e => {
          this.sending = false;
          console.log(e);
        })
      }
    },
    async findDesks() {
      if (this.restaurant)
        await getDesksByRestaurantId(this.restaurant.id).then(desks => {
          this.desks = desks;
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
  async mounted() {
    await this.findRestaurant();
    await this.findDesks();

    console.log("restaurant", this.restaurant)
    if (this.restaurant?.usesDesks && this.desks) {
      console.log("selecting desk")
      this.selectedDesk = this.desks[0]
      console.log(this.selectedDesk)
    }

    await this.getAvailableTimes();
  }
}
</script>
