package com.coffeecup.coffeecupadminsystem.service; // Sửa package nếu cần

import com.coffeecup.coffeecupadminsystem.model.Authority; // Sửa import nếu cần
import com.coffeecup.coffeecupadminsystem.model.User; // Sửa import nếu cần
import com.coffeecup.coffeecupadminsystem.repository.UserRepository; // Sửa import nếu cần
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Sử dụng exception này để báo lỗi chung chung
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tìm user trong DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password")); // Lỗi chung nếu không tìm thấy

        // 2. Kiểm tra xem user có đủ quyền truy cập hệ thống này không
        boolean hasRequiredRole = user.getAuthorities().stream()
                .map(Authority::getName) // Lấy tên của role (vd: "ROLE_ADMIN")
                .anyMatch(roleName -> roleName.equals("ROLE_ADMIN") || roleName.equals("ROLE_SYSTEM"));

        if (!hasRequiredRole) {
            // Nếu không có ROLE_ADMIN hoặc ROLE_SYSTEM, ném lỗi để login thất bại
            // Ném UsernameNotFoundException sẽ khiến Spring Security hiển thị lỗi "Invalid username or password"
            throw new UsernameNotFoundException("User does not have required role for this system");
            // Hoặc bạn có thể ném các lỗi khác như DisabledException, LockedException nếu muốn phân biệt,
            // nhưng UsernameNotFoundException hoặc BadCredentialsException thường được dùng để che giấu lý do thật sự.
        }

        // 3. Nếu đủ quyền, tạo đối tượng UserDetails như bình thường
        Set<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                grantedAuthorities
        );
    }
}