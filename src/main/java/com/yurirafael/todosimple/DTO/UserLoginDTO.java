package com.yurirafael.todosimple.DTO;

// Data Transfer Object
public class UserLoginDTO {
    private String username;
    private String password;

    public UserLoginDTO() {
        // Construtor padrão necessário para a desserialização do Json
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
