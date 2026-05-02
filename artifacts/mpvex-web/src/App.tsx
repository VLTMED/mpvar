import { useEffect, useState } from "react";
import { ThemeProvider } from "@/lib/theme-context";
import { Header } from "@/components/sections/header";
import { HeroSection } from "@/components/sections/hero";
import { FeaturesSection } from "@/components/sections/features";
import { DownloadsSection } from "@/components/sections/downloads";
import { ScreenshotsSection } from "@/components/sections/screenshots";
import { ContributorsSection } from "@/components/sections/contributors";
import { Footer } from "@/components/sections/footer";
import { getLatestRelease, getRepositoryStats, getRepositoryContributors } from "@/lib/github";
import { siteConfig } from "@/lib/site";

function HomePage() {
  const [latestRelease, setLatestRelease] = useState<{ tag_name: string; html_url: string } | null>(null);
  const [repoStats, setRepoStats] = useState<{ stars: number } | null>(null);
  const [contributorsCount, setContributorsCount] = useState<number | undefined>(undefined);

  useEffect(() => {
    getLatestRelease().then(setLatestRelease);
    getRepositoryStats().then(setRepoStats);
    getRepositoryContributors(100).then((c) => setContributorsCount(c.length));
  }, []);

  const downloadUrl = latestRelease?.html_url;
  const version = latestRelease?.tag_name || siteConfig.version;

  return (
    <main className="bg-background text-foreground">
      <Header downloadUrl={downloadUrl} />
      <HeroSection
        version={version}
        downloadUrl={downloadUrl}
        stars={repoStats?.stars}
        contributors={contributorsCount}
      />
      <FeaturesSection />
      <DownloadsSection downloadUrl={downloadUrl} />
      <ScreenshotsSection />
      <ContributorsSection />
      <Footer />
    </main>
  );
}

function App() {
  return (
    <ThemeProvider
      attribute="class"
      defaultTheme="system"
      enableSystem
      disableTransitionOnChange
    >
      <HomePage />
    </ThemeProvider>
  );
}

export default App;
