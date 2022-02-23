package com.curso.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.ecommerce.model.Deposito;
import com.curso.ecommerce.model.Usuario;

@Repository
public interface IOrdenRepository extends JpaRepository<Deposito, Integer> {
	List<Deposito> findByUsuario (Usuario usuario);
}
