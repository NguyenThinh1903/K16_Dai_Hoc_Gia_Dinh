// C:\thangs\NODE_JS\blog-system_backup\blog-system\frontend\src\views\LoginView.vue
<template>
  <div class="login-page py-12 bg-gray-50">
    <div class="max-w-md mx-auto">
      <div class="bg-mint-500 p-6 rounded-t-lg shadow-md text-center">
        <!-- Make the h1 clickable for easter egg -->
        <h1
          class="text-2xl font-bold text-white cursor-pointer"
          @click="handleWelcomeClick"
        >
          Welcome Back
        </h1>
        <p class="text-mint-100">Sign in to access your account</p>
      </div>
      <div class="bg-white rounded-b-lg shadow-md p-6">
        <LoginForm />
      </div>
    </div>
  </div>
</template>

<script setup>
import LoginForm from '@/components/auth/LoginForm.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const welcomeClickCount = ref(0); // Initialize click counter

const handleWelcomeClick = () => {
  welcomeClickCount.value++; // Increment counter on each click
  if (welcomeClickCount.value >= 3) {
    router.push('/owner-login'); // Redirect to owner login after 3 clicks
  }
};
</script>