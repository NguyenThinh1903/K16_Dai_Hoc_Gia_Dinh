// C:\thangs\NODE_JS\blog-system_backup\blog-system\frontend\src\views\admin\AdminRegisterView.vue
<template>
  <div class="admin-register-page py-12 bg-gray-50">
    <div class="max-w-md mx-auto">
      <div class="bg-mint-500 p-6 rounded-t-lg shadow-md text-center">
        <h1 class="text-2xl font-bold text-white">Register New Admin</h1>
        <p class="text-mint-100">Create an administrator account (Owner Access Required).</p>
      </div>
      <div class="bg-white rounded-b-lg shadow-md p-6">
        <RegisterForm />
      </div>
    </div>
  </div>
</template>

<script setup>
import RegisterForm from '@/components/auth/RegisterForm.vue';
</script>