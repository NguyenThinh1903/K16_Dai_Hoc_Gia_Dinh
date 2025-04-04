<template>
  <div class="register-page py-12 bg-gray-50"> <!-- Add background to the page if needed, matching main bg -->
    <div class="max-w-md mx-auto"> <!-- Container to center the form -->
      <div class="bg-mint-500 p-6 rounded-t-lg shadow-md text-center"> <!-- Mint banner, rounded top -->
        <h1 class="text-2xl font-bold text-white">Create Account</h1>
        <p class="text-mint-100">Join our community today</p>
      </div>
      <div class="bg-white rounded-b-lg shadow-md p-6"> <!-- White background for form content, rounded bottom -->
        <RegisterForm />
      </div>
    </div>
  </div>
</template>

<script setup>
import RegisterForm from '@/components/auth/RegisterForm.vue';
</script>