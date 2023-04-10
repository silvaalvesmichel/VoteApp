package br.com.vote.repository;

import br.com.vote.repository.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    @Query(value = "select u from Agenda u where u.description = :description")
    Agenda findByDescription(@Param("description") String description);
}