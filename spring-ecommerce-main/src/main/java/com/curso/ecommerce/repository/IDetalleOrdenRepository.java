package com.curso.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.ecommerce.model.Historial;

@Repository
public interface IDetalleOrdenRepository extends JpaRepository<Historial, Integer> {

}
