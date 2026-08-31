-- One-time migration for databases created before User and Task IDs became UUIDs.
-- Run this script against the taskflow database while the application is stopped.
-- It preserves existing rows by assigning each legacy numeric user ID a UUID.

SET FOREIGN_KEY_CHECKS = 0;

CREATE TEMPORARY TABLE taskflow_user_id_uuid_map (
    old_id BIGINT NOT NULL PRIMARY KEY,
    new_id VARCHAR(36) NOT NULL UNIQUE
);

INSERT INTO taskflow_user_id_uuid_map (old_id, new_id)
SELECT id, UUID()
FROM users;

-- Remove constraints that currently point to users.id BIGINT.
ALTER TABLE tasks
    DROP FOREIGN KEY FK6s1ob9k4ihi75xbxe2w0ylsdh;
ALTER TABLE user_addresses
    DROP FOREIGN KEY FKn2fisxyyu3l9wlch3ve2nocgp;
ALTER TABLE user_preferences
    DROP FOREIGN KEY FKepakpib0qnm82vmaiismkqf88;
ALTER TABLE user_profiles
    DROP FOREIGN KEY FKjcad5nfve11khsnpwj1mv8frj;

-- Convert dependent columns and replace numeric values with their new UUIDs.
ALTER TABLE tasks
    MODIFY COLUMN user_id VARCHAR(36) NULL;
UPDATE tasks t
JOIN taskflow_user_id_uuid_map m
    ON t.user_id = CAST(m.old_id AS CHAR)
SET t.user_id = m.new_id;

ALTER TABLE user_addresses
    MODIFY COLUMN user_id VARCHAR(36) NOT NULL;
UPDATE user_addresses a
JOIN taskflow_user_id_uuid_map m
    ON a.user_id = CAST(m.old_id AS CHAR)
SET a.user_id = m.new_id;

ALTER TABLE user_preferences
    MODIFY COLUMN user_id VARCHAR(36) NOT NULL;
UPDATE user_preferences p
JOIN taskflow_user_id_uuid_map m
    ON p.user_id = CAST(m.old_id AS CHAR)
SET p.user_id = m.new_id;

ALTER TABLE user_profiles
    MODIFY COLUMN user_id VARCHAR(36) NOT NULL;
UPDATE user_profiles p
JOIN taskflow_user_id_uuid_map m
    ON p.user_id = CAST(m.old_id AS CHAR)
SET p.user_id = m.new_id;

-- task_users was created with string columns but may contain legacy numeric values.
UPDATE task_users tu
JOIN taskflow_user_id_uuid_map m
    ON tu.user_id = CAST(m.old_id AS CHAR)
SET tu.user_id = m.new_id;

-- Replace the users primary key while retaining all user data.
ALTER TABLE users
    ADD COLUMN uuid_id VARCHAR(36) NULL;
UPDATE users u
JOIN taskflow_user_id_uuid_map m
    ON u.id = m.old_id
SET u.uuid_id = m.new_id;

-- Remove AUTO_INCREMENT before replacing the primary key column.
ALTER TABLE users
    MODIFY COLUMN id BIGINT NOT NULL;
ALTER TABLE users
    DROP PRIMARY KEY;
ALTER TABLE users
    DROP COLUMN id;
ALTER TABLE users
    CHANGE COLUMN uuid_id id VARCHAR(36) NOT NULL;
ALTER TABLE users
    ADD PRIMARY KEY (id);

-- Rebuild the relationships using the new UUID columns.
ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_user_uuid
    FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE user_addresses
    ADD CONSTRAINT fk_user_addresses_user_uuid
    FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE user_preferences
    ADD CONSTRAINT fk_user_preferences_user_uuid
    FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE user_profiles
    ADD CONSTRAINT fk_user_profiles_user_uuid
    FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE task_users
    ADD CONSTRAINT fk_task_users_user_uuid
    FOREIGN KEY (user_id) REFERENCES users (id);

DROP TEMPORARY TABLE taskflow_user_id_uuid_map;
SET FOREIGN_KEY_CHECKS = 1;
