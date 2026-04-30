/**
 * @file data.ts
 * @description بيانات ثابتة للتطبيق - المميزات وخيارات التحميل والإحصاءات
 * @module lib/data
 */

export const features = [
  {
    id: 1,
    title: "تصميم Material3",
    description: "واجهة حديثة وتعبيرية تتبع أحدث إرشادات تصميم أندرويد.",
    icon: "palette",
  },
  {
    id: 2,
    title: "إعدادات متقدمة",
    description: "تحكم كامل في إعدادات mpv وقدرات السكريبت.",
    icon: "sliders",
  },
  {
    id: 3,
    title: "صورة داخل صورة",
    description: "شاهد مقاطع الفيديو أثناء تعدد المهام بدعم PiP سلس.",
    icon: "frame",
  },
  {
    id: 4,
    title: "تشغيل في الخلفية",
    description: "استمع للمحتوى الصوتي أثناء استخدام تطبيقات أخرى.",
    icon: "volume-2",
  },
  {
    id: 5,
    title: "إدارة الملفات",
    description: "منتقي وسائط بعرض شجري ومجلدات للتصفح السريع.",
    icon: "folder",
  },
  {
    id: 6,
    title: "دعم الترجمة",
    description: "دعم ترجمة خارجية بتوافق صيغ متعددة.",
    icon: "captions",
  },
  {
    id: 7,
    title: "البث عبر الشبكة",
    description: "بث من SMB/FTP/WebDAV بجودة عالية.",
    icon: "wifi",
  },
  {
    id: 8,
    title: "قوائم تشغيل مخصصة",
    description: "أنشئ وأدر مجموعات قوائم التشغيل بسهولة.",
    icon: "list",
  },
  {
    id: 9,
    title: "إيماءات التكبير",
    description: "تحكم بديهي بالتكبير والتحريك بالإيماءات.",
    icon: "maximize-2",
  },
  {
    id: 10,
    title: "صوت خارجي",
    description: "دعم مسارات الصوت الخارجية واختيار التدفق.",
    icon: "headphones",
  },
  {
    id: 11,
    title: "وظيفة البحث",
    description: "بحث سريع للعثور على ملفات وسائطك.",
    icon: "search",
  },
  {
    id: 12,
    title: "مجاني ومفتوح المصدر",
    description: "مجاني تمامًا ومفتوح المصدر بدون إعلانات أو صلاحيات زائدة.",
    icon: "shield-check",
  },
];

import { siteConfig } from "@/lib/site";

export const downloadOptions = [
  {
    id: 1,
    title: "الإصدار الثابت",
    description: "حمّل أحدث إصدار مستقر مباشرةً من GitHub.",
    link: siteConfig.links.latestRelease,
    icon: "github",
    cta: "تحميل APK",
  },
  {
    id: 2,
    title: "إصدارات المعاينة",
    description: "جرّب أحدث الميزات والتحسينات في مرحلة التطوير.",
    link: siteConfig.links.releases,
    icon: "zap",
    cta: "عرض الإصدارات",
  },
  {
    id: 3,
    title: "IzzyOnAndroid",
    description: "ثبّت وحدّث تلقائيًا عبر عميل IzzyOnAndroid.",
    link: siteConfig.links.izzyOnAndroid,
    icon: "download",
    cta: "عرض المستودع",
  },
];

export const stats = [
  { label: "نجمة", value: "21.7K", icon: "⭐" },
  { label: "تحميل", value: "5.1K", icon: "📥" },
  { label: "مساهم", value: "25+", icon: "👥" },
  { label: "مفتوح المصدر", value: "Apache 2.0", icon: "📄" },
];
