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
import org.springframework.dao.DataIntegrityViolationException;

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

    // --- TOGGLE USER ENABLED STATUS ---
    @PostMapping("/users/toggle/{id}")
    public String toggleUserStatus(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, Authentication authentication) {
        log.info("Attempting to toggle status for user ID: {}", id);
        String loggedInUsername = authentication.getName();

        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User userToToggle = userOptional.get();

                // Ngăn chặn tự disable tài khoản đang đăng nhập
                if (userToToggle.getUsername().equals(loggedInUsername)) {
                    log.warn("Attempt to disable own account: {}", loggedInUsername);
                    redirectAttributes.addFlashAttribute("errorMessage", "You cannot disable your own account.");
                    return "redirect:/system/users";
                }

                // Đảo ngược trạng thái enabled
                boolean newState = !userToToggle.isEnabled();
                userToToggle.setEnabled(newState);
                userRepository.save(userToToggle); // Lưu lại thay đổi

                log.info("User '{}' status toggled to: {}", userToToggle.getUsername(), newState ? "Enabled" : "Disabled");
                redirectAttributes.addFlashAttribute("successMessage",
                        "User '" + userToToggle.getUsername() + "' has been " + (newState ? "enabled" : "disabled") + ".");

            } else {
                log.warn("User not found for status toggle with ID: {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "User not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error toggling status for user ID: {}: ", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error toggling status for user (ID: " + id + ").");
        }
        return "redirect:/system/users";
    }

    // ===========================================
    // Authority (Role) Management Methods
    // ===========================================

    // --- HIỂN THỊ DANH SÁCH AUTHORITIES ---
    @GetMapping("/authorities")
    public String listAuthorities(Model model) {
        log.info("Fetching authority list");
        List<Authority> authorities = authorityRepository.findAll();
        model.addAttribute("authorities", authorities);
        // Thêm thông báo nếu có
        if (model.containsAttribute("successMessage")) log.info("Displaying success message: {}", model.getAttribute("successMessage"));
        if (model.containsAttribute("errorMessage")) log.warn("Displaying error message: {}", model.getAttribute("errorMessage"));
        return "system/authorities/list"; // View: templates/system/authorities/list.html
    }

    // --- HIỂN THỊ FORM THÊM AUTHORITY MỚI ---
    @GetMapping("/authorities/add")
    public String showAddAuthorityForm(Model model) {
        log.info("Showing add authority form");
        model.addAttribute("authority", new Authority());
        model.addAttribute("pageTitle", "Add New Authority");
        return "system/authorities/form";
    }

    // --- HIỂN THỊ FORM CHỈNH SỬA AUTHORITY ---
    @GetMapping("/authorities/edit/{id}")
    public String showEditAuthorityForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Showing edit authority form for ID: {}", id);
        Optional<Authority> authorityOptional = authorityRepository.findById(id);

        if (authorityOptional.isEmpty()) {
            log.warn("Authority not found for edit with ID: {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Authority not found with ID: " + id);
            return "redirect:/system/authorities";
        }

        Authority authority = authorityOptional.get();
        model.addAttribute("authority", authority);
        model.addAttribute("pageTitle", "Edit Authority '" + authority.getName() + "' (ID: " + id + ")");
        return "system/authorities/form";
    }

    // --- XỬ LÝ LƯU AUTHORITY (Thêm mới / Cập nhật) ---
    @PostMapping("/authorities/save")
    public String saveAuthority(@Valid @ModelAttribute("authority") Authority authority,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        boolean isNewAuthority = (authority.getId() == null);
        log.info("Attempting to save authority (New: {}): {}", isNewAuthority, authority.getName());

        // Kiểm tra tên authority trùng lặp
        authorityRepository.findByName(authority.getName()).ifPresent(existingAuthority -> {
            if (isNewAuthority || !existingAuthority.getId().equals(authority.getId())) {
                bindingResult.rejectValue("name", "error.authority", "Authority name already exists.");
            }
        });

        // Xử lý nếu có lỗi validation
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors found for authority {}: {}", authority.getName(), bindingResult.getAllErrors());
            model.addAttribute("pageTitle", isNewAuthority ? "Add New Authority" : "Edit Authority (ID: " + authority.getId() + ")");
            return "system/authorities/form";
        }

        // Lưu authority nếu không có lỗi
        try {
            log.info("Saving authority: {}", authority.getName());
            authorityRepository.save(authority);
            redirectAttributes.addFlashAttribute("successMessage", "Authority '" + authority.getName() + "' has been saved successfully!");
            return "redirect:/system/authorities";
        } catch (Exception e) {
            log.error("Error saving authority {}: ", authority.getName(), e);
            model.addAttribute("saveError", "An error occurred while saving the authority. Check logs for details.");
            model.addAttribute("pageTitle", isNewAuthority ? "Add New Authority" : "Edit Authority (ID: " + authority.getId() + ")");
            return "system/authorities/form";
        }
    }

    // --- XÓA AUTHORITY ---
    @GetMapping("/authorities/delete/{id}")
    public String deleteAuthority(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        log.info("Attempting to delete authority with ID: {}", id);

        try {
            Optional<Authority> authorityOptional = authorityRepository.findById(id);
            if (authorityOptional.isPresent()) {
                Authority authorityToDelete = authorityOptional.get();
                String roleName = authorityToDelete.getName();

                // Kiểm tra các role cốt lõi - không cho xóa
                if ("ROLE_ADMIN".equals(roleName) || "ROLE_SYSTEM".equals(roleName) || "ROLE_USER".equals(roleName)) {
                    log.warn("Attempt to delete core role: {}", roleName);
                    redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete core system role: " + roleName);
                    return "redirect:/system/authorities";
                }

                authorityRepository.deleteById(id);
                log.info("Authority deleted successfully: {}", roleName);
                redirectAttributes.addFlashAttribute("successMessage", "Role '" + roleName + "' (ID: " + id + ") deleted successfully.");
            } else {
                log.warn("Authority not found for deletion with ID: {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Role not found with ID: " + id);
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Error deleting authority with ID: {}: Cannot delete role because it might be in use.", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete role (ID: " + id + "). It might be assigned to users. Please check user assignments.");
        } catch (Exception e) {
            log.error("Error deleting authority with ID: {}: ", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting role (ID: " + id + ").");
        }
        return "redirect:/system/authorities";
    }
}