package com.yurirafael.todosimple.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity // entity = tratar a classe como uma tabela
@Table(name = User.TABLE_NAME) // criando uma tabela
public class User {
    public interface CreateUser {
    }

    public interface UpdateUser {
    }

    public static final String TABLE_NAME = "user"; // garantindo que o nome da tabela vai ser "user"

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // basicamente um auto increment
    @Column(name = "id", unique = true) // o nome da coluna é id e é uma "unique key"
    private Long id;

    @Column(name = "username", length = 100, unique = true, nullable = false)
    @NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 2, max = 100)
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY) // nao retornar senha no json da api
    @Column(name = "password", length = 60, nullable = false)
    @NotNull(groups = { CreateUser.class, UpdateUser.class }) // garantindo que no update/create, as validacoes estejam
                                                              // acontecendo
    @NotEmpty(groups = { CreateUser.class, UpdateUser.class })
    @Size(groups = { CreateUser.class, UpdateUser.class }, min = 8, max = 60)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<Task>();

    public User() {
    }

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @JsonIgnore
    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true; // Verifica se os dois objetos são a mesma instância na memória. Se forem, eles
                         // são considerados iguais, e o método retorna true.
        if (obj == null)
            return false; // Verifica se o objeto fornecido para comparação é nulo. Se for nulo, os
                          // objetos não são iguais, e o método retorna false.
        if (!(obj instanceof User))
            return false; // Verifica se o objeto fornecido pertence à classe User. Se não pertencer, os
                          // objetos não são considerados iguais, e o método retorna false.
        User other = (User) obj; // Converte o objeto fornecido (obj) para a classe User, uma vez que já foi
                                 // verificado que é uma instância de User. É um pouco redundante, apenas feito
                                 // pra realmente garantir que é um User
        if (this.id == null) {
            if (other.id != null)
                return false; // Verifica se o ID do objeto atual (this) é nulo. Se for nulo, verifica se o ID
                              // do objeto fornecido (other) também é nulo. Se não for nulo, os objetos não
                              // são considerados iguais, e o método retorna false. Se ambos os IDs são nulos,
                              // a comparação continua para os campos username e password.
            if (!this.id.equals(other.id))
                return false; // Compara os IDs dos objetos atual e fornecido. Se não forem iguais, os objetos
                              // não são considerados iguais, e o método retorna false.
        }
        return Objects.equals(this.id, other.id) && Objects.equals(this.username, other.username)
                && Objects.equals(this.password, other.password); // Compara os campos id, username e password usando o
                                                                  // método Objects.equals. Se todos esses campos são
                                                                  // iguais nos dois objetos, o método retorna true. Se
                                                                  // algum deles for diferente, o método retorna false.
                                                                  // Essa comparação é feita utilizando o método
                                                                  // Objects.equals para evitar possíveis exceções de
                                                                  // NullPointerException se algum dos campos for nulo.
    }

    @Override
    public int hashCode() {
        final int prime = 31; // Um número primo geralmente é escolhido como um multiplicador em funções de
                              // hash para ajudar a distribuir melhor os códigos hash. O número 31 é
                              // frequentemente usado por ser um número primo pequeno que tem propriedades
                              // favoráveis.

        int result = 1; // Inicializa o resultado com um valor inicial (geralmente 1).

        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode()); // Calcula o código hash para o campo id
        // e combina isso com o resultado anterior. Se o id for nulo, contribu com 0 ao
        // código hash, caso contrário, contribui com o código hash do id. Se o id for
        // nulo, (prime * result) ainda será igual a result, então a contribuição para o
        // código hash é 0. Se o id não for nulo, a contribuição para o código hash é
        // (prime * result) + id.hashCode()

        return result;
    }
}
