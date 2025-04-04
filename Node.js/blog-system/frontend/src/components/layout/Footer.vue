// .\src\components\layout\Footer.vue
<template>
  <footer class="bg-mint-600 text-white p-6 mt-auto">
    <div class="container mx-auto max-w-5xl">
      <div class="flex flex-col md:flex-row justify-between items-center">
        <div class="mb-4 md:mb-0">
          <p class="font-medium">© {{ new Date().getFullYear() }} Blog System</p>
          <p class="text-sm text-mint-100">Sharing thoughts and ideas</p>
        </div>
        <div class="flex space-x-4">
          <a href="#" class="hover:text-mint-200 transition">About</a>
          <a href="#" class="hover:text-mint-200 transition">Contact</a>
          <a href="#" class="hover:text-mint-200 transition">Privacy</a>
        </div>
      </div>
    </div>
  </footer>
</template>