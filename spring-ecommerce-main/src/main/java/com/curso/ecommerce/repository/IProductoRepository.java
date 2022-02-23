package com.curso.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.ecommerce.model.Cuentas;

@Repository
public interface IProductoRepository extends JpaRepository<Cuentas, Integer> {

}
