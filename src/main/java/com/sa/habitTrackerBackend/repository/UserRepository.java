package com.sa.habitTrackerBackend.repository;

import com.sa.habitTrackerBackend.dao.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long>, CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
