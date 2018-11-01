package com.wolffr.SimilarityChecker.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.wolffr.SimilarityChecker.entity.Physician;

@Service
public interface IPhysicianManager extends CrudRepository<Physician, Long> {

    public List<Physician> findAll();
}
