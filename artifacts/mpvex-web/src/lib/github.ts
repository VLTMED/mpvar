export interface GitHubContributor {
  login: string;
  id: number;
  avatar_url: string;
  html_url: string;
  contributions: number;
  name?: string;
  bio?: string;
  company?: string;
  location?: string;
  public_repos?: number;
}

const REPO_OWNER = "marlboro-advance";
const REPO_NAME = "mpvEx";
const GITHUB_API_URL = "https://api.github.com";

export async function getRepositoryContributors(
  limit?: number,
): Promise<GitHubContributor[]> {
  try {
    const url = `${GITHUB_API_URL}/repos/${REPO_OWNER}/${REPO_NAME}/contributors?per_page=${limit || 100}&sort=contributions`;

    const response = await fetch(url, {
      headers: {
        Accept: "application/vnd.github.v3+json",
      },
    });

    if (!response.ok) {
      throw new Error(`GitHub API error: ${response.status}`);
    }

    const contributors: GitHubContributor[] = await response.json();
    return contributors;
  } catch (error) {
    console.error("Failed to fetch contributors:", error);
    return [];
  }
}

export async function getRepositoryStats() {
  try {
    const url = `${GITHUB_API_URL}/repos/${REPO_OWNER}/${REPO_NAME}`;

    const response = await fetch(url, {
      headers: {
        Accept: "application/vnd.github.v3+json",
      },
    });

    if (!response.ok) {
      throw new Error(`GitHub API error: ${response.status}`);
    }

    const stats = await response.json();

    return {
      stars: stats.stargazers_count,
      forks: stats.forks_count,
      watchers: stats.watchers_count,
      issues: stats.open_issues_count,
      language: stats.language,
      lastUpdate: stats.updated_at,
    };
  } catch (error) {
    console.error("Failed to fetch repository stats:", error);
    return null;
  }
}

export async function getLatestRelease() {
  try {
    const url = `${GITHUB_API_URL}/repos/${REPO_OWNER}/${REPO_NAME}/releases/latest`;
    const response = await fetch(url, {
      headers: {
        Accept: "application/vnd.github.v3+json",
      },
    });

    if (!response.ok) {
      throw new Error(`GitHub API error: ${response.status}`);
    }

    const release = await response.json();

    return {
      tag_name: release.tag_name,
      name: release.name,
      published_at: release.published_at,
      html_url: release.html_url,
      assets: release.assets,
    };
  } catch (error) {
    console.error("Failed to fetch latest release:", error);
    return null;
  }
}
