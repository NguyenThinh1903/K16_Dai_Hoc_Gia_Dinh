<template>
  <div class="owner-login-page py-12 bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div v-if="hasStats" class="lg:grid lg:grid-cols-2 lg:gap-8 items-center justify-center">
        <!-- Left side: Site Statistics -->
        <div class="py-8 lg:py-16 flex justify-center">
          <div class="bg-mint-100 rounded-lg p-6 shadow-md text-mint-800 w-full max-w-md">
            <h2 class="text-2xl font-bold mb-4">Site Overview</h2>
            <div class="mb-6">
              <p class="font-semibold">Total Visitors:</p>
              <p class="text-lg">{{ totalVisitors }}</p>
            </div>
            <div class="mb-6">
              <p class="font-semibold">Registered Users:</p>
              <p class="text-lg">{{ registeredUsers }}</p>
            </div>
            <div>
              <p class="font-semibold">Total Posts:</p>
              <p class="text-lg">{{ totalPosts }}</p>
            </div>
          </div>
        </div>

        <!-- Right side: Owner Login Form -->
        <div class="py-8 lg:py-16 flex justify-center">
          <div class="bg-white rounded-lg shadow-md p-8 w-full max-w-md">
            <div class="bg-mint-500 p-6 rounded-t-lg text-center -mt-8 -mx-8">
              <h1 class="text-2xl font-bold text-white">Owner Login</h1>
              <p class="text-mint-100">Access your administration panel</p>
            </div>
            <div class="p-6">
              <OwnerLoginForm />
            </div>
          </div>
        </div>
      </div>

      <!-- Simplified layout when no stats -->
      <div v-else class="flex justify-center">
        <div class="bg-white rounded-lg shadow-md p-8 w-full max-w-md">
          <div class="bg-mint-500 p-6 rounded-t-lg text-center -mt-8 -mx-8">
            <h1 class="text-2xl font-bold text-white">Owner Login</h1>
            <p class="text-mint-100">Access your administration panel</p>
          </div>
          <div class="p-6">
            <OwnerLoginForm />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import OwnerLoginForm from '@/components/auth/OwnerLoginForm.vue';
import { ref, onMounted } from 'vue';

const totalVisitors = ref(0);
const registeredUsers = ref(0);
const totalPosts = ref(0);
const hasStats = ref(false);

const fetchSiteStats = async () => {
  try {
    const response = await fetch('/api/admin/stats');
    if (!response.ok) throw new Error('Failed to fetch stats');
    
    const data = await response.json();
    totalVisitors.value = data.totalVisitors;
    registeredUsers.value = data.registeredUsers;
    totalPosts.value = data.totalPosts;
    hasStats.value = true;
  } catch (error) {
    console.error('Error fetching site stats:', error);
    hasStats.value = false;
  }
};

onMounted(() => {
  fetchSiteStats();
});
</script>
