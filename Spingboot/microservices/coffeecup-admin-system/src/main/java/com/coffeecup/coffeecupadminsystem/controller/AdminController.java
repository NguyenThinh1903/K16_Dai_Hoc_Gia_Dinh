package com.coffeecup.coffeecupadminsystem.controller;

import com.coffeecup.coffeecupadminsystem.model.Customer;
import com.coffeecup.coffeecupadminsystem.model.Product;
import com.coffeecup.coffeecupadminsystem.repository.CustomerRepository;
import com.coffeecup.coffeecupadminsystem.repository.ProductRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    // ===========================================
    // Customer Management Methods
    // ===========================================
    @GetMapping("/customers")
    public String listCustomers(Model model) {
        log.info("Fetching customer list");
        List<Customer> customers = customerRepository.findAll();
        model.addAttribute("customers", customers);
        return "admin/customers/list";
    }

    @GetMapping("/customers/add")
    public String showAddCustomerForm(Model model) {
        log.info("Showing add customer form");
        model.addAttribute("customer", new Customer());
        model.addAttribute("pageTitle", "Add New Customer");
        return "admin/customers/form";
    }

    @GetMapping("/customers/edit/{id}")
    public String showEditCustomerForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Showing edit customer form for ID: {}", id);
        return customerRepository.findById(id)
                .map(customer -> {
                    model.addAttribute("customer", customer);
                    model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");
                    return "admin/customers/form";
                })
                .orElseGet(() -> {
                    log.warn("Customer not found for edit with ID: {}", id);
                    redirectAttributes.addFlashAttribute("errorMessage", "Customer not found with ID: " + id);
                    return "redirect:/admin/customers";
                });
    }

    @PostMapping("/customers/save")
    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        String action = (customer.getId() == null) ? "add" : "edit";
        log.info("Attempting to save customer (Action: {})", action);

        boolean isNew = (customer.getId() == null);
        if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
            customerRepository.findByEmail(customer.getEmail()).ifPresent(existingCustomer -> {
                if (isNew || !existingCustomer.getId().equals(customer.getId())) {
                    log.warn("Email duplication detected for email: {}", customer.getEmail());
                    bindingResult.rejectValue("email", "error.customer", "Email already exists.");
                }
            });
        }

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors found: {}", bindingResult.getAllErrors());
            model.addAttribute("pageTitle", isNew ? "Add New Customer" : "Edit Customer (ID: " + customer.getId() + ")");
            return "admin/customers/form";
        }

        try {
            log.info("Saving customer with ID: {}", customer.getId() == null ? "NEW" : customer.getId());
            customerRepository.save(customer);
            redirectAttributes.addFlashAttribute("successMessage", "Customer has been saved successfully!");
            return "redirect:/admin/customers";
        } catch (Exception e) {
            log.error("Error saving customer: ", e);
            model.addAttribute("pageTitle", isNew ? "Add New Customer" : "Edit Customer (ID: " + customer.getId() + ")");
            model.addAttribute("saveError", "An error occurred while saving the customer. Please check the data and try again.");
            if (e.getMessage().contains("constraint")) {
                model.addAttribute("saveError", "Failed to save. Ensure email is unique.");
                bindingResult.rejectValue("email", "error.customer", "Email might already be in use.");
            }
            return "admin/customers/form";
        }
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        log.info("Attempting to delete customer with ID: {}", id);
        try {
            if (customerRepository.existsById(id)) {
                customerRepository.deleteById(id);
                log.info("Customer deleted successfully with ID: {}", id);
                redirectAttributes.addFlashAttribute("successMessage", "Customer (ID: " + id + ") deleted successfully.");
            } else {
                log.warn("Customer not found for deletion with ID: {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Customer not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error deleting customer with ID: {}: ", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting customer (ID: " + id + "). It might be referenced elsewhere.");
        }
        return "redirect:/admin/customers";
    }

    // ===========================================
    // Product Management Methods
    // ===========================================
    @GetMapping("/products")
    public String listProducts(Model model) {
        log.info("Fetching product list");
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        if (model.containsAttribute("successMessage")) {
            log.info("Displaying success message: {}", model.getAttribute("successMessage"));
        }
        if (model.containsAttribute("errorMessage")) {
            log.warn("Displaying error message: {}", model.getAttribute("errorMessage"));
        }
        return "admin/products/list";
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        log.info("Showing add product form");
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Add New Product");
        return "admin/products/form";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Showing edit product form for ID: {}", id);
        return productRepository.findById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
                    return "admin/products/form";
                })
                .orElseGet(() -> {
                    log.warn("Product not found for edit with ID: {}", id);
                    redirectAttributes.addFlashAttribute("errorMessage", "Product not found with ID: " + id);
                    return "redirect:/admin/products";
                });
    }

    @PostMapping("/products/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult bindingResult,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        String action = (product.getMa() == null) ? "add" : "edit";
        log.info("Attempting to save product (Action: {})", action);

        boolean isNew = (product.getMa() == null);
        String oldImageFileName = product.getImageFileName();

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors found: {}", bindingResult.getAllErrors());
            model.addAttribute("pageTitle", isNew ? "Add New Product" : "Edit Product (ID: " + product.getMa() + ")");
            return "admin/products/form";
        }

        String uniqueFileName = oldImageFileName;
        if (imageFile != null && !imageFile.isEmpty()) {
            log.info("Processing new image file upload.");
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                uniqueFileName = fileName;

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    log.info("Created directory: {}", uploadPath);
                }

                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                log.info("Successfully uploaded new image to: {}", filePath);

                if (oldImageFileName != null && !oldImageFileName.isEmpty()) {
                    Path oldFilePath = uploadPath.resolve(oldImageFileName);
                    try {
                        Files.deleteIfExists(oldFilePath);
                        log.info("Deleted old image file: {}", oldFilePath);
                    } catch (IOException e) {
                        log.error("Could not delete old image file: {}", oldFilePath, e);
                    }
                }

            } catch (IOException e) {
                log.error("Error uploading image: ", e);
                model.addAttribute("pageTitle", isNew ? "Add New Product" : "Edit Product (ID: " + product.getMa() + ")");
                model.addAttribute("saveError", "Error uploading image. Please try again.");
                return "admin/products/form";
            }
        }

        product.setImageFileName(uniqueFileName);

        try {
            log.info("Saving product with ID: {}", product.getMa() == null ? "NEW" : product.getMa());
            productRepository.save(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product has been saved successfully!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            log.error("Error saving product: ", e);
            model.addAttribute("pageTitle", isNew ? "Add New Product" : "Edit Product (ID: " + product.getMa() + ")");
            model.addAttribute("saveError", "An error occurred while saving the product. Please check the data and try again.");
            return "admin/products/form";
        }
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        log.info("Attempting to delete product with ID: {}", id);
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isPresent()) {
                Product productToDelete = productOptional.get();
                String imageFileNameToDelete = productToDelete.getImageFileName();

                productRepository.deleteById(id);
                log.info("Product deleted successfully from DB with ID: {}", id);

                if (imageFileNameToDelete != null && !imageFileNameToDelete.isEmpty()) {
                    try {
                        Path uploadPath = Paths.get(uploadDir);
                        Path filePathToDelete = uploadPath.resolve(imageFileNameToDelete);
                        Files.deleteIfExists(filePathToDelete);
                        log.info("Deleted associated image file: {}", filePathToDelete);
                    } catch (IOException e) {
                        log.error("Could not delete image file {} for deleted product ID {}: ", imageFileNameToDelete, id, e);
                        redirectAttributes.addFlashAttribute("warningMessage", "Product deleted, but failed to delete associated image file.");
                    }
                }
                redirectAttributes.addFlashAttribute("successMessage", "Product (ID: " + id + ") deleted successfully.");
            } else {
                log.warn("Product not found for deletion with ID: {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error deleting product with ID: {}: ", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product (ID: " + id + "). It might be referenced elsewhere.");
        }
        return "redirect:/admin/products";
    }
}