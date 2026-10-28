#!/usr/bin/env python3
"""Loads IEFC secrets from AWS Secrets Manager into /opt/iefc at boot.

Usage: iefc-load-secrets <region> <app-env-secret> <jwt-secret|-> <keys|*>
"""
import json
import os
import subprocess
import sys
import time

BASE = "/opt/iefc"
ATTEMPTS = 90
DELAY_SECONDS = 10


def fetch(region, secret_id):
    for attempt in range(1, ATTEMPTS + 1):
        result = subprocess.run(
            ["aws", "secretsmanager", "get-secret-value", "--region", region,
             "--secret-id", secret_id, "--query", "SecretString", "--output", "text"],
            capture_output=True, text=True,
        )
        if result.returncode == 0:
            return json.loads(result.stdout)
        print(f"[{attempt}/{ATTEMPTS}] waiting for {secret_id}: {result.stderr.strip()[:300]}", file=sys.stderr)
        time.sleep(DELAY_SECONDS)
    sys.exit(f"secret {secret_id} is unavailable")


def dotenv_value(value):
    value = str(value)
    if "'" not in value and "\n" not in value:
        return f"'{value}'"
    escaped = value.replace("\\", "\\\\").replace('"', '\\"').replace("$", "$$").replace("\n", "\\n")
    return f'"{escaped}"'


def write(path, content, mode):
    tmp = f"{path}.tmp"
    fd = os.open(tmp, os.O_WRONLY | os.O_CREAT | os.O_TRUNC, 0o600)
    with os.fdopen(fd, "w") as handle:
        handle.write(content)
    os.chmod(tmp, mode)
    os.replace(tmp, path)


def main():
    region, app_secret, jwt_secret, keys = sys.argv[1:5]
    settings = fetch(region, app_secret)
    if keys != "*":
        wanted = keys.split(",")
        missing = [key for key in wanted if key not in settings]
        if missing:
            sys.exit(f"{app_secret} is missing: {', '.join(missing)}")
        settings = {key: settings[key] for key in wanted}
    env = "".join(f"{key}={dotenv_value(value)}\n" for key, value in sorted(settings.items()))
    write(f"{BASE}/.env", env, 0o600)
    print(f"wrote {BASE}/.env with {len(settings)} keys from {app_secret}")

    if jwt_secret != "-":
        jwt = fetch(region, jwt_secret)
        os.makedirs(f"{BASE}/secrets", mode=0o700, exist_ok=True)
        # The container runs as a non-root user; the parent directory stays root-only.
        write(f"{BASE}/secrets/jwt-private.pem", jwt["private_key_pem"], 0o444)
        write(f"{BASE}/secrets/jwt-public.pem", jwt["public_key_pem"], 0o444)
        print(f"wrote JWT key pair from {jwt_secret}")


if __name__ == "__main__":
    main()
