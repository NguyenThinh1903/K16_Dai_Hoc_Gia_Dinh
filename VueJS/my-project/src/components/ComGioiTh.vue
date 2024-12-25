<template>
  <div class="about">
    <h1>About Books Stores</h1>

    <section class="about-section">
      <h2>Welcome to Books Stores</h2>
      <p>
        A bookstore is a space that offers a wide variety of books to meet the reading and learning needs of customers. 
        Here, you can find books in various genres such as literature,
         science, education, self-development, children's books, and specialized topics.
          A bookstore is not only a place for buying books but also an inspiring environment that sparks a passion for reading and learning. 
          With a comfortable atmosphere and diverse choices,
         the bookstore makes it easy for customers to find their favorite books, helping them enhance their knowledge and personal growth.
      </p>
    </section>

    <section class="mission-section">
      <h2>Our Mission</h2>
      <p>
        Our mission is to inspire a love for reading by providing a diverse selection of books that educate, entertain, and enrich.
       We aim to create a welcoming space where knowledge is accessible, fostering personal growth and a lifelong passion for learning.
      </p>
    </section>

    <section class="contact-section">
      <h2>Contact Us</h2>
      <p>
        Have questions or need assistance? Feel free to reach out to us:
      </p>
      <ul>
        <li>Email: <a href="mailto:contact@BookStore.com">contact@BooksStores.com</a></li>
        <li>Phone: +1 (800) 123-4567</li>
        <li>Location: 1234 Books Street, Ho Chi Minh City, Vietnam</li>
      </ul>
    </section>

    <section class="social-media">
      <h2>Follow Us</h2>
      <p>
        Stay connected with us on social media for the latest updates and
        promotions:
      </p>
      <ul>
        <li><a href="https://facebook.com/bookstores" target="_blank">Facebook</a></li>
        <li><a href="https://twitter.com/bookstores" target="_blank">Twitter</a></li>
        <li><a href="https://instagram.com/boookstores" target="_blank">Instagram</a></li>
      </ul>
    </section>
  </div>
</template>

<script>
export default {
  name: "About BooksStores",
  mounted() {
    document.title = "Giới thiệu | BooksStores";
  },
};
</script>

<style scoped>
/* General Styles */
.about {
  font-family: Arial, sans-serif;
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
  font-size: 2.5rem;
}

section {
  margin-bottom: 30px;
}

h2 {
  color: #4CAF50;
  margin-bottom: 10px;
  font-size: 1.75rem;
}

ul {
  list-style: none;
  padding: 0;
}

ul li {
  margin-bottom: 8px;
}

a {
  color: #007BFF;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

.contact-section ul, .social-media ul {
  margin-top: 10px;
}
</style>