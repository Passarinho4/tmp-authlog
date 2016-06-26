package pl.com.tegess.controller.admin.readcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tegess.controller.admin.request.AdminReadData;
import pl.com.tegess.domain.admin.AdminRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Component
public class AdminReadController {

    @Autowired
    AdminRepository repository;

    @RequestMapping(value = "/api/admins", method = RequestMethod.GET)
    public List<AdminReadData> get() {
        return repository.find().asList()
                .stream()
                .map(admin -> new AdminReadData(admin.getUsername()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/loginAdmin", method = RequestMethod.GET)
    public AdminReadData loginAdmin() {
        return new AdminReadData("Szymek");
    }
}
