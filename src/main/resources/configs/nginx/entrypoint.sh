#!/bin/sh
# Start nginx
/docker-entrypoint.sh nginx

entrypoint_log() {
    if [ -z "${NGINX_ENTRYPOINT_QUIET_LOGS:-}" ]; then
        echo "$@"
    fi
}

# Monitor certs changes
FILE_TO_MONITOR="/etc/nginx/certs/fullchain.pem"
# Check if the file exists
if [ ! -f "$FILE_TO_MONITOR" ]; then
    entrypoint_log "$(date) - File $FILE_TO_MONITOR does not exist. Exiting."
    exit 1
fi

# Get the initial modification timestamp
LAST_MODIFIED=$(stat -c %Y "$FILE_TO_MONITOR")

entrypoint_log "$(date) - Monitoring changes to $FILE_TO_MONITOR. Press Ctrl+C to stop."

while true; do
    # Get the current modification timestamp
    CURRENT_MODIFIED=$(stat -c %Y "$FILE_TO_MONITOR")

    # Compare the timestamps to check if the file was modified
    if [ "$CURRENT_MODIFIED" -ne "$LAST_MODIFIED" ]; then
        entrypoint_log "$(date) - File modified at $(date)"
        nginx -s reload
        LAST_MODIFIED="$CURRENT_MODIFIED"
    fi

    # Wait for 15 seconds before checking again
    sleep 15
done