package com.curso.ecommerce.service;

import com.curso.ecommerce.model.Historial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.repository.IDetalleOrdenRepository;

@Service
public class DetalleOrdenServiceImpl implements IDetalleOrdenService{
	
	@Autowired
	private IDetalleOrdenRepository detalleOrdenRepository;

	@Override
	public Historial save(Historial detalleOrden) {
		return detalleOrdenRepository.save(detalleOrden);
	}

}
