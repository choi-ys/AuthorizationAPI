package io.example.authorization.repository;

import io.example.authorization.domain.partner.entity.PartnerAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerAccountEntityRepository extends JpaRepository<PartnerAccountEntity, Long> {
}
