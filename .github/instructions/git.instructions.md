---
applyTo: "**"
---

# Git Best Practices for LinkedHub

## Branching Strategy
- Use a Gitflow-inspired workflow:
  - `main`: Production-ready, stable code.
  - `dev`: Integration branch for upcoming releases.
  - `feature/<name>`: New features or enhancements.
  - `bugfix/<name>`: Bug fixes for `dev` or `main`.
  - `hotfix/<name>`: Urgent production fixes.
  - `release/vX.Y.Z`: Release preparation.
- Branch from `dev` for features/bugfixes, from `main` for hotfixes.
- Use lowercase, hyphenated branch names. Include ticket numbers if applicable.

## Commit Message Convention
- Format: `<type> (<scope>): <short description>`
- Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
- Scope: Microservice/component (e.g., `auth`, `profile`, `gateway`, `docs`)
- Keep short description under 50 characters.
- Reference issues in the footer (e.g., `Fixes #123`).

## Pull Request Guidelines
- PR titles follow commit message format.
- PRs should be small, focused, and include tests for new features/bugfixes.
- All PRs require at least one reviewer and must pass CI checks.

## Code Quality
- Run linters/formatters before committing (e.g., Checkstyle, Spotless for Java).
- Write unit tests for all new features and bug fixes.
- Use meaningful names and follow Java conventions.

## Tagging Releases
- Use semantic versioning: `vX.Y.Z` (e.g., `v1.0.0`).
- Create annotated tags: `git tag -a v1.0.0 -m "Release v1.0.0: Initial production release"`

## Best Practices
- Keep commits small and focused.
- Pull frequently from `dev` to avoid conflicts.
- Avoid force pushes to shared branches.
- Backup before rebasing.
- Automate testing, building, and deployment with CI/CD.

## Microservices Consideration
- Each microservice has its own lifecycle and versioning.
- For cross-service features, create a feature branch in each relevant service.

## Repository Structure
- Each microservice in its own directory (e.g., `auth-service/`).
- Shared configs/scripts in the root (e.g., `docker-compose.yml`).

---

For more details, see `docs/git-workflow.md`.
