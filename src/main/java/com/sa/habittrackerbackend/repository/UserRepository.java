package com.sa.habittrackerbackend.repository;

import com.sa.habittrackerbackend.dao.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
}
