<template>
    <footer>
         <div class="footer-container">
             <div class="footer-links">
                <router-link to="/">Trang Chủ</router-link>
                <router-link to="/gioithieu">Giới Thiệu</router-link>
                <router-link to="/lienhe">Liên Hệ</router-link>
             </div>
             <div class="footer-social">
                 <a href="#"><i class="fab fa-facebook-f"></i> Facebook</a>
                 <a href="#"><i class="fab fa-twitter"></i> Twitter</a>
                 <a href="#"><i class="fab fa-instagram"></i> Instagram</a>
             </div>
             <p>&copy; 2024 Your Website. All Rights Reserved.</p>
         </div>
     </footer>
 </template>

 
 <script>
 export default {
 
 }
 </script>
 
 <style scoped>
 * {
     margin: 0;
     padding: 0;
     box-sizing: border-box;
 }
 
 footer {
     background-color: #003c47; 
     color: #fff; 
     padding: 40px 20px;
     text-align: center;
     font-family: 'Arial', sans-serif;
     transition: background-color 0.3s ease, color 0.3s ease;
 }
 
 body.dark-mode footer {
     background-color: #222;
     color: #ddd;
 }
 
 .footer-container {
     display: flex;
     flex-direction: column;
     align-items: center;
     max-width: 1200px;
     margin: 0 auto;
     padding: 0 20px;
 }
 
 .footer-links {
     display: flex;
     flex-wrap: wrap;
     justify-content: center;
     gap: 20px;
     margin-bottom: 20px;
 }
 
 .footer-links a {
     color: #fff; 
     font-size: 16px;
     text-decoration: none;
     font-weight: 500;
     transition: color 0.3s ease, transform 0.2s ease;
 }

 </style>
 