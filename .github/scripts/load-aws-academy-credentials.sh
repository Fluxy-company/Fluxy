#!/usr/bin/env bash
# Parses the AWS Academy "AWS CLI" credentials block and exports it for later workflow steps.
set -euo pipefail

if [ -z "${AWS_ACADEMY_CREDENTIALS:-}" ]; then
  echo "::error::Secret AWS_ACADEMY_CREDENTIALS is empty or not configured."
  exit 1
fi

trim() {
  local value="$1"
  value="${value#"${value%%[![:space:]]*}"}"
  value="${value%"${value##*[![:space:]]}"}"
  printf '%s' "$value"
}

access_key_id=""
secret_access_key=""
session_token=""

while IFS= read -r line || [ -n "$line" ]; do
  line="$(trim "${line%$'\r'}")"
  case "$line" in
    '' | '#'* | '['*) continue ;;
    *=*) ;;
    *) continue ;;
  esac

  key="$(trim "${line%%=*}")"
  value="$(trim "${line#*=}")"
  case "${key,,}" in
    aws_access_key_id) access_key_id="$value" ;;
    aws_secret_access_key) secret_access_key="$value" ;;
    aws_session_token) session_token="$value" ;;
  esac
done <<< "$AWS_ACADEMY_CREDENTIALS"

missing=()
[ -n "$access_key_id" ] || missing+=(aws_access_key_id)
[ -n "$secret_access_key" ] || missing+=(aws_secret_access_key)
[ -n "$session_token" ] || missing+=(aws_session_token)
if [ "${#missing[@]}" -gt 0 ]; then
  echo "::error::AWS_ACADEMY_CREDENTIALS is missing: ${missing[*]}"
  exit 1
fi

echo "::add-mask::$access_key_id"
echo "::add-mask::$secret_access_key"
echo "::add-mask::$session_token"

{
  echo "AWS_ACCESS_KEY_ID=$access_key_id"
  echo "AWS_SECRET_ACCESS_KEY=$secret_access_key"
  echo "AWS_SESSION_TOKEN=$session_token"
} >> "$GITHUB_ENV"
