INSERT INTO task
(`id`, `description`, `is_reminder_set`, `is_task_open`, `priority`, `created_at`, `updated_at`)
VALUES (111, 'test description1', false, false, 'LOW', CURRENT_TIME(), CURRENT_TIME());

INSERT INTO task
(`id`, `description`, `is_reminder_set`, `is_task_open`, `priority`, `created_at`, `updated_at`)
VALUES (112, 'test description2', true, false, 'HIGH', CURRENT_TIME(), CURRENT_TIME());

INSERT INTO task
(`id`, `description`, `is_reminder_set`, `is_task_open`, `priority`, `created_at`, `updated_at`)
VALUES (113, 'test description3', false, true, 'LOW', CURRENT_TIME(), CURRENT_TIME());
