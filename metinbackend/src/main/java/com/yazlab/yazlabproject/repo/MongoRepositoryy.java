package com.yazlab.yazlabproject.repo;

import com.yazlab.yazlabproject.entity.Metinler;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoRepositoryy extends MongoRepository<Metinler, String> {
}
