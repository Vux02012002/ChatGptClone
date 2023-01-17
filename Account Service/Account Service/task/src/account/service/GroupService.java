package account.service;

import account.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import account.entity.Group;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GroupService {
    GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {
        try {
            groupRepository.save(new Group("ROLE_ADMINISTRATOR"));
            groupRepository.save(new Group("ROLE_USER"));
            groupRepository.save(new Group("ROLE_ACCOUNTANT"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
