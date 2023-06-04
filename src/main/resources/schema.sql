-- Task
create table IF NOT EXISTS Task(
    id serial primary key,
    description varchar(255),
    priority varchar(100),
    created_at timestamp not null,
    updated_at timestamp
);