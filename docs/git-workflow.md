# 🌟 Git Workflow & Conventions for LinkedHub

## 📋 Overview

This document outlines the Git workflow and conventions for the LinkedHub project. It ensures a consistent, collaborative, and efficient development process across all microservices. The workflow is designed to support a microservices architecture, enabling parallel development while maintaining code quality and stability.

---

## 🚀 Git Workflow

We use a **Gitflow-inspired workflow** tailored for microservices development. This workflow balances simplicity and structure for a team working on multiple services.

### 🔄 Branching Strategy

- **main**: The production-ready branch. It contains the latest stable code that is deployed to production.
- **develop**: The integration branch for upcoming releases. It reflects the latest development changes and is used for testing and staging.
- **feature/**: Feature branches for new features or enhancements (e.g., `feature/add-user-auth`).
- **bugfix/**: Branches for fixing bugs in the `develop` or `main` branch (e.g., `bugfix/fix-login-error`).
- **hotfix/**: Branches for urgent fixes in production (e.g., `hotfix/patch-auth-service`).
- **release/**: Branches for preparing a new release (e.g., `release/v1.0.0`).

#### Workflow Steps

1. **Start a New Feature**:

   - Branch off from `develop`:

     ```bash
     git checkout develop
     git pull
     git checkout -b feature/<feature-name>
     ```
   - Example: `feature/add-user-auth` for adding authentication in the `auth-service`.

2. **Work on the Feature**:

   - Make commits following the commit message convention (see below).
   - Push the branch to the remote repository:

     ```bash
     git push origin feature/<feature-name>
     ```

3. **Create a Pull Request (PR)**:

   - Open a PR from `feature/<feature-name>` to `develop`.
   - Add a detailed description of the changes, including:
     - What was changed.
     - Why it was changed.
     - Any related issues (e.g., `#123`).
   - Request at least one reviewer for code review.

4. **Code Review & Merge**:

   - Address feedback from reviewers.
   - Ensure all CI/CD checks pass (e.g., tests, linting).
   - Merge the PR into `develop` using a **merge commit** (avoid rebasing to preserve history).

5. **Prepare a Release**:

   - Branch off from `develop` to create a release branch:

     ```bash
     git checkout develop
     git pull
     git checkout -b release/vX.Y.Z
     ```
   - Update version numbers, documentation, and perform final testing.
   - Merge into `main` and `develop`:

     ```bash
     git checkout main
     git merge --no-ff release/vX.Y.Z
     git checkout develop
     git merge --no-ff release/vX.Y.Z
     ```
   - Tag the release:

     ```bash
     git tag vX.Y.Z
     git push origin vX.Y.Z
     ```

6. **Hotfix for Production**:

   - Branch off from `main`:

     ```bash
     git checkout main
     git pull
     git checkout -b hotfix/<hotfix-name>
     ```
   - Fix the issue, commit, and create a PR to both `main` and `develop`.
   - Tag the hotfix release (e.g., `vX.Y.Z+1`).

### 🛠️ Microservices Consideration

- Each microservice (e.g., `auth-service`, `profile-service`) should have its own independent lifecycle.
- If a feature spans multiple services, create a feature branch in each service (e.g., `feature/add-user-auth` in `auth-service` and `feature/update-profile` in `profile-service`).
- Use **semantic versioning** for each microservice (e.g., `auth-service:1.0.0`, `profile-service:1.1.0`).

---

## 📝 Commit Message Convention

We follow a structured commit message format to ensure clarity and enable automated changelog generation.

### Format

```
<type>(<scope>): <short description>

[optional body]

[optional footer]
```

#### Types

- `feat`: A new feature (e.g., `feat(auth): add JWT token generation`).
- `fix`: A bug fix (e.g., `fix(profile): resolve null pointer in profile update`).
- `docs`: Documentation changes (e.g., `docs(readme): update setup instructions`).
- `style`: Code style changes (formatting, linting) (e.g., `style(gateway): fix indentation`).
- `refactor`: Code refactoring (e.g., `refactor(auth): simplify login logic`).
- `test`: Adding or updating tests (e.g., `test(profile): add unit tests for profile service`).
- `chore`: Miscellaneous changes (e.g., `chore(deps): update dependencies`).

#### Scope

- The microservice or component affected (e.g., `auth`, `profile`, `gateway`, `docs`).

#### Examples

- `feat(auth): implement user registration endpoint`
- `fix(profile): handle missing skills field in update API`
- `docs(readme): add Git workflow section`

#### Additional Notes

- Keep the short description under 50 characters.
- Use the body for detailed explanations if needed.
- Reference issues in the footer (e.g., `Fixes #123`).

---

## 🔧 Additional Conventions

### Branch Naming

- Use lowercase with hyphens: `feature/add-user-auth`, `bugfix/fix-login-error`.
- Include the ticket number if applicable: `feature/ISSUE-123-add-user-auth`.

### Pull Request Guidelines

- PR titles should follow the commit message format: `feat(auth): add user registration`.
- Ensure PRs are small and focused (one feature or bugfix per PR).
- Include automated tests for new features or bug fixes.
- PRs must pass all CI checks (e.g., unit tests, code linting) before merging.

### Code Quality

- Run linters and formatters before committing (e.g., Checkstyle, Spotless for Java).
- Write unit tests for all new features and bug fixes.
- Use meaningful variable and method names following Java naming conventions.

### Tagging Releases

- Use semantic versioning: `vX.Y.Z` (e.g., `v1.0.0` for initial release, `v1.0.1` for a patch).
- Create annotated tags:

  ```bash
  git tag -a v1.0.0 -m "Release v1.0.0: Initial production release"
  ```

---

## 🛡️ Best Practices

- **Keep Commits Small**: Each commit should represent a single logical change.
- **Pull Frequently**: Regularly pull from `develop` to avoid merge conflicts.
- **Avoid Force Pushes**: Do not force push to shared branches (`main`, `develop`).
- **Backup Before Rebasing**: If rebasing is necessary, ensure you have a backup of your branch.
- **Automate Where Possible**: Use CI/CD pipelines to automate testing, building, and deployment.

---

## 📦 Repository Structure

- Each microservice lives in its own directory (e.g., `auth-service/`, `profile-service/`).
- Shared configurations or scripts are stored in the root directory (e.g., `docker-compose.yml`).

---

🌟 **Happy Coding with LinkedHub!** 🌟