#!/usr/bin/env bash
set -euo pipefail

DEFAULT_FILE_PROP="src/main/resources/application.properties"
UG_PROD_FILE_PROP="src/main/resources/application-ug-prod.properties"
CONFIGMAP_YAML="deploy/configmap.yaml"

emit_raw() {
  local file="${1:-}"
  [[ -r "$file" ]] || { echo "ERROR: cannot read $file" >&2; return 1; }
  # Normalize CRLF to LF to be safe
  sed -e 's/\r$//' "$file"
}

{
  echo "apiVersion: v1"
  echo "kind: ConfigMap"
  echo "metadata:"
  echo "  name: accountmanagement-config"
  echo "data:"
  echo "  application.properties: |-"
  emit_raw "$DEFAULT_FILE_PROP" | sed 's/^/    /'
  # **Ensure a newline after the block content**
  printf '\n'

  echo "  application-ug-prod.properties: |-"
  emit_raw "$UG_PROD_FILE_PROP" | sed 's/^/    /'
  # **Ensure a newline after the block content**
  printf '\n'
} > "$CONFIGMAP_YAML"

echo "Generated $CONFIGMAP_YAML."


