#!/usr/bin/env bash
set -euo pipefail

MODE="${1:-apply}"
BASE_DIR="$(cd "$(dirname "$0")" && pwd)"

require_env() {
	local name="$1"
	if [[ -z "${!name:-}" ]]; then
		echo "Missing required env var: ${name}" >&2
		exit 1
	fi
}

require_env MYSQL_HOST
require_env MYSQL_PORT
require_env MYSQL_DB
require_env MYSQL_USER
require_env MYSQL_PASSWORD

case "$MODE" in
	apply)
		SQL_FILE="${BASE_DIR}/V20260327_01__add_user_social_email.sql"
		;;
	rollback)
		SQL_FILE="${BASE_DIR}/V20260327_01__add_user_social_email__rollback.sql"
		;;
	*)
		echo "Usage: $0 [apply|rollback]" >&2
		exit 1
		;;
esac

echo "Running ${MODE} migration with ${SQL_FILE}"
mysql \
	-h "${MYSQL_HOST}" \
	-P "${MYSQL_PORT}" \
	-u "${MYSQL_USER}" \
	-p"${MYSQL_PASSWORD}" \
	"${MYSQL_DB}" < "${SQL_FILE}"

echo "Done."
