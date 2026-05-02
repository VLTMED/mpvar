import { motion } from "framer-motion";

const screenshots = [
  { id: 1, title: "مستعرض الملفات", image: "/IMG-20251120-WA0004.jpg" },
  { id: 2, title: "مشغّل الفيديو", image: "/IMG-20251120-WA0002.jpg" },
  { id: 3, title: "الإعدادات", image: "/IMG-20251120-WA0003.jpg" },
  { id: 4, title: "التشغيل", image: "/IMG-20251120-WA0005.jpg" },
  { id: 5, title: "صورة داخل صورة", image: "/IMG-20251120-WA0006.jpg" },
  { id: 6, title: "أدوات التحكم", image: "/IMG-20251120-WA0008.jpg" },
];

export function ScreenshotsSection() {
  return (
    <section
      id="screenshots"
      className="py-16 px-4 sm:px-6 lg:px-8 bg-background relative overflow-hidden"
      dir="rtl"
    >
      <div className="absolute top-[20%] left-1/2 -translate-x-1/2 w-[600px] h-[300px] bg-primary/20 blur-[100px] rounded-full opacity-20 pointer-events-none" />

      <div className="max-w-7xl mx-auto relative z-10">
        <div className="text-center mb-10">
          <h2
            className="text-4xl md:text-5xl mb-6 tracking-tight"
            style={{
              fontFamily: "'ThmanyahSans', system-ui, sans-serif",
              fontWeight: 900,
              lineHeight: 1.2,
            }}
          >
            <span className="bg-clip-text text-transparent bg-gradient-to-r from-foreground via-foreground/80 to-foreground/50">
              لقطات الشاشة
            </span>
          </h2>
          <p
            className="text-lg text-muted-foreground max-w-2xl mx-auto"
            style={{
              fontFamily: "'ThmanyahSans', system-ui, sans-serif",
              fontWeight: 400,
              lineHeight: 1.75,
            }}
          >
            تصميم Material3 حديث يتكيّف مع جهازك.
          </p>
        </div>

        <div className="flex justify-start overflow-x-auto pb-12 pt-12 ps-4 md:ps-32 pe-4 md:pe-32 scrollbar-hide snap-x snap-proximity">
          <div className="flex flex-nowrap items-center gap-6 md:gap-12 w-max">
            {screenshots.map((screenshot) => (
              <motion.div
                key={screenshot.id}
                whileHover={{ scale: 1.08, zIndex: 20, transition: { duration: 0.2 } }}
                className="relative shrink-0 w-[160px] md:w-[200px] overflow-hidden border-4 border-foreground/5 dark:border-white/5 bg-black shadow-2xl cursor-pointer transition-all duration-200 group snap-center rounded-[2.5rem]"
                style={{ aspectRatio: "9/19" }}
              >
                <div className="absolute inset-0 pointer-events-none z-20 ring-1 ring-foreground/10 dark:ring-white/10 shadow-[inset_0_0_20px_rgba(0,0,0,0.05)] dark:shadow-[inset_0_0_20px_rgba(255,255,255,0.05)] rounded-[2.5rem]" />
                <div className="absolute inset-0 z-0">
                  <img
                    src={screenshot.image}
                    alt={screenshot.title}
                    className="object-cover w-full h-full transition-transform duration-300 group-hover:scale-105"
                  />
                  <div className="absolute inset-x-0 bottom-0 h-1/4 bg-gradient-to-t from-black/40 to-transparent opacity-60 transition-opacity" />
                </div>
              </motion.div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}
