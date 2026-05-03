"use client";

import { motion, type Variants } from "framer-motion";
import { Home } from "lucide-react";
import Link from "next/link";
import { Button } from "@/components/ui/button";

export default function NotFound() {
  const containerVariants: Variants = {
    hidden: { opacity: 0 },
    visible: { opacity: 1, transition: { staggerChildren: 0.2 } },
  };
  const itemVariants: Variants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.5, ease: "easeOut" } },
  };
  const titleVariants: Variants = {
    hidden: { opacity: 0, y: 50, scale: 0.8 },
    visible: { opacity: 1, y: 0, scale: 1, transition: { duration: 0.8, ease: "backOut" } },
  };

  return (
    <motion.div
      variants={containerVariants}
      initial="hidden"
      animate="visible"
      className="min-h-screen flex flex-col items-center justify-center bg-background text-foreground p-4 text-center"
      dir="rtl"
    >
      <motion.div
        variants={titleVariants}
        className="text-9xl bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary mb-4"
        style={{ fontFamily: "'ThmanyahSerifDisplay', system-ui", fontWeight: 900 }}
      >
        404
      </motion.div>
      <motion.h1
        variants={itemVariants}
        className="text-4xl mb-4"
        style={{ fontFamily: "'ThmanyahSans', system-ui", fontWeight: 900 }}
      >
        الصفحة غير موجودة
      </motion.h1>
      <motion.p
        variants={itemVariants}
        className="text-muted-foreground text-lg mb-8 max-w-md"
        style={{ fontFamily: "'ThmanyahSans', system-ui", fontWeight: 400, lineHeight: 1.75 }}
      >
        عذراً! الصفحة التي تبحث عنها غير موجودة أو تم نقلها.
      </motion.p>
      <motion.div variants={itemVariants}>
        <Button
          asChild
          className="bg-primary hover:bg-primary/90 text-primary-foreground rounded-full px-8 py-6 text-lg shadow-lg hover:shadow-xl transition-all"
          style={{ fontFamily: "'ThmanyahSans', system-ui", fontWeight: 700 }}
        >
          <Link href="/">
            <Home className="ms-2 w-5 h-5 icon-directional" />
            العودة للرئيسية
          </Link>
        </Button>
      </motion.div>
    </motion.div>
  );
}
