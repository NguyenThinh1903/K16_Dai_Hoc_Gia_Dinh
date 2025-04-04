<template>
  <div class="login-form">
    <h2>User Login</h2>
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
        {{ isSubmitting ? 'Logging in...' : 'Login' }}
      </button>
      
      <div class="actions">
        <router-link to="/register">Don't have an account? Register</router-link>
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
    
    // Call the login action from auth store
    await authStore.login({
      email: email.value,
      password: password.value
    });
    
    // Redirect based on user role
    if (authStore.user && authStore.user.role === 'admin') {
      router.push('/admin/dashboard');
    } else {
      router.push('/');
    }
  } catch (error) {
    console.error('Login error:', error);
    
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
.login-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
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
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

button:hover {
  background-color: #45a049;
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