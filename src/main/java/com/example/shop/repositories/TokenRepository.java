package com.example.shop.repositories;

import com.example.shop.entities.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TokenRepository extends MongoRepository<Token, String> {
    @Query("{ 'user.$id': ?0, $or: [ { 'expired': false }, { 'revoked': false } ] }")
    List<Token> findAllValidTokenByUser(String userId);}