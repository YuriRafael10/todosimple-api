package com.yurirafael.todosimple.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yurirafael.todosimple.models.Task;
import com.yurirafael.todosimple.models.User;
import com.yurirafael.todosimple.models.enums.ProfileEnum;
import com.yurirafael.todosimple.repositories.TaskRepository;
import com.yurirafael.todosimple.security.UserSpringSecurity;
import com.yurirafael.todosimple.services.exceptions.AuthorizationException;
import com.yurirafael.todosimple.services.exceptions.DataBindingViolationExcpetion;
import com.yurirafael.todosimple.services.exceptions.ObjectNotFoundExcpetion;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        @SuppressWarnings("null")
        Task task = this.taskRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExcpetion(
                        "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
            throw new AuthorizationException("Acesso negado!");

        return task;
    }

    public List<Task> findAllByUser() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    @Transactional
    public Task create(Task obj) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");


        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    @SuppressWarnings("null")
    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationExcpetion("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }
}
