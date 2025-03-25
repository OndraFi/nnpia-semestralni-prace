<template>
  <div class="p-3 p-md-5">
    <div class="d-flex justify-content-start flex-wrap">
      <div v-for="(value,index) in times"
           @click="selectTime(index)"
           :key="index"
           @mouseover="hoveredIndex = index"
           @mouseleave="hoveredIndex = null"
           style="cursor: pointer"
           class="timeBlock  mt-4"
           :class="{'bg-primary hovered' : isInHoveredRange(index) || index === startTime, 'rounded-start' : index === startTime || (endTime && index === endTime+1), 'rounded-end' : index === endTime || (startTime && index === startTime-1), 'bg-secondary cursor-not-allowed' : unavailableTimesIndexes.includes(index)}">

      <small class="time muted">
      {{ value.start }}
      </small>
        <small v-if="index === times.length-1"
              class="time-end muted">
        {{ value.end }}
      </small>
        <span v-if="hoveredIndex === index && startTime === null"
              class="time-tooltip-sm start text-black">{{ times[hoveredIndex].start }}</span>
        <span v-if="hoveredIndex === index && startTime !== null"
              class="time-tooltip-sm end" :class="{'text-black' : !isInHoveredRange(index)}">{{ times[hoveredIndex].end }}</span>

        <span v-if="startTime === index"
              class="time-tooltip-sm start">{{ times[startTime].start }}</span>
        <span v-if="endTime === index"
              class="time-tooltip-sm end">{{ times[endTime].end }}</span>
      </div>
    </div>
    <div class="text-center">{{ selectedTime }}</div>
  </div>
</template>
<script lang="ts">
import type {ReservationTimeRange} from "@/api/reservation.ts";

export default {
  props: {
    availableTimes: {
      required: true,
      type: Array<ReservationTimeRange>
    },
    restaurant: {
      required: true,
    },
    timeRange: {
      default: 15
    }
  },
  data() {
    const times: Array<ReservationTimeRange> = [];
    return {
      times,
      startTime: null as number | null,
      endTime: null as number | null,
      hoveredIndex: null as number | null,
      unavailableTimesIndexes: [] as Array<number>,
    }
  },
  methods: {
    /**
     *
     * @param a
     * @param b
     * @return : 1 - a je větší, -1 b je větší, 0 - časy jsou stejný
     */
    compareTwoReservationTimes(a: string, b: string) {
      const aSplits = a.split(':');
      const bSplits = b.split(':');
      if (aSplits[0] > bSplits[0])
        return 1;
      if (aSplits[0] < bSplits[0])
        return -1;
      if (aSplits[1] > bSplits[1])
        return 1; // a je větší
      if (aSplits[1] < bSplits[1])
        return -1; // b je větší
      return 0; // jsou stejný
    },
    parseTime(timeStr: string): Date {
      const [hours, minutes] = timeStr.split(":").map(Number);
      const date = new Date();
      date.setHours(hours, minutes, 0, 0);
      return date;
    },
    formatTime(date: Date): string {
      return date.toTimeString().slice(0, 5); // "HH:mm"
    },
    setupTimes() {
      const open = this.parseTime(this.restaurant.openTime);
      const close = this.parseTime(this.restaurant.closeTime);
      const interval = this.timeRange; // např. 15 minut

      const times: { start: string; end: string }[] = [];

      let current = new Date(open.getTime());
      while (current < close) {
        const start = this.formatTime(current);
        const endDate = new Date(current.getTime() + interval * 60 * 1000);
        if (endDate > close) break;

        const end = this.formatTime(endDate);
        times.push({start, end});

        current = endDate;
      }

      this.times = times;
    },
    setupUnavailableTimes() {
      this.unavailableTimesIndexes = [];
      this.times.forEach((time, index) => {
        if (!this.isAvailable(index))
          this.unavailableTimesIndexes.push(index);
      })
    },
    selectTime(index: number) {
      if (this.unavailableTimesIndexes.includes(index))
        return;
      if (this.startTime === null) {
        this.startTime = index;
      } else if (this.endTime === null) {
        const isUnavailableInBetween = this.unavailableTimesIndexes.filter(_index => {
          if (this.startTime !== null && _index > this.startTime && _index < index)
            return true
        }).length > 0;
        if (this.startTime <= index && !isUnavailableInBetween){
          this.endTime = index
          this.sendSelectedTimeToParent();
        }
      } else {
        this.startTime = null;
        this.endTime = null;
        this.sendSelectedTimeToParent();
      }
    },
    isInHoveredRange(index: number) {
      if (this.startTime !== null && this.endTime !== null && index >= this.startTime && index <= this.endTime)
        return true;
      if (this.startTime !== null && this.endTime === null && this.hoveredIndex && index >= this.startTime && index <= this.hoveredIndex) {
        const isUnavailableInBetween = this.unavailableTimesIndexes.filter(_index => {
          if (this.startTime !== null && _index > this.startTime && _index <= index)
            return true;
        }).length > 0;
        return !isUnavailableInBetween;
      }
      return false;
    },
    isAvailable(index: number): boolean {
      const time = this.times[index];
      for (let i = 0; i < this.availableTimes.length; i++) {
        const compareStartTime = this.compareTwoReservationTimes(time.start, this.availableTimes[i].start);
        const compareEndTime = this.compareTwoReservationTimes(time.end, this.availableTimes[i].end);
        if ((compareStartTime === 0 || compareStartTime === 1) && (compareEndTime === 0 || compareEndTime === -1)) {
          return true;
        }
      }
      return false;
    },
    sendSelectedTimeToParent() {
      if (this.startTime !== null && this.endTime !== null) {
        const selectedTime: ReservationTimeRange = {
          start: this.times[this.startTime].start + ":00",
          end: this.times[this.endTime].end +":00"
        }
        this.$emit('time-selected', selectedTime)
      }else{
        this.$emit('time-selected', null);
      }
    }
  },
  computed: {
    selectedTime() {
      if (this.startTime === null && this.endTime === null) {
        if (this.hoveredIndex !== null)
          return this.times[this.hoveredIndex].start + ' | --:--';
      }
      if (this.startTime !== null && this.endTime === null) {
        if (this.hoveredIndex !== null)
          return this.times[this.startTime].start + ' | ' + this.times[this.hoveredIndex].end;
        else
          return this.times[this.startTime].start + ' | --:--';
      }
      if (this.startTime !== null && this.endTime !== null)
        return this.times[this.startTime].start + ' | ' + this.times[this.endTime].end;

      return '--:-- | --:--';
    }
  },
  created() {
    this.setupTimes();
    this.setupUnavailableTimes();
  },
  mounted() {
    this.setupUnavailableTimes();
  }
}
</script>
<style scoped>
.timeBlock {
  width: 50px;
  height: 50px;
  position: relative;
  background-color: #EBF4F6;
  user-select: none;
  .time {
    position: absolute;
    top: -20px;
    left:-50%;
  }
  .time-end{
    position: absolute;
    top: -20px;
    left:+50%;
  }
  .time-tooltip{
    position: absolute;
    top: 25%;
  }
  .time-tooltip-sm{
    position: absolute;
    font-size: 14px;
    color: white;
  }
  .start{
    top: 0%;
    left: 5%;
  }
  .end{
    right: 5%;
    bottom: 0%;
  }
}

.hovered{
  .time-tooltip{
    color: white;
  }
}

.cursor-not-allowed {
  cursor: not-allowed;
}
</style>
