#!/bin/bash

DEFAULT_FILE="src/main/resources/application.properties"
UG_UAT_FILE="src/main/resources/application-ug-uat.properties"
CONFIGMAP_YAML="deploy/configmap-merged.yaml"
SECRET_YAML="deploy/secret-merged.yaml"

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

filter_confidential_encoded() {
  local file="$1"
  while IFS='=' read -r key value; do
    [[ "$key" =~ ^#.*$ || -z "$key" || -z "$value" ]] && continue
    key=$(echo "$key" | xargs)
    value=$(echo "$value" | xargs)
    if is_confidential "$key"; then
      encoded=$(echo -n "$value" | base64)
      echo "$key=$encoded"
    fi
  done < "$file"
}

# Update ConfigMap YAML
{
  echo "apiVersion: v1"
  echo "kind: ConfigMap"
  echo "metadata:"
  echo "  name: merged-config"
  echo "  namespace: pipelines-tutorial"
  echo "immutable: false"
  echo "data:"
  echo "  application.properties: |-"
  filter_non_confidential "$DEFAULT_FILE" | sed 's/^/    /'
  echo "  application-ug-uat.properties: |-"
  filter_non_confidential "$UG_UAT_FILE" | sed 's/^/    /'
} > "$CONFIGMAP_YAML"

# Update Secret YAML with grouped base64-encoded values
{
  echo "apiVersion: v1"
  echo "kind: Secret"
  echo "metadata:"
  echo "  name: merged-secret"
  echo "  namespace: pipelines-tutorial"
  echo "type: Opaque"
  echo "data:"
  echo "  application.properties: |-"
  filter_confidential_encoded "$DEFAULT_FILE" | sed 's/^/    /'
  echo "  application-ug-uat.properties: |-"
  filter_confidential_encoded "$UG_UAT_FILE" | sed 's/^/    /'
} > "$SECRET_YAML"

echo "Updated $CONFIGMAP_YAML and $SECRET_YAML with filtered properties."
