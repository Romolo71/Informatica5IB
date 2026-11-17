#!/usr/bin/env bash
set -euo pipefail

usage() {
    printf 'Uso: bash dev.sh [nome-progetto]\n'
    printf 'Senza argomenti mostra il menu. Imposta PHP_DEV_PORT per cambiare porta.\n'
}

if [[ ${1-} == --help || ${1-} == -h ]]; then
    usage
    exit 0
fi

if (( $# > 1 )); then
    usage >&2
    exit 2
fi

root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
shopt -s nullglob
projects=()
for directory in "$root"/*/; do
    directory=${directory%/}
    if [[ -L "$directory" ]]; then
        continue
    fi
    if [[ -f "$directory/index.php" || -f "$directory/public/index.php" ]]; then
        projects+=("${directory##*/}")
    fi
done

if (( ${#projects[@]} == 0 )); then
    printf 'Nessun progetto con index.php o public/index.php in %s.\n' "$root" >&2
    exit 1
fi

if (( $# == 1 )); then
    project=''
    for candidate in "${projects[@]}"; do
        if [[ $candidate == "$1" ]]; then
            project=$candidate
            break
        fi
    done
    if [[ -z $project ]]; then
        printf 'Progetto non trovato: %s\n' "$1" >&2
        printf '  %s\n' "${projects[@]}" >&2
        exit 2
    fi
else
    PS3='Scegli il numero del progetto: '
    project=''
    select project in "${projects[@]}"; do
        if [[ -n $project ]]; then
            break
        fi
        printf 'Seleziona un numero dal menu.\n' >&2
    done
    [[ -n $project ]] || exit 0
fi

port=${PHP_DEV_PORT:-8080}
if [[ ! $port =~ ^[0-9]{1,5}$ ]] || (( 10#$port < 1 || 10#$port > 65535 )); then
    printf 'PHP_DEV_PORT deve essere un numero tra 1 e 65535.\n' >&2
    exit 2
fi
port=$((10#$port))

if ! command -v docker >/dev/null; then
    printf 'Docker non trovato. Su Kali puoi installarlo con sudo apt install docker.io.\n' >&2
    exit 1
fi

project_dir="$root/$project"
document_root=/app
if [[ -f "$project_dir/public/index.php" ]]; then
    document_root=/app/public
fi

# Il Dockerfile condiviso è incorporato nello script. Docker riusa i layer in cache.
printf 'Preparo PHP 8.3 per %s. La prima build può richiedere qualche minuto.\n' "$project"
image=$(docker build --quiet - <<'DOCKERFILE'
FROM php:8.3-cli
RUN docker-php-ext-install pdo_mysql mysqli \
    && pecl install xdebug \
    && docker-php-ext-enable xdebug
DOCKERFILE
)

printf 'Avvio %s su http://127.0.0.1:%s/ (Ctrl+C per fermarlo).\n' "$project" "$port"
exec docker run --rm --init \
    --publish "127.0.0.1:$port:8000" \
    --add-host host.docker.internal:host-gateway \
    --user "$(id -u):$(id -g)" \
    --volume "$project_dir:/app" \
    --workdir /app \
    "$image" \
    php -d error_reporting=-1 -d display_errors=1 -d log_errors=1 -d error_log=/dev/stderr \
    -d xdebug.mode=debug,develop -d xdebug.start_with_request=trigger \
    -d xdebug.client_host=host.docker.internal -d xdebug.client_port=9003 \
    -S 0.0.0.0:8000 -t "$document_root"
