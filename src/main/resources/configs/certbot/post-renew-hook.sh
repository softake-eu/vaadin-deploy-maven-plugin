#!/bin/sh

# Define variables
CERT_SOURCE_DIR="/etc/letsencrypt/live"
CERT_DEST_DIR="/etc/nginx/certs"

# Create destination directory if it doesn't exist
mkdir -p "$CERT_DEST_DIR"

# Copy certificates and override existing files
for domain_dir in "$CERT_SOURCE_DIR"/*; do
    if [ -d "$domain_dir" ]; then
        domain=$(basename "$domain_dir")
        echo "$(date) - Processing certificates for $domain"

        # Copy necessary files
        cp -f "$domain_dir/fullchain.pem" "$CERT_DEST_DIR/fullchain.pem"
        cp -f "$domain_dir/privkey.pem" "$CERT_DEST_DIR/privkey.pem"

        echo "$(date) - Certificates for $domain copied to $CERT_DEST_DIR"
    fi
done

echo "$(date) - Post-renew hook completed successfully"
exit 0