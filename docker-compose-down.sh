#!/usr/bin/env sh

echo '[INFO] Down IT Docker containers...'
docker compose --file docker-compose.yml down
