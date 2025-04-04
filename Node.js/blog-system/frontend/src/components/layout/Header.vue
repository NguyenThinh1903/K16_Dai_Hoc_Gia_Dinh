// C:\thangs\NODE_JS\blog-system_backup\blog-system\frontend\src\components\layout\Header.vue
<template>
  <header class="bg-mint-500 shadow-md">
    <div class="container mx-auto max-w-5xl px-4 py-4">
      <nav class="flex flex-col md:flex-row justify-between items-center gap-4">
        <!-- Left Side: Logo and Home Link -->
        <div class="flex items-center space-x-4">
          <router-link to="/" class="text-2xl font-bold text-white">Blog System</router-link>
          <router-link to="/" class="text-white hover:text-mint-100 transition hidden md:inline-block">Home</router-link>
        </div>

        <!-- Center: Search Bar -->
        <div class="w-full md:w-1/2 lg:w-1/3">
          <div class="relative">
            <input
              type="text"
              v-model="searchQuery"
              @keyup.enter="performSearch"
              placeholder="Search posts..."
              class="w-full px-4 py-2 rounded-md text-gray-700 bg-mint-50 focus:outline-none focus:ring-2 focus:ring-mint-300 placeholder-gray-500"
            />
            <button @click="performSearch" class="absolute right-0 top-0 h-full px-3 text-gray-500 hover:text-mint-600">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </button>
          </div>
        </div>

        <!-- Right Side: Auth Links/Dropdown -->
        <div class="flex items-center space-x-4">
          <router-link v-if="!isAuthenticated" to="/login" class="inline-block px-4 py-2 text-white border border-white rounded-md hover:bg-mint-100 hover:text-mint-600 transition">
            Sign In
          </router-link>
          <router-link v-if="!isAuthenticated" to="/register" class="inline-block px-4 py-2 bg-white text-mint-500 rounded-md hover:bg-mint-100 hover:text-mint-600 transition">
            Sign Up
          </router-link>
          <div v-else class="relative">
            <button
              @click="toggleDropdown"
              class="flex items-center space-x-1 text-white hover:text-mint-100 focus:outline-none"
            >
              <span>Account</span>
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
              </svg>
            </button>
            <div
              v-show="isDropdownOpen"
              class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-10"
            >
              <router-link to="/profile" class="block px-4 py-2 text-gray-700 hover:bg-mint-100">Profile</router-link>
              <router-link v-if="isAdmin" to="/admin/dashboard" class="block px-4 py-2 text-gray-700 hover:bg-mint-100">Dashboard</router-link>
              <button @click="logout" class="block w-full text-left px-4 py-2 text-gray-700 hover:bg-mint-100">Sign Out</button>
            </div>
          </div>
        </div>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { computed, ref, onMounted, onUnmounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();
const isDropdownOpen = ref(false);
const searchQuery = ref(''); // Added for search input

const isAuthenticated = computed(() => authStore.isAuthenticated);
const isAdmin = computed(() => authStore.isAdmin);

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const closeDropdown = (e) => {
  if (!e.target.closest('.relative')) {
    isDropdownOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', closeDropdown);
});

onUnmounted(() => {
  document.removeEventListener('click', closeDropdown);
});

const logout = async () => {
  await authStore.logout();
  isDropdownOpen.value = false;
  router.push('/login');
};

// Function to perform search
const performSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({ name: 'home', query: { search: searchQuery.value } });
  } else {
     // If search is cleared, go to home without query
    router.push({ name: 'home' });
  }
  // Optional: Clear input after search?
  // searchQuery.value = '';
};
</script>

<style scoped>
/* Add any specific styles if needed */
</style>