package br.com.vote.repository;

import br.com.vote.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select u from User u where u.cpf = :cpf")
    User findByCPF(@Param("cpf") String cpf);

    @Query(value = "select u from User u where u.email = :email")
    User findByEmail(@Param("email") String email);

}
