package pl.com.tegess.controller.user.writecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tegess.domain.user.UserRepository;

@RestController
@Component
public class UserWriteController {

    @Autowired
    UserRepository repository;

}
