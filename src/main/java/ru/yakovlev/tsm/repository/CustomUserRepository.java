package ru.yakovlev.tsm.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yakovlev.tsm.dto.user.UserSearchCriteria;
import ru.yakovlev.tsm.model.user.User;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class CustomUserRepository {

    private final EntityManager entityManager;

    public List<User> findUsers(UserSearchCriteria userSearchCriteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (userSearchCriteria.getId() != null) {
            Predicate id = builder.equal(root.get("user_id"), userSearchCriteria.getId());
            predicates.add(id);
        }

        if (userSearchCriteria.getEmail() != null) {
            Predicate email = builder.like(root.get("email"), userSearchCriteria.getEmail());
            predicates.add(email);
        }

        if (userSearchCriteria.getUserName() != null) {
            Predicate userName = builder.equal(root.get("userName"), userSearchCriteria.getUserName());
            predicates.add(userName);
        }

        if (userSearchCriteria.getFirstName() != null) {
            String searchText = "%" + userSearchCriteria.getFirstName().toLowerCase() + "%";

            Predicate firstName = builder.like(builder.lower(root.get("firstName")), searchText);
            predicates.add(firstName);
        }

        if (userSearchCriteria.getLastName() != null) {
            String searchText = "%" + userSearchCriteria.getLastName().toLowerCase() + "%";

            Predicate lastName = builder.like(builder.lower(root.get("lastName")), searchText);
            predicates.add(lastName);
        }

        if (userSearchCriteria.getActive() != null) {
            Predicate active = builder.equal(root.get("active"), userSearchCriteria.getActive());
            predicates.add(active);
        }

        CriteriaQuery<User> select = criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]))
                .orderBy(builder.asc(root.get("id")));
        TypedQuery<User> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(userSearchCriteria.getFrom());
        typedQuery.setMaxResults(userSearchCriteria.getSize());

        return typedQuery.getResultList();
    }
}
