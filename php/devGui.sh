#!/usr/bin/env bash
set -euo pipefail

root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
port=${PHP_GUI_PORT:-8080}

if [[ ! -f "$root/index.php" ]]; then
    printf 'Metti devGui.sh accanto al file index.php della GUI.\n' >&2
    exit 1
fi

if [[ ! $port =~ ^[0-9]{1,5}$ ]] || (( 10#$port < 1 || 10#$port > 65535 )); then
    printf 'PHP_GUI_PORT deve essere un numero tra 1 e 65535.\n' >&2
    exit 2
fi
port=$((10#$port))

if ! command -v docker >/dev/null; then
    printf 'Docker non trovato. Su Kali puoi installarlo con sudo apt install docker.io.\n' >&2
    exit 1
fi

if ! docker info >/dev/null; then
    if (( EUID != 0 )); then
        printf 'Se Docker segnala un errore di permessi, prova sudo bash devGui.sh dalla cartella php/.\n' >&2
    fi
    exit 1
fi

# Con sudo, mantieni l'utente originale per i file scritti dai progetti.
uid=${SUDO_UID:-$(id -u)}
gid=${SUDO_GID:-$(id -g)}

printf 'GUI disponibile all’avvio su http://127.0.0.1:%s/ (Ctrl+C per fermarla).\n' "$port"
exec docker run --rm --init \
    --publish "127.0.0.1:$port:8000" \
    --volume "$root:/app" \
    --workdir /app \
    --user "$uid:$gid" \
    php:8.3-cli php -d display_errors=1 -S 0.0.0.0:8000
