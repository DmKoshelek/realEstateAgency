package by.bsuir.realEstateAgency.web.security;

import by.bsuir.realEstateAgency.core.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

public class AuthenticationService implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        by.bsuir.realEstateAgency.core.model.User userInfo = userService.getByLoginOrEmail(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new AuthUserDetails(userInfo);
    }
}