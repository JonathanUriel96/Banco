package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Deposito;
import com.curso.ecommerce.model.Usuario;

public interface IOrdenService {
	List<Deposito> findAll();
	Optional<Deposito> findById(Integer id);
	Deposito save (Deposito deposito);
	String generarNumeroOrden();
	List<Deposito> findByUsuario (Usuario usuario);
}
