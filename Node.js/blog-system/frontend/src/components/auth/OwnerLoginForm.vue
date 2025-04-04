<template>
    <div class="owner-login-form">
      <h2>Blog Owner Login</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="email">Email</label>
          <input 
            id="email" 
            v-model="email" 
            type="email" 
            placeholder="Enter your email"
            required
          />
          <p v-if="errors.email" class="error-message">{{ errors.email }}</p>
        </div>
        
        <div class="form-group">
          <label for="password">Password</label>
          <input 
            id="password" 
            v-model="password" 
            type="password" 
            placeholder="Enter your password"
            required
          />
          <p v-if="errors.password" class="error-message">{{ errors.password }}</p>
        </div>
        
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        
        <button type="submit" :disabled="isSubmitting">
          {{ isSubmitting ? 'Logging in...' : 'Login as Owner' }}
        </button>
        
        <div class="actions">
          <router-link to="/login">Regular user login</router-link>
        </div>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { useAuthStore } from '@/stores/auth';
  
  const router = useRouter();
  const authStore = useAuthStore();
  
  // Form data
  const email = ref('');
  const password = ref('');
  const isSubmitting = ref(false);
  const errorMessage = ref('');
  const errors = ref({
    email: '',
    password: ''
  });
  
  const handleSubmit = async () => {
    // Reset errors
    errors.value = {
      email: '',
      password: ''
    };
    errorMessage.value = '';
    
    try {
      isSubmitting.value = true;
      
      // Call the login action from auth store with owner flag
      await authStore.loginAsOwner({
        email: email.value,
        password: password.value
      });
      
      // Redirect to admin dashboard after successful login
      router.push('/admin/dashboard');
    } catch (error) {
      console.error('Owner login error:', error);
      
      // Handle validation errors
      if (error.response && error.response.data && error.response.data.errors) {
        const validationErrors = error.response.data.errors;
        validationErrors.forEach(err => {
          if (err.path === 'email') {
            errors.value.email = err.msg;
          } else if (err.path === 'password') {
            errors.value.password = err.msg;
          }
        });
      } else if (error.response && error.response.data && error.response.data.message) {
        // Handle general error message
        errorMessage.value = error.response.data.message;
      } else {
        // Fallback error message
        errorMessage.value = 'Login failed. Please try again.';
      }
    } finally {
      isSubmitting.value = false;
    }
  };
  </script>
  
  <style scoped>
  .owner-login-form {
    padding: 20px;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    background-color: #f9f9f9;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
  }
  
  input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  button {
    width: 100%;
    padding: 10px;
    background-color: #2c3e50;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-top: 10px;
  }
  
  button:hover {
    background-color: #1a2530;
  }
  
  button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  
  .error-message {
    color: red;
    font-size: 0.85rem;
    margin-top: 5px;
  }
  
  .actions {
    margin-top: 15px;
    text-align: center;
  }
  
  a {
    color: #2c3e50;
    text-decoration: none;
  }
  
  a:hover {
    text-decoration: underline;
  }
  </style>