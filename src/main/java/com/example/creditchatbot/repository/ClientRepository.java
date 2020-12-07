package com.example.creditchatbot.repository;

import com.example.creditchatbot.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
