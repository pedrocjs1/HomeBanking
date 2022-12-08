package com.MindHub.HomeBanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity // Crea una tabla en la DB (en este caso de cliente)
public class Client {

    @Id // indica que la propiedad de abajo es la clave primaria (primari key). cada objejeto debe de tener una primary key UNICA
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") // esa primary key es generada por la db y se y se lo indicamos con esta notacion
    @GenericGenerator(name = "native", strategy = "native") // se usa para denotar un generador especifico(como se va a generar). es una anotacion de hybernate
    private Long id;

    @OneToMany(mappedBy="owner", fetch=FetchType.EAGER) // especificamos la relacion entre objetos (en este caso uno a muchos) el mapped by sirve para aclarar con que atributo de la otra clase tenemos asociada esta clase.
    Set<Account> accounts = new HashSet<>(); // inicializar en memoria un set vacio

    private String firstName, lastName, email;

    public Client(){}

    public Client(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }


    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEmail(){
        return email;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        account.setOwner(this);
        accounts.add(account);
    }
}