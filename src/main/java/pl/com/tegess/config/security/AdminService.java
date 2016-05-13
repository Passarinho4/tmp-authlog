package pl.com.tegess.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.com.tegess.config.Config;
import pl.com.tegess.domain.admin.AdminRepository;

@Configuration
@Import({Config.class})
public class AdminService implements UserDetailsService {

    @Autowired
    AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findOne(username);
    }
}
