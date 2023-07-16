package ru.main_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.main_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> getUsersByIdIsIn(List<Long> usersId, Pageable pageable);
}
