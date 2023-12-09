package ru.yakovlev.tsm.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yakovlev.tsm.dto.task.*;
import ru.yakovlev.tsm.exception.exceptions.AccessDeniedException;
import ru.yakovlev.tsm.exception.exceptions.NotFoundException;
import ru.yakovlev.tsm.mapper.CommentMapper;
import ru.yakovlev.tsm.mapper.TaskMapper;
import ru.yakovlev.tsm.model.task.Comment;
import ru.yakovlev.tsm.model.task.Status;
import ru.yakovlev.tsm.model.task.Task;
import ru.yakovlev.tsm.model.user.User;
import ru.yakovlev.tsm.repository.CommentRepository;
import ru.yakovlev.tsm.repository.CustomTaskRepository;
import ru.yakovlev.tsm.repository.TaskRepository;
import ru.yakovlev.tsm.service.TaskService;
import ru.yakovlev.tsm.service.UserService;

import java.security.Principal;
import java.util.List;

/**
 * Implementation of {@link TaskService}
 */

@Service
@Slf4j
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final CustomTaskRepository customTaskRepository;

    private final CommentRepository commentRepository;

    private final UserService userService;

    /**
     * Author of task is currently logged in user.
     *
     * @param taskDtoRequest provided task
     * @param principal      current user.
     * @return {@link TaskDtoFullResponse}
     */
    @Override
    @Transactional
    public TaskDtoFullResponse addTask(TaskDtoRequest taskDtoRequest, Principal principal) {
        log.trace("Adding new task: {}", taskDtoRequest);
        Task task = TaskMapper.mapFromDto(taskDtoRequest);
        task.setAuthor(userService.findUserByName(principal.getName()));

        if (taskDtoRequest.getExecutor() != null) {
            task.setExecutor(userService.findUserById(taskDtoRequest.getExecutor()));
        }

        Task savedtask = taskRepository.save(task);
        log.trace("Task saved: {}.", savedtask);
        return TaskMapper.mapToFullDto(savedtask);
    }

    /**
     * Only author can update task information.
     *
     * @param updatedTask updated task information
     * @param principal   curren user
     * @param taskId      task id to update
     * @return {@link TaskDtoFullResponse}
     */
    @Override
    @Transactional
    public TaskDtoFullResponse updateTaskUser(TaskDtoRequest updatedTask, Principal principal, Long taskId) {
        Task taskToUpdate = findTask(taskId);
        if (taskToUpdate.getAuthor().getUsername().equals(principal.getName())) {
            updateTaskFields(updatedTask, taskToUpdate);
        } else {
            log.error("User {} cannot update task {}.", principal.getName(), taskToUpdate);
            throw new AccessDeniedException("Only author can update task");
        }
        Task savedTask = taskRepository.save(taskToUpdate);
        log.trace("Task updated: {}", savedTask);
        return TaskMapper.mapToFullDto(savedTask);
    }

    /**
     * Update task information by admin.
     *
     * @param updatedTask updated task information
     * @param taskId      task id to update
     * @return {@link TaskDtoFullResponse}
     */
    @Override
    @Transactional
    public TaskDtoFullResponse updateTaskAdmin(TaskDtoRequest updatedTask, Long taskId) {
        Task taskToUpdate = findTask(taskId);
        updateTaskFields(updatedTask, taskToUpdate);
        Task savedTask = taskRepository.save(taskToUpdate);
        log.trace("Task updated: {}", savedTask);
        return TaskMapper.mapToFullDto(savedTask);
    }

    /**
     * Only executor can update task status.
     *
     * @param taskId    task id to update
     * @param status    new status
     * @param principal current user
     * @return {@link TaskDtoFullResponse}
     */
    @Override
    @Transactional
    public TaskDtoFullResponse updateStatus(Long taskId, Status status, Principal principal) {
        Task taskToUpdate = findTask(taskId);

        if (taskToUpdate.getExecutor().getUsername().equals(principal.getName())) {
            taskToUpdate.setStatus(status);
        } else {
            log.error("User {} cannot update status of task {}.", principal.getName(), taskToUpdate);
            throw new AccessDeniedException("Only executor can update task status");
        }

        Task updatedTask = taskRepository.save(taskToUpdate);
        log.trace("Task status updated: {}.", updatedTask);
        return TaskMapper.mapToFullDto(updatedTask);
    }

    /**
     * Finding tasks by search criteria.
     *
     * @param taskSearchCriteria {@link TaskSearchCriteria} criteria for task search.
     * @return {@link TaskDtoShortResponse} list of task found
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskDtoShortResponse> findTasks(TaskSearchCriteria taskSearchCriteria) {
        log.trace("Searching tasks: {}.", taskSearchCriteria);
        List<Task> tasks = customTaskRepository.findTasks(taskSearchCriteria);
        log.trace("Number of tasks found: {}", tasks.size());
        return tasks.stream().map(TaskMapper::mapToShortDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDtoFullResponse findTaskById(Long id) {
        return TaskMapper.mapToFullDto(findTask(id));
    }

    /**
     * Adding new comment to a task
     *
     * @param commentDtoRequest text of comment
     * @param principal         current user
     * @param taskId            task id which should be commented
     * @return {@link CommentDtoResponse} saved comment
     */
    @Override
    @Transactional
    public CommentDtoResponse addComment(CommentDtoRequest commentDtoRequest, Principal principal, Long taskId) {
        log.trace("Adding comment: {}.", commentDtoRequest);
        Comment comment = CommentMapper.mapFromDto(commentDtoRequest);

        User author = userService.findUserByName(principal.getName());
        comment.setAuthor(author);
        Task task = findTask(taskId);
        comment.setTask(task);

        Comment savedComment = commentRepository.save(comment);
        log.trace("Comment added: {}.", savedComment);
        return CommentMapper.mapToDto(savedComment);
    }

    /**
     * Deleting task. Checking whether request comes from author, if no throw exception.
     *
     * @param taskId    task id to delete
     * @param principal current user
     */
    @Override
    @Transactional
    public void markTaskAsDeleted(Long taskId, Principal principal) {
        log.trace("Deleting task {} by {}", taskId, principal.getName());
        Task task = taskRepository.findTaskByIdAndAuthorUserName(taskId, principal.getName()).orElseThrow(() -> {
            log.error("Task id {} does not exist or user {} have no rights to delete", taskId, principal.getName());
            return new NotFoundException(String.format("Task id %s not found", taskId));
        });
        task.setDeleted(true);
        taskRepository.save(task);
        log.trace("Task {} marked deleted.", taskId);
    }

    /**
     * Admin delete task
     *
     * @param taskId    task id to delete
     * @param principal current user - admin
     */
    @Override
    @Transactional
    public void markTaskAsDeletedByAdmin(Long taskId, Principal principal) {
        log.trace("Deleting task {} by {}", taskId, principal.getName());
        Task task = findTask(taskId);
        task.setDeleted(true);
        taskRepository.save(task);
        log.trace("Task {} deleted.", taskId);
    }

    private Task findTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("Task id {} not found", taskId);
            return new NotFoundException(String.format("Task id %s not found", taskId));
        });
    }

    private void updateTaskFields(TaskDtoRequest updatedTask, Task taskToUpdate) {
        if (updatedTask.getName() != null) {
            taskToUpdate.setName(updatedTask.getName());
        }

        if (updatedTask.getDescription() != null) {
            taskToUpdate.setDescription(updatedTask.getDescription());
        }

        if (updatedTask.getStatus() != null) {
            taskToUpdate.setStatus(updatedTask.getStatus());
        }

        if (updatedTask.getPriority() != null) {
            taskToUpdate.setPriority(updatedTask.getPriority());
        }

        if (updatedTask.getExecutor() != null) {
            User executor = userService.findUserById(updatedTask.getExecutor());
            taskToUpdate.setExecutor(executor);
        }

        if (updatedTask.getDeleted() != null) {
            taskToUpdate.setDeleted(updatedTask.getDeleted());
        }
    }
}
