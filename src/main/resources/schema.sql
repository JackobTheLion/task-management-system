CREATE TABLE IF NOT EXISTS public.tms_user
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY UNIQUE NOT NULL,
    email      varchar(256) UNIQUE                            NOT NULL,
    user_name  varchar(256) UNIQUE                            NOT NULL,
    first_name varchar(256),
    last_name  varchar(256),
    password   varchar(256)                                   NOT NULL,
    enabled    boolean
);

CREATE TABLE IF NOT EXISTS public.roles
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY UNIQUE NOT NULL,
    name varchar(20) UNIQUE
);

CREATE TABLE IF NOT EXISTS public.user_roles
(
    user_id bigint not null,
    role_id bigint not null,

    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tms_user (id) ON DELETE CASCADE,
    CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.tasks
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY UNIQUE NOT NULL,
    name        varchar(256)                                   NOT NULL,
    description varchar(3000)                                  NOT NULL,
    status      varchar(20)                                    NOT NULL,
    priority    smallint                                       NOT NULL,
    author_id   BIGINT                                         NOT NULL,
    executor_id BIGINT,
    deleted     BOOLEAN,

    CONSTRAINT fk_task_author_id FOREIGN KEY (author_id) REFERENCES tms_user (id) ON DELETE CASCADE,
    CONSTRAINT fk_task_executor_id FOREIGN KEY (executor_id) REFERENCES tms_user (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY UNIQUE NOT NULL,
    text      varchar(500)                                   NOT NULL,
    task_id   BIGINT                                         NOT NULL,
    author_id BIGINT                                         NOT NULL,

    CONSTRAINT fk_task_comment_id FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    CONSTRAINT fk_author_comment_id FOREIGN KEY (author_id) REFERENCES tms_user (id) ON DELETE CASCADE
);