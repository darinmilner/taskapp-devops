-- Task
create table IF NOT EXISTS Task(
    id serial primary key,
    description varchar(255),
    priority varchar(100),
    is_task_open boolean,
    is_reminder_set boolean,
    created_at timestamp not null,
    updated_at timestamp
);