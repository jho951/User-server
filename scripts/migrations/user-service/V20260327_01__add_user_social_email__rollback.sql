-- rollback for V20260327_01__add_user_social_email.sql
-- 주의: email 컬럼 데이터가 모두 삭제됩니다.

SET @column_exists := (
	SELECT COUNT(*)
	FROM information_schema.COLUMNS
	WHERE TABLE_SCHEMA = DATABASE()
	  AND TABLE_NAME = 'user_social_accounts'
	  AND COLUMN_NAME = 'email'
);

SET @ddl := IF(
	@column_exists = 1,
	'ALTER TABLE user_social_accounts DROP COLUMN email',
	'SELECT ''skip drop column: user_social_accounts.email does not exist'''
);

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
