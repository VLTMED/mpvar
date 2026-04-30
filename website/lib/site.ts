/**
 * @file site.ts
 * @description الإعدادات المركزية للموقع - البيانات الوصفية والروابط والقيم الثابتة
 * @module lib/site
 */

export const siteConfig = {
  name: "mpvExtended",
  version: "v1.2.7",
  description:
    "مشغّل فيديو متقدم لأندرويد مبني على libmpv، بمميزات قوية وتشغيل سلس وحرية المصدر المفتوح.",
  url: "https://mpvex.vercel.app",
  ogImage: "https://mpvex.vercel.app/og.jpg",
  icons: {
    icon: "/icon.svg",
    apple: "/apple-icon.png",
  },
  links: {
    github: "https://github.com/marlboro-advance/mpvEx",
    releases: "https://github.com/marlboro-advance/mpvEx/releases",
    latestRelease: "https://github.com/marlboro-advance/mpvEx/releases/latest",
    izzyOnAndroid: "https://apt.izzysoft.de/packages/app.marlboroadvance.mpvex",
    contributors: "https://github.com/marlboro-advance/mpvEx/graphs/contributors",
  },
  author: {
    name: "marlboro-advance",
    url: "https://github.com/marlboro-advance",
  },
} as const;

export type SiteConfig = typeof siteConfig;
