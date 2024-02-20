package com.yurirafael.todosimple.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yurirafael.todosimple.models.User;
import com.yurirafael.todosimple.models.enums.ProfileEnum;
import com.yurirafael.todosimple.repositories.UserRepository;
import com.yurirafael.todosimple.security.UserSpringSecurity;
import com.yurirafael.todosimple.services.exceptions.AuthorizationException;
import com.yurirafael.todosimple.services.exceptions.DataBindingViolationExcpetion;
import com.yurirafael.todosimple.services.exceptions.ObjectNotFoundExcpetion;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        UserSpringSecurity userSpringSecurity = authenticated();
        if (!Objects.nonNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
            throw new AuthorizationException("Acesso negado!");

        @SuppressWarnings("null")
        Optional<User> user = this.userRepository.findById(id);

        return user.orElseThrow(
                () -> new ObjectNotFoundExcpetion(
                        "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }

    @Transactional // Usar sempre que for criar/atualizar algo no banco
    public User create(User obj) {
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    @SuppressWarnings("null")
    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationExcpetion("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}