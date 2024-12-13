package ro.tucn.energy_mgmt_login.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ro.tucn.energy_mgmt_login.exception.ExceptionCode;
import ro.tucn.energy_mgmt_login.model.UserEntity;
import ro.tucn.energy_mgmt_login.repository.UserRepository;

import static ro.tucn.energy_mgmt_login.security.util.SecurityConstants.ROLE_ADMIN;
import static ro.tucn.energy_mgmt_login.security.util.SecurityConstants.ROLE_USER;

@RequiredArgsConstructor
public class UserDetailsServiceBean implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(this::getUserDetails)
                .orElseThrow(() -> new BadCredentialsException(ExceptionCode.ERR001_INVALID_CREDENTIALS.getMessage()));
    }

    private UserDetails getUserDetails(UserEntity user) {
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.isAdmin()?ROLE_USER:ROLE_ADMIN)
                .build();
    }
}
