package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Cuentas;

public interface ProductoService {
	public Cuentas save(Cuentas cuentas);
	public Optional<Cuentas> get(Integer id);
	public void update(Cuentas cuentas);
	public void delete(Integer id);
	public List<Cuentas> findAll();

}
