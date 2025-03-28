package com.coffeecup.coffeecupadminsystem.controller;

import com.coffeecup.coffeecupadminsystem.model.Authority;
import com.coffeecup.coffeecupadminsystem.model.User;
import com.coffeecup.coffeecupadminsystem.repository.AuthorityRepository;
import com.coffeecup.coffeecupadminsystem.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/system")
public class SystemController {

    private static final Logger log = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- HIỂN THỊ DANH SÁCH USERS ---
    @GetMapping("/users")
    public String listUsers(Model model) {
        log.info("Fetching user list");
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        if (model.containsAttribute("successMessage")) {
            log.info("Displaying success message: {}", model.getAttribute("successMessage"));
        }
        if (model.containsAttribute("errorMessage")) {
            log.warn("Displaying error message: {}", model.getAttribute("errorMessage"));
        }
        return "system/users/list";
    }

    // --- HIỂN THỊ FORM THÊM USER MỚI ---
    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        log.info("Showing add user form");
        List<Authority> allRoles = authorityRepository.findAll();
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("pageTitle", "Add New User");
        return "system/users/form";
    }

    // --- HIỂN THỊ FORM CHỈNH SỬA USER ---
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Showing edit user form for ID: {}", id);
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            log.warn("User not found for edit with ID: {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", "User not found with ID: " + id);
            return "redirect:/system/users";
        }

        User user = userOptional.get();
        List<Authority> allRoles = authorityRepository.findAll();
        user.setPassword(null);        // Xóa mật khẩu nhạy cảm
        user.setCurrentPassword(null); // Xóa currentPassword nếu có
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("pageTitle", "Edit User '" + user.getUsername() + "' (ID: " + id + ")");
        return "system/users/form";
    }

    // --- XỬ LÝ LƯU USER (Thêm mới / Cập nhật) ---
    @PostMapping("/users/save")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        boolean isNewUser = (user.getId() == null);
        log.info("Attempting to save user (New: {}): {}", isNewUser, user.getUsername());

        // --- Validation tùy chỉnh ---
        // 1. Kiểm tra username trùng lặp
        userRepository.findByUsername(user.getUsername()).ifPresent(existingUser -> {
            if (isNewUser || !existingUser.getId().equals(user.getId())) {
                bindingResult.rejectValue("username", "error.user", "Username already exists.");
            }
        });

        // 2. Password, Confirm Password, và Current Password
        boolean isPasswordProvided = user.getPassword() != null && !user.getPassword().isEmpty();
        String currentPasswordFromForm = user.getCurrentPassword();
        String confirmPasswordFromForm = user.getConfirmPassword();

        if (!isNewUser) { // --- TRƯỜNG HỢP EDIT ---
            // *** BẮT BUỘC KIỂM TRA CURRENT PASSWORD ***
            if (currentPasswordFromForm == null || currentPasswordFromForm.isEmpty()) {
                bindingResult.rejectValue("currentPassword", "error.user", "Current password is required to save changes.");
            } else {
                User currentUser = userRepository.findById(user.getId()).orElse(null);
                if (currentUser == null) {
                    bindingResult.reject("error.global", "User not found, cannot verify current password.");
                } else if (!passwordEncoder.matches(currentPasswordFromForm, currentUser.getPassword())) {
                    bindingResult.rejectValue("currentPassword", "error.user", "Incorrect current password.");
                }
                // Nếu current password đúng, mới kiểm tra new/confirm password nếu có
                else if (isPasswordProvided) { // Chỉ kiểm tra nếu có nhập pass mới
                    if (user.getPassword().length() < 6 || user.getPassword().length() > 10) {
                        bindingResult.rejectValue("password", "error.user", "New password must be between 6 and 10 characters.");
                    } else if (confirmPasswordFromForm == null || !user.getPassword().equals(confirmPasswordFromForm)) {
                        bindingResult.rejectValue("confirmPassword", "error.user", "New passwords do not match.");
                    }
                }
            } // Hết kiểm tra current password đúng
        } else { // --- TRƯỜNG HỢP THÊM MỚI ---
            // Password và Confirm là bắt buộc và phải khớp
            if (!isPasswordProvided || user.getPassword().length() < 6 || user.getPassword().length() > 10) {
                bindingResult.rejectValue("password", "error.user", "Password must be between 6 and 10 characters.");
            } else if (confirmPasswordFromForm == null || confirmPasswordFromForm.isEmpty()) {
                bindingResult.rejectValue("confirmPassword", "error.user", "Please confirm the password.");
            } else if (!user.getPassword().equals(confirmPasswordFromForm)) {
                bindingResult.rejectValue("confirmPassword", "error.user", "Passwords do not match.");
            }
        } // Hết if (isNewUser)

        // 3. Role validation được xử lý bởi @NotEmpty trong entity

        // --- Xử lý nếu có lỗi ---
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors found for user {}: {}", user.getUsername(), bindingResult.getAllErrors());
            List<Authority> allRoles = authorityRepository.findAll();
            model.addAttribute("allRoles", allRoles);
            model.addAttribute("pageTitle", isNewUser ? "Add New User" : "Edit User (ID: " + user.getId() + ")");
            user.setPassword(null);        // Xóa password mới đã nhập
            user.setCurrentPassword(null); // Xóa current password đã nhập
            return "system/users/form";
        }

        // --- Xử lý nếu không có lỗi ---
        try {
            User userToSave;
            if (isNewUser) {
                userToSave = user;
                userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                userToSave = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user.getId()));
                userToSave.setUsername(user.getUsername());
                userToSave.setEnabled(user.isEnabled());
                if (isPasswordProvided) { // Chỉ mã hóa nếu có nhập mới VÀ current password đã đúng ở trên
                    userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
                    log.info("Updating password for user ID: {}", user.getId());
                }
                // Cập nhật roles
                Set<Authority> managedAuthorities = new HashSet<>();
                if (user.getAuthorities() != null) {
                    for (Authority roleFromForm : user.getAuthorities()) {
                        if (roleFromForm != null && roleFromForm.getId() != null) {
                            authorityRepository.findById(roleFromForm.getId())
                                    .ifPresent(managedAuthorities::add);
                        }
                    }
                }
                userToSave.setAuthorities(managedAuthorities);
            }

            log.info("Saving user: {}", userToSave.getUsername());
            userRepository.save(userToSave);

            redirectAttributes.addFlashAttribute("successMessage", "User '" + userToSave.getUsername() + "' has been saved successfully!");
            return "redirect:/system/users";

        } catch (Exception e) {
            log.error("Error saving user {}: ", user.getUsername(), e);
            model.addAttribute("saveError", "An error occurred while saving the user. Check logs for details.");
            List<Authority> allRoles = authorityRepository.findAll();
            model.addAttribute("allRoles", allRoles);
            model.addAttribute("pageTitle", isNewUser ? "Add New User" : "Edit User (ID: " + user.getId() + ")");
            user.setPassword(null);
            user.setCurrentPassword(null);
            return "system/users/form";
        }
    }

    // --- XÓA USER ---
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, Authentication authentication) {
        log.info("Attempting to delete user with ID: {}", id);

        // Lấy username của người dùng hiện tại
        String loggedInUsername = authentication.getName();

        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User userToDelete = userOptional.get();

                // Ngăn người dùng tự xóa tài khoản của chính mình
                if (userToDelete.getUsername().equals(loggedInUsername)) {
                    log.warn("Attempt to delete currently logged-in user: {}", loggedInUsername);
                    redirectAttributes.addFlashAttribute("errorMessage", "You cannot delete your own account.");
                    return "redirect:/system/users";
                }

                userRepository.deleteById(id);
                log.info("User deleted successfully with ID: {}", id);
                redirectAttributes.addFlashAttribute("successMessage", "User '" + userToDelete.getUsername() + "' (ID: " + id + ") deleted successfully.");
            } else {
                log.warn("User not found for deletion with ID: {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "User not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error deleting user with ID: {}: ", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user (ID: " + id + "). It might be referenced elsewhere.");
        }
        return "redirect:/system/users";
    }
}