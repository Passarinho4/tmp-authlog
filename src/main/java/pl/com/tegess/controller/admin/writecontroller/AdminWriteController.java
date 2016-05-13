package pl.com.tegess.controller.admin.writecontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tegess.controller.admin.request.AdminWriteData;
import pl.com.tegess.domain.admin.Admin;
import pl.com.tegess.domain.admin.AdminRepository;

@RestController
@Component
public class AdminWriteController {

    @Autowired
    AdminRepository repository;

    @RequestMapping(value = "/api/admins", method = RequestMethod.POST)
    public void create(@RequestBody AdminWriteData adminWriteData) {
        Admin admin = new Admin(adminWriteData.getUsername(), adminWriteData.getPassword());
        repository.insert(admin);
    }
}
