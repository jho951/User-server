#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
PROFILE="${1:-dev}"
MSA_SHARED_NETWORK="${MSA_SHARED_NETWORK:-msa-shared}"

case "${PROFILE}" in
  dev|prod)
    ;;
  *)
    echo "Usage: $0 [dev|prod]" >&2
    exit 1
    ;;
esac

cd "${REPO_ROOT}/docker"

if ! docker network inspect "${MSA_SHARED_NETWORK}" >/dev/null 2>&1; then
  echo "Creating external docker network: ${MSA_SHARED_NETWORK}"
  docker network create "${MSA_SHARED_NETWORK}" >/dev/null
fi

docker compose -f "docker-compose.${PROFILE}.yml" up --build -d
