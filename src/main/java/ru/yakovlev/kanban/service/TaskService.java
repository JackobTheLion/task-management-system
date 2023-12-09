package ru.yakovlev.kanban.service;

import ru.yakovlev.kanban.dto.task.*;
import ru.yakovlev.kanban.model.task.Status;

import java.security.Principal;
import java.util.List;

/**
 * Interface for working with tasks and its comments.
 */
public interface TaskService {

    /**
     * Adding a new task by a user.
     *
     * @param task      provided task
     * @param principal current user.
     * @return {@link TaskDtoFullResponse} saved task with id
     */
    TaskDtoFullResponse addTask(TaskDtoRequest task, Principal principal);

    /**
     * Updating task by curren user.
     *
     * @param task      updated task information
     * @param principal curren user
     * @param taskId    task id to update
     * @return {@link TaskDtoFullResponse} updated task
     */
    TaskDtoFullResponse updateTaskUser(TaskDtoRequest task, Principal principal, Long taskId);

    /**
     * Updating task by admin.
     *
     * @param updatedTask updated task information
     * @param taskId      task id to update
     * @return {@link TaskDtoFullResponse} updated task
     */
    TaskDtoFullResponse updateTaskAdmin(TaskDtoRequest updatedTask, Long taskId);

    /**
     * Searching tasks based on search criteria.
     * {@link TaskSearchCriteria}
     *
     * @param taskSearchCriteria {@link TaskSearchCriteria} criteria for task search.
     * @return {@link TaskDtoShortResponse} list of found tasks
     */
    List<TaskDtoShortResponse> findTasks(TaskSearchCriteria taskSearchCriteria);

    TaskDtoFullResponse findTaskById(Long id);

    /**
     * Status update for task.
     *
     * @param taskId    task id to update
     * @param status    new status
     * @param principal current user
     * @return {@link TaskDtoFullResponse} updated task
     */
    TaskDtoFullResponse updateStatus(Long taskId, Status status, Principal principal);

    /**
     * Adding new comment to a task
     *
     * @param commentDtoRequest text of comment
     * @param principal         current user
     * @param taskId            task id which should be commented
     * @return {@link CommentDtoResponse} saved comment
     */
    CommentDtoResponse addComment(CommentDtoRequest commentDtoRequest, Principal principal, Long taskId);

    /**
     * Deleting task.
     *
     * @param taskId    task id to delete
     * @param principal current user
     */
    void markTaskAsDeleted(Long taskId, Principal principal);

    /**
     * Admin delete task
     *
     * @param taskId    task id to delete
     * @param principal current user - admin
     */
    void markTaskAsDeletedByAdmin(Long taskId, Principal principal);

}
