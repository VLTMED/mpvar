import { Github, Heart } from "lucide-react";

export function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer
      className="bg-background border-t border-foreground/5 py-16 px-4 sm:px-6 lg:px-8 relative overflow-hidden"
      dir="rtl"
    >
      <div className="absolute inset-0 bg-grid-black/[0.01] dark:bg-grid-white/[0.01] pointer-events-none" style={{ backgroundSize: "32px 32px" }} />
      <div className="max-w-7xl mx-auto relative z-10">
        <div className="text-center mb-12">
          <h3
            className="text-lg text-foreground mb-4"
            style={{ fontWeight: 700, fontFamily: "'ThmanyahSans', system-ui" }}
          >
            شكر وتقدير
          </h3>
          <div className="flex flex-wrap items-center justify-center gap-6">
            {[
              { label: "mpv-android", href: "https://github.com/mpv-android/mpv-android" },
              { label: "mpvKt", href: "https://github.com/abdallahmehiz/mpvKt" },
              { label: "Next player", href: "https://github.com/anilbeesetti/nextplayer" },
              { label: "Gramophone", href: "https://github.com/FoedusProgramme/Gramophone" },
            ].map((item, i, arr) => (
              <span key={item.label} className="flex items-center gap-6">
                <a
                  href={item.href}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-sm text-muted-foreground hover:text-primary transition-colors"
                  style={{ fontWeight: 400, fontFamily: "'ThmanyahSans', system-ui" }}
                >
                  {item.label}
                </a>
                {i < arr.length - 1 && (
                  <span className="text-muted-foreground/30">•</span>
                )}
              </span>
            ))}
          </div>
        </div>

        <div className="border-t border-border/20 my-8" />

        <div className="flex flex-col sm:flex-row items-center justify-center gap-6 text-center">
          <p
            className="text-sm text-muted-foreground"
            style={{ fontWeight: 400, fontFamily: "'ThmanyahSans', system-ui" }}
          >
            صُنع بـ
            <Heart className="inline w-4 h-4 text-red-500 mx-1" fill="currentColor" />
            بواسطة{" "}
            <a
              href="https://riteshdpandit.vercel.app/"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-primary transition-colors decoration-primary/30 underline-offset-4 hover:underline"
              style={{ fontWeight: 500 }}
            >
              Ritesh Pandit
            </a>
          </p>
          <span className="hidden sm:inline text-muted-foreground/30">•</span>
          <a
            href="https://github.com/Riteshp2001"
            target="_blank"
            rel="noopener noreferrer"
            className="text-sm text-muted-foreground hover:text-primary transition-colors flex items-center gap-2 justify-center"
            style={{ fontWeight: 400, fontFamily: "'ThmanyahSans', system-ui" }}
          >
            <Github className="w-4 h-4" />
            GitHub
          </a>
          <span className="text-muted-foreground/30">•</span>
          <p
            className="text-sm text-muted-foreground"
            style={{ fontWeight: 400, fontFamily: "'ThmanyahSans', system-ui" }}
          >
            {`© ${currentYear} مشروع mpvExtended. رخصة Apache 2.0`}
          </p>
        </div>
      </div>
    </footer>
  );
}
