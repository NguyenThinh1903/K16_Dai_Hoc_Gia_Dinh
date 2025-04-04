import axios from 'axios';
import router from '@/router';
import { useAuthStore } from '@/stores/auth';

// Create axios instance with base URL from environment variables
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:3000',  // Correct base URL
  // Don't set a default Content-Type here as it will interfere with FormData
  withCredentials: true, // IMPORTANT: Send cookies with requests
});

// Request interceptor to add authorization token and handle content type
api.interceptors.request.use(
  config => {
    const authStore = useAuthStore(); // Access auth store *inside* the interceptor
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }

    // Only set content type if not FormData
    // Let Axios automatically set the correct multipart/form-data with boundary for FormData
    if (!(config.data instanceof FormData)) {
      config.headers['Content-Type'] = 'application/json';
    }
      // Add guest ID to headers if not authenticated
    if (!authStore.isAuthenticated) {
        let guestId = localStorage.getItem('guestId');
        if (!guestId) {
            guestId = crypto.randomUUID(); // Use a more robust method to generate a UUID, using crypto
            localStorage.setItem('guestId', guestId);
        }
        config.headers['x-visitor-id'] = guestId;
    }

    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle common errors
api.interceptors.response.use(
  response => {
    return response;
  },
    async error => { // Make this async
    const authStore = useAuthStore(); // Get auth store

    // Handle 401 Unauthorized errors (expired or invalid token)
    if (error.response && error.response.status === 401) {
      // Clear token from localStorage
      localStorage.removeItem('token');
      authStore.logout(); // Use authStore to clear user data

      // Redirect to login page if not already there
      if (router.currentRoute.value.path !== '/login') {
        router.push('/login');
      }
    }

    return Promise.reject(error);
  }
);

export default api;