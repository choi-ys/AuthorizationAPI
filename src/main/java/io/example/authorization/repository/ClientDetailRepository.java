package io.example.authorization.repository;

import io.example.authorization.domain.client.ClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDetailRepository extends JpaRepository<ClientDetail, String> {
}