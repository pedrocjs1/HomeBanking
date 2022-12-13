package com.MindHub.HomeBanking.models;

import com.MindHub.HomeBanking.dtos.ClientLoanDTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity // Crea una tabla en la DB (en este caso de cliente)
public class Client {

    @Id // indica que la propiedad de abajo es la clave primaria (primari key). cada objejeto debe de tener una primary key UNICA
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") // esa primary key es generada por la db y se y se lo indicamos con esta notacion
    @GenericGenerator(name = "native", strategy = "native") // genericGeneractor hace refencia a la estrategia nativa, hibernate usa la estrategia soportada de forma nativa por el dialecto configurado
    private Long id;

    @OneToMany(mappedBy="owner", fetch=FetchType.EAGER) // especificamos la relacion entre objetos (en este caso uno a muchos) el mapped by sirve para aclarar con que atributo de la otra clase tenemos asociada esta clase.
    private Set<Account> accounts = new HashSet<>(); // inicializar en memoria un set vacio

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> loans = new HashSet<>();
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

    public void setLoans(Set<ClientLoan> loans) {
        this.loans = loans;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }


    public void addAccount(Account account) {
        account.setOwner(this);
        accounts.add(account);
    }



    public List<ClientLoanDTO> getLoans() {
        return loans.stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toList());
    }


}
