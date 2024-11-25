package com.example.bankapp.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT MAX(number) FROM Account")
    Long findMaxAccountNumber();

    @Query("SELECT a.user.personalId FROM Account a WHERE a.idAccount = :idAccount")
    Long getIdUserByIdAccount(@Param("idAccount") Long idAccount);

    @Query("SELECT a.number FROM Account a WHERE a.idAccount = :idAccount")
    Long getNumberByIdAccount(@Param("idAccount") Long idAccount);


    @Query("SELECT a FROM Account a WHERE a.number = :number")
    Account getAccountByNumber(@Param("number") Long number);

}
