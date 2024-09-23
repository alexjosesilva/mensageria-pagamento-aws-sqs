package br.com.desafio.repository;

import br.com.desafio.domain.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {}
