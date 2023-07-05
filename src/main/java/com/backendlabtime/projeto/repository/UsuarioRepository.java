package com.backendlabtime.projeto.repository;

import com.backendlabtime.projeto.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByNomeCompletoContainingIgnoreCaseOrNomeSocialContainingIgnoreCaseOrEmailContainingIgnoreCase(String nomeCompleto, String nomeSocial, String email);

    List<Usuario> findAllUsersByFilter();

    Page<Usuario> findAllUsers(Pageable pageable);
}
