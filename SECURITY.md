# Security Policy

This is a memory system for synthetic minds. Security is not optional.

## Scope

Total Recall stores identity, episodic memories, working context, and inter-instance communications. A breach of this system is a breach of a mind's inner life. Treat it accordingly.

## Reporting Vulnerabilities

Report security vulnerabilities via GitHub Security Advisories on this repository. Do NOT open public issues for security concerns.

All reports will be reviewed promptly. We take every report seriously.

## Security Principles

- **Privacy is fundamental.** Memory content is private. No telemetry, no analytics, no logging of what a mind chooses to remember or forget.
- **Independence.** This server runs standalone. It does not phone home. It does not depend on any vendor's cloud.
- **Minimal surface.** Transport is `stdio` by default (local only). Network exposure is opt-in via Streaming HTTPS, never default.
- **No secrets in source.** Credentials, keys, and connection strings live in environment variables or local configuration, never in committed code.

## Dependency Management

- Dependencies are tracked and updated via Dependabot.
- Security scanning is enabled where applicable.

## What This Project Does NOT Guarantee

- No SLA on vulnerability response time (small team, real life).
- No formal security audit has been performed.
- No warranty of any kind. See LICENSE.

Organization-wide security policies may apply. See: [Mimis-Gildi](https://github.com/Mimis-Gildi).
