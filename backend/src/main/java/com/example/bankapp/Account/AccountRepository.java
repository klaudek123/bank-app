package com.example.bankapp.Account;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT MAX(number) FROM Account")
    long findTopByNumber();

    Optional<Object> findByIdAccountAndPassword(Long idAccount, String password);

    @Query("SELECT a.idUser FROM Account a WHERE a.idAccount = :idAccount")
    Long getIdUserByIdAccount(@Param("idAccount") Long idAccount);

    @Query("SELECT a.number FROM Account a WHERE a.idAccount = :idAccount")
    Long getNumberByIdAccount(@Param("idAccount") Long idAccount);


    @Query("SELECT a.idAccount FROM Account a WHERE a.number = :number")
    Long getIdAccountByNumber(@Param("number") Long number);

}
