package com.elms.userservice.repository;

import com.elms.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // لا تحتاج أي كود إضافي الآن
}
