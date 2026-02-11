---
name: Architecture
about: Structural change to service layers, transport, or backing services
title: "[Architecture] "
labels: architecture
assignees: ""
---

## Value

<!-- Why does this structural change matter? What risk does it mitigate? -->

## Outcome

<!-- What does the architecture look like after this? (One sentence) -->

## Rationale

<!-- Why this approach? What alternatives were considered? -->
<!-- Reference any lessons from Gen 1 (MATILDA), Gen 2 (daemon), or Gen 3v1 if applicable. -->

## Affected Layers

<!-- Check all that apply -->

- [ ] Memory model (types, tiers, attention)
- [ ] Backing service interface
- [ ] Backing service implementation (which?)
- [ ] Transport (stdio / streaming HTTPS)
- [ ] MCP tool definitions
- [ ] Graceful shutdown

## Acceptance Criteria

- [ ] <!-- Define how we verify success -->
- [ ] Decoupling preserved -- no layer depends on another's implementation
- [ ] Graceful shutdown still works
- [ ] Tests pass
- [ ] Reviewed by verifier

## Roles

- **Accountable:**
- **Verifier:**
