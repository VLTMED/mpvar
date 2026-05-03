"use client";

import { motion, type Variants } from "framer-motion";
import { RefreshCcw } from "lucide-react";
import { useEffect } from "react";
import { Button } from "@/components/ui/button";

export default function ErrorPage({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  useEffect(() => { console.error(error); }, [error]);

  const containerVariants: Variants = {
    hidden: { opacity: 0 },
    visible: { opacity: 1, transition: { staggerChildren: 0.1 } },
  };
  const itemVariants: Variants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.5, ease: "easeOut" } },
  };

  return (
    <motion.div
      variants={containerVariants}
      initial="hidden"
      animate="visible"
      className="min-h-screen flex flex-col items-center justify-center bg-background text-foreground p-4 text-center"
      dir="rtl"
    >
      <motion.h2
        variants={itemVariants}
        className="text-4xl mb-4 text-destructive"
        style={{ fontFamily: "'ThmanyahSans', system-ui", fontWeight: 900 }}
      >
        حدث خطأ ما!
      </motion.h2>
      <motion.p
        variants={itemVariants}
        className="text-muted-foreground text-lg mb-8 max-w-md"
        style={{ fontFamily: "'ThmanyahSans', system-ui", fontWeight: 400, lineHeight: 1.75 }}
      >
        واجهنا خطأً غير متوقع. يرجى المحاولة مرة أخرى لاحقًا.
      </motion.p>
      <motion.div variants={itemVariants}>
        <Button
          onClick={reset}
          className="bg-primary hover:bg-primary/90 text-primary-foreground rounded-full px-8 py-6 text-lg shadow-lg hover:shadow-xl transition-all"
          style={{ fontFamily: "'ThmanyahSans', system-ui", fontWeight: 700 }}
        >
          <RefreshCcw className="ms-2 w-5 h-5" />
          حاول مجدداً
        </Button>
      </motion.div>
      {error.digest && (
        <motion.p
          variants={itemVariants}
          className="mt-8 text-xs text-muted-foreground font-mono"
        >
          معرّف الخطأ: {error.digest}
        </motion.p>
      )}
    </motion.div>
  );
}
