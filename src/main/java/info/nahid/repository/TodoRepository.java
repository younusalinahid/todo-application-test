package info.nahid.repository;

import info.nahid.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {

    Optional<Todo> findById(UUID id);

}
