package ru.yakovlev.kanban.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yakovlev.kanban.dto.task.TaskSearchCriteria;
import ru.yakovlev.kanban.model.task.Task;
import ru.yakovlev.kanban.model.user.User;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class CustomTaskRepository {

    private final EntityManager entityManager;

    public List<Task> findTasks(TaskSearchCriteria taskSearchCriteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> criteriaQuery = builder.createQuery(Task.class);
        Root<Task> root = criteriaQuery.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();

        if (taskSearchCriteria.getId() != null) {
            Predicate id = builder.equal(root.get("id"), taskSearchCriteria.getId());
            predicates.add(id);
        }

        if (taskSearchCriteria.getName() != null) {
            String searchText = "%" + taskSearchCriteria.getName().toLowerCase() + "%";

            Predicate searchInName = builder.like(builder.lower(root.get("name")), searchText);
            predicates.add(searchInName);
        }

        if (taskSearchCriteria.getDescription() != null) {
            String searchText = "%" + taskSearchCriteria.getDescription().toLowerCase() + "%";

            Predicate searchInDescription = builder.like(builder.lower(root.get("description")), searchText);
            predicates.add(searchInDescription);
        }

        if (taskSearchCriteria.getStatus() != null) {
            Predicate status = builder.equal(root.get("status"), taskSearchCriteria.getStatus());
            predicates.add(status);
        }

        if (taskSearchCriteria.getPriority() != null) {
            Predicate priority = builder.equal(root.get("priority"), taskSearchCriteria.getPriority());
            predicates.add(priority);
        }

        if (taskSearchCriteria.getAuthor() != null) {
            Join<User, Task> taskJoin = root.join("author");
            Predicate author = builder.equal(taskJoin.get("id"), taskSearchCriteria.getAuthor());

            predicates.add(author);
        }

        if (taskSearchCriteria.getExecutor() != null) {
            Join<User, Task> taskJoin = root.join("executor");
            Predicate author = builder.equal(taskJoin.get("id"), taskSearchCriteria.getExecutor());

            predicates.add(author);
        }

        if (taskSearchCriteria.getDeleted() != null) {
            Predicate deleted = builder.equal(root.get("deleted"), taskSearchCriteria.getDeleted());
            predicates.add(deleted);
        }

        CriteriaQuery<Task> select = criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]))
                .orderBy(builder.asc(root.get("id")));
        TypedQuery<Task> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(taskSearchCriteria.getFrom());
        typedQuery.setMaxResults(taskSearchCriteria.getSize());

        return typedQuery.getResultList();
    }
}
