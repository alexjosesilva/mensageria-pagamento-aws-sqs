package br.com.desafio.repository;

import br.com.desafio.domain.model.SellerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<SellerModel, Long> {}
