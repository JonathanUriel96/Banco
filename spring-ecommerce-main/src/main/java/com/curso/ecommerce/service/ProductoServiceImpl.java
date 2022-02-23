package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Cuentas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.repository.IProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService{
	
	@Autowired
	private IProductoRepository productoRepository;

	@Override
	public Cuentas save(Cuentas cuentas) {
		return productoRepository.save(cuentas);
	}

	@Override
	public Optional<Cuentas> get(Integer id) {
		return productoRepository.findById(id);
	}

	@Override
	public void update(Cuentas cuentas) {
		productoRepository.save(cuentas);
	}

	@Override
	public void delete(Integer id) {
		productoRepository.deleteById(id);		
	}

	@Override
	public List<Cuentas> findAll() {
		return productoRepository.findAll();
	}

}
