package br.com.desafio.repository;

import br.com.desafio.domain.model.BillingModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<BillingModel, Long> {}
