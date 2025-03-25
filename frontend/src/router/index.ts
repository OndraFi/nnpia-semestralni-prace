import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/LoginView.vue";
import SignUpView from "@/views/SignUpView.vue";
import DashboardView from "@/views/DashboardView/DashboardView.vue";
import {useAuthStore} from "@/stores/authStore.ts";
import RestaurantEditView from "@/views/DashboardView/RestaurantEditView.vue";
import RestaurantsView from "@/views/RestaurantsView.vue";
import RestaurantDetailView from "@/views/RestaurantDetailView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/sign-up',
      name: 'sign-up',
      component: SignUpView
    },
    {
      path: '/restaurants',
      name: 'restaurants',
      component: RestaurantsView
    },
    {
      path: '/restaurant/:id',
      name: 'restaurant-detail',
      component: RestaurantDetailView
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: DashboardView
    },
    {
      path:'/dashboard/restaurant/:id',
      name: 'restaurant-edit',
      component: RestaurantEditView
    }
  ],
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  if ((to.name === 'login' || to.name === 'sign-up') && authStore.isLoggedIn )
    next({name: 'dashboard'})
  else if(to.name === 'dashboard' && !authStore.isLoggedIn)
    next({name:'login'})
  else next()
})

export default router
