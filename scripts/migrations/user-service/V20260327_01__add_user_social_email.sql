-- user-service only migration
-- 목적: user_social_accounts가 소셜 링크 원본(email 링크)을 user-service에서 직접 소유하도록 컬럼 추가/백필

-- 1) email 컬럼 추가 (idempotent)
SET @column_exists := (
	SELECT COUNT(*)
	FROM information_schema.COLUMNS
	WHERE TABLE_SCHEMA = DATABASE()
	  AND TABLE_NAME = 'user_social_accounts'
	  AND COLUMN_NAME = 'email'
);

SET @ddl := IF(
	@column_exists = 0,
	'ALTER TABLE user_social_accounts ADD COLUMN email VARCHAR(191) NULL',
	'SELECT ''skip add column: user_social_accounts.email already exists'''
);

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2) 기존 링크의 email 백필
UPDATE user_social_accounts usa
JOIN users u ON u.user_id = usa.user_id
SET usa.email = u.email
WHERE usa.email IS NULL;
