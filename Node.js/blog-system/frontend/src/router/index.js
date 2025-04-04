import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth'; // Corrected import

import HomeView from '@/views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';
import RegisterView from '@/views/RegisterView.vue';
import ProfileView from '@/views/ProfileView.vue';
import PostView from '@/views/PostView.vue';
import DashboardView from '@/views/admin/DashboardView.vue';
import UserManagementView from '@/views/admin/UserManagementView.vue';
import OwnerLoginView from '@/views/OwnerLoginView.vue';
import CreatePostView from '@/views/CreatePostView.vue'; // Import
import EditPostView from '@/views/EditPostView.vue';   // Import
import NotFoundView from '@/views/NotFoundView.vue';
import AdminRegisterView from '@/views/admin/AdminRegisterView.vue';
import AdminUserProfileView from '@/views/admin/AdminUserProfileView.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { requiresGuest: true }
    },
    {
      path: '/owner-login',
      name: 'ownerLogin',
      component: OwnerLoginView,
      meta: { requiresGuest: true }
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { requiresGuest: true }
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: { requiresAuth: true }
    },
    {
      path: '/posts/:id',
      name: 'post',
      component: PostView,
       props: true,
    },
    {
        path: '/create-post',
        name: 'createPost',
        component: CreatePostView,
        meta: { requiresAuth: true, requiresAdmin: true }
    },
     {
        path: '/edit-post/:id',
        name: 'editPost',
        component: EditPostView,
        meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/admin/dashboard',
      name: 'adminDashboard',
      component: DashboardView,
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/admin/users',
      name: 'userManagement',
      component: UserManagementView,
      meta: { requiresAuth: true, requiresAdmin: true }
    },
     {
      path: '/:pathMatch(.*)*', // Catch-all route for 404s
      name: 'notFound',
      component: NotFoundView
    },
    {
        path: '/admin/register',
        name: 'adminRegister',
        component: AdminRegisterView,
        meta: { requiresAuth: true, requiresOwner: true } // Only owner can register admins
    },
     {
        path: '/admin/users/:userId/profile',
        name: 'adminUserProfile',
        component: AdminUserProfileView,
        meta: { requiresAuth: true, requiresAdmin: true },
        props: true
    },
  ]
});

// Navigation guard
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();

  if (authStore.isAuthenticated && !authStore.user) {
    try {
      await authStore.fetchUserProfile();
    } catch (error) {
      authStore.logout();
    }
  }

  if (to.meta.requiresGuest && authStore.isAuthenticated) {
    return next('/');
  }

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return next('/login');
  }

  if (to.meta.requiresAdmin && !authStore.isAdmin) {
    return next('/');
  }
  if (to.meta.requiresOwner && authStore.user.role !== 'owner') {
       return next('/'); // Redirect to a suitable page
    }

  next();
});

export default router;