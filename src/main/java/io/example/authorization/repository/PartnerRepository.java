package io.example.authorization.repository;

import io.example.authorization.domain.partner.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {
    long countByPartnerId(String id);
}
