import { motion } from "framer-motion";
import { ArrowLeft, FlaskConical, Github } from "lucide-react";
import { Button } from "@/components/ui/button";
import { downloadOptions } from "@/lib/data";

const iconMap: Record<string, React.ReactNode> = {
  github: <Github className="w-8 h-8 text-foreground" />,
  zap: <FlaskConical className="w-8 h-8 text-red-500" />,
  download: (
    <div className="relative w-32 h-12">
      <img src="/izzy.svg" alt="احصل عليه من IzzyOnDroid" className="object-contain w-full h-full" />
    </div>
  ),
};

export function DownloadsSection({ downloadUrl }: { downloadUrl?: string }) {
  return (
    <section
      className="py-32 px-4 sm:px-6 lg:px-8 bg-background relative overflow-hidden"
      dir="rtl"
    >
      <div className="absolute bottom-0 left-1/2 -translate-x-1/2 w-[800px] h-[300px] bg-secondary/10 blur-[120px] rounded-full opacity-30 pointer-events-none" />

      <div className="max-w-7xl mx-auto relative z-10">
        <h2
          className="text-4xl md:text-6xl mb-6 text-center tracking-tight"
          style={{
            fontFamily: "'ThmanyahSans', system-ui, sans-serif",
            fontWeight: 900,
            lineHeight: 1.2,
          }}
        >
          <span className="bg-clip-text text-transparent bg-gradient-to-r from-foreground via-foreground/80 to-foreground/40">
            احصل عليه
          </span>
        </h2>
        <p
          className="text-lg text-muted-foreground text-center mb-20 max-w-2xl mx-auto"
          style={{
            fontFamily: "'ThmanyahSans', system-ui, sans-serif",
            fontWeight: 400,
            lineHeight: 1.75,
          }}
        >
          حمّل mpvExtended من المصدر الذي تفضله وابدأ تجربة التشغيل المتميز اليوم.
        </p>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {downloadOptions.map((option) => (
            <motion.div
              key={option.id}
              whileHover={{ scale: 1.05, transition: { duration: 0.2 } }}
              className="download-card relative overflow-hidden bg-foreground/[0.03] dark:bg-white/[0.02] backdrop-blur-md border border-foreground/5 dark:border-white/5 rounded-3xl p-8 flex flex-col items-center text-center hover:bg-foreground/5 dark:hover:bg-white/[0.04] hover:border-secondary/30 transition-all duration-300 group"
            >
              <div className="absolute inset-0 bg-gradient-to-br from-secondary/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
              <div className="relative z-10 flex flex-col h-full items-center text-center">
                <div className="mb-6 text-secondary flex justify-center transition-transform duration-500 group-hover:scale-110">
                  {iconMap[option.icon]}
                </div>
                <h3
                  className="text-2xl text-foreground mb-3"
                  style={{
                    fontFamily: "'ThmanyahSans', system-ui, sans-serif",
                    fontWeight: 700,
                    lineHeight: 1.3,
                  }}
                >
                  {option.title}
                </h3>
                <p
                  className="text-muted-foreground mb-8 grow text-sm"
                  style={{
                    fontFamily: "'ThmanyahSans', system-ui, sans-serif",
                    fontWeight: 400,
                    lineHeight: 1.75,
                  }}
                >
                  {option.description}
                </p>
                <Button
                  className="w-full h-12 bg-secondary hover:bg-secondary/80 text-secondary-foreground rounded-2xl transition-all duration-300 flex items-center justify-center gap-2 shadow-lg cursor-pointer"
                  style={{
                    fontFamily: "'ThmanyahSans', system-ui, sans-serif",
                    fontWeight: 700,
                  }}
                  onClick={() => {
                    if (option.title === "إصدارات المعاينة" && downloadUrl) {
                      window.open(downloadUrl, "_blank");
                    } else {
                      window.open(option.link, "_blank");
                    }
                  }}
                >
                  {option.cta}
                  <ArrowLeft className="w-5 h-5 transition-transform group-hover:-translate-x-1" />
                </Button>
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
