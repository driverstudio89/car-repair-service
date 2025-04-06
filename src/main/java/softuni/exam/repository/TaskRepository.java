package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT t FROM Task t JOIN t.cars c JOIN t.mechanic m WHERE c.carType = 'coupe' ORDER BY t.price DESC")
    List<Task> findAllByPriceDesc();
}
