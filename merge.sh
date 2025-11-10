#!/bin/bash

DEFAULT_FILE="src/main/resources/application.properties"
UG_UAT_FILE="src/main/resources/application-ug-uat.properties"
CONFIGMAP_YAML="deploy/configmap.yaml"
SECRET_YAML="deploy/secret.yaml"

confidential_patterns=("auth" "password" "secret" "token" "key")

is_confidential() {
  local key="$1"
  for pattern in "${confidential_patterns[@]}"; do
    if [[ "$key" == *"$pattern"* ]]; then
      return 0
    fi
  done
  return 1
}

filter_non_confidential() {
  local file="$1"
  while IFS='=' read -r key value; do
    [[ "$key" =~ ^#.*$ || -z "$key" || -z "$value" ]] && continue
    key=$(echo "$key" | xargs)
    if ! is_confidential "$key"; then
      echo "$key=$value"
    fi
  done < "$file"
}

extract_confidential_block() {
  local file="$1"
  local temp_file=$(mktemp)
  while IFS='=' read -r key value; do
    [[ "$key" =~ ^#.*$ || -z "$key" || -z "$value" ]] && continue
    key=$(echo "$key" | xargs)
    value=$(echo "$value" | xargs)
    if is_confidential "$key"; then
      echo "$key=$value" >> "$temp_file"
    fi
  done < "$file"
  base64 "$temp_file" | tr -d '\n'
  rm "$temp_file"
}

# Generate ConfigMap YAML
{
  echo "apiVersion: v1"
  echo "kind: ConfigMap"
  echo "metadata:"
  echo "  name: accountmanagement-config"
  echo "data:"
  echo "  application.properties: |-"
  filter_non_confidential "$DEFAULT_FILE" | sed 's/^/    /'
  echo "  application-ug-uat.properties: |-"
  filter_non_confidential "$UG_UAT_FILE" | sed 's/^/    /'
} > "$CONFIGMAP_YAML"

# Generate Secret YAML
{
  echo "apiVersion: v1"
  echo "kind: Secret"
  echo "metadata:"
  echo "  name: accountmanagement-secret"
  echo "type: Opaque"
  echo "data:"
  echo -n "  application.properties: "
  extract_confidential_block "$DEFAULT_FILE"
  echo
  echo -n "  application-ug-uat.properties: "
  extract_confidential_block "$UG_UAT_FILE"
  echo
} > "$SECRET_YAML"

echo "✅ Generated $CONFIGMAP_YAML and $SECRET_YAML with proper formatting."
