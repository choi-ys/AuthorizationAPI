package io.example.authorization.repository;

import io.example.authorization.domain.client.entity.ClientDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientDetailRepository extends JpaRepository<ClientDetailsEntity, String> {

    Optional<ClientDetailsEntity> findByClientId(String clientId);
}