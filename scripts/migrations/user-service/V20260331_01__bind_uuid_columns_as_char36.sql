-- user-service UUID columns -> CHAR(36)
-- 목적: JPA UUID 식별자를 MySQL CHAR(36) 컬럼으로 통일

-- 1) user_social_accounts.user_id FK 제거
SET @fk_name := (
	SELECT CONSTRAINT_NAME
	FROM information_schema.KEY_COLUMN_USAGE
	WHERE TABLE_SCHEMA = DATABASE()
	  AND TABLE_NAME = 'user_social_accounts'
	  AND COLUMN_NAME = 'user_id'
	  AND REFERENCED_TABLE_NAME = 'users'
	  AND REFERENCED_COLUMN_NAME = 'user_id'
	LIMIT 1
);

SET @ddl := IF(
	@fk_name IS NOT NULL,
	CONCAT('ALTER TABLE user_social_accounts DROP FOREIGN KEY `', @fk_name, '`'),
	'SELECT ''skip drop fk: user_social_accounts.user_id fk does not exist'''
);

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2) UUID columns을 CHAR(36)로 전환
ALTER TABLE users
	MODIFY COLUMN user_id CHAR(36) NOT NULL;

ALTER TABLE user_social_accounts
	MODIFY COLUMN user_social_id CHAR(36) NOT NULL;

ALTER TABLE user_social_accounts
	MODIFY COLUMN user_id CHAR(36) NOT NULL;

-- 3) FK 복구
ALTER TABLE user_social_accounts
	ADD CONSTRAINT fk_user_social_accounts_user
	FOREIGN KEY (user_id) REFERENCES users(user_id);
