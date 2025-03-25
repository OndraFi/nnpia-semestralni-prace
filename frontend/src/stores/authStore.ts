import {defineStore} from "pinia";
import {login, signUp} from "@/api/auth.ts";
import {jwtDecode} from 'jwt-decode';

interface TokenPayload {
  exp: number;
  sub: string;
}

export const useAuthStore = defineStore('authStore', {
  state: () => ({
    token: "",
    email: "",
  }),
  getters: {
    isLoggedIn(state): boolean {
      if (!state.token) return false;
      try {
        const decoded = jwtDecode<TokenPayload>(state.token);
        const now = Date.now();
        const exp = decoded.exp * 1000;
        const remainingMs = exp - now;
        console.log(`⏳ Token vyprší za ${Math.floor(remainingMs / 1000)} sekund (${Math.round(remainingMs / 60000)} minut)`);

        return remainingMs > 0;
      } catch {
        return false;
      }
    }
  },
  actions: {
    async loginUser(email: string, password: string) {
      return login(email, password).then((response) => {
        this.token = response.data.token;
        this.email = response.data.email;
      }).catch((error) => {
        console.log(error)
        throw error;
      });
    },
    async signUpUser(email: string, password: string) {
      return signUp(email, password).then((response) => {
        console.log(response);
      }).catch((error) => {
        console.log(error)
        throw error;
      });
    },
    logout() {
      this.token = "";
    }
  },
  persist: true,
})
