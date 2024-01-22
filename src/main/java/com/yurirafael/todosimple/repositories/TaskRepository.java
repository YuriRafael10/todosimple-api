package com.yurirafael.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yurirafael.todosimple.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser_Id(Long id);

    // @Query(value="SELECT t FROM Task t WHERE t.user.id = :user_id")
    // List<Task> findByUser_Id(@Param("user_id") Long user_id);

    // @Query(value="SELECT * FROM task t WHERE t.user_id = :user_id" ,nativeQuery =
    // true)
    // List<Task> findByUser_Id(@Param("user_id") Long user_id);
}
