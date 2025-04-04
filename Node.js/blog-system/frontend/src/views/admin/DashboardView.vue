// .\src\views\admin\DashboardView.vue
<template>
    <div class="admin-dashboard">
      <h1 class="text-3xl font-bold mb-6">Admin Dashboard</h1>
      <p>Welcome to the admin dashboard. You can manage users and posts here.</p>
      <div class="grid grid-cols-2 gap-4 mt-6">
        <router-link to="/admin/users" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-4 px-6 rounded">
          Manage Users
        </router-link>
         <router-link to="/admin/register" class="bg-green-500 hover:bg-green-700 text-white font-bold py-4 px-6 rounded">
            Register Admin (Owner Only)
        </router-link>
      </div>
    </div>
</template>