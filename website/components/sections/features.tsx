"use client";

import { motion } from "framer-motion";
import {
  Captions, Folder, Frame, Headphones, List,
  Maximize2, Palette, Search, ShieldCheck, Sliders, Volume2, Wifi,
} from "lucide-react";
import type React from "react";
import { features } from "@/lib/data";

const iconMap: Record<string, React.ReactNode> = {
  palette: <Palette className="w-8 h-8" />,
  sliders: <Sliders className="w-8 h-8" />,
  frame: <Frame className="w-8 h-8" />,
  "volume-2": <Volume2 className="w-8 h-8" />,
  folder: <Folder className="w-8 h-8" />,
  captions: <Captions className="w-8 h-8" />,
  wifi: <Wifi className="w-8 h-8" />,
  "shield-check": <ShieldCheck className="w-8 h-8" />,
  list: <List className="w-8 h-8" />,
  "maximize-2": <Maximize2 className="w-8 h-8" />,
  headphones: <Headphones className="w-8 h-8" />,
  search: <Search className="w-8 h-8" />,
};

export function FeaturesSection() {
  return (
    <section
      id="features"
      className="py-20 px-4 sm:px-6 lg:px-8 bg-background relative overflow-hidden"
      dir="rtl"
    >
      <div className="absolute inset-0 bg-grid-black/[0.02] dark:bg-grid-white/[0.02] bg-size-[32px_32px] pointer-events-none" />

      <div className="max-w-7xl mx-auto relative z-10">
        <div className="text-center mb-20">
          <h2
            className="text-4xl md:text-6xl mb-6 tracking-tight"
            style={{
              fontFamily: "'ThmanyahSans', system-ui, sans-serif",
              fontWeight: 900,
              lineHeight: 1.2,
            }}
          >
            <span className="bg-clip-text text-transparent bg-linear-to-r from-foreground via-foreground/80 to-foreground/40">
              مميزات قوية
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
            كل ما تحتاجه لتجربة مشاهدة فيديو استثنائية،
            مبنيّة على الأساس المتين لـ libmpv.
          </p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {features.map((feature) => (
            <motion.div
              key={feature.id}
              whileHover={{ scale: 1.05, transition: { duration: 0.2 } }}
              className="feature-card relative overflow-hidden bg-foreground/3 dark:bg-white/2 backdrop-blur-md border border-foreground/5 dark:border-white/5 rounded-3xl p-8 hover:bg-foreground/5 dark:hover:bg-white/4 hover:border-primary/30 transition-all duration-300 cursor-pointer group flex flex-col items-center text-center"
            >
              <div className="absolute inset-0 bg-linear-to-br from-primary/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
              <div className="relative z-10 flex flex-col items-center text-center">
                <div className="mb-6 text-primary flex justify-center transition-transform duration-500 group-hover:scale-110">
                  {iconMap[feature.icon]}
                </div>
                <h3
                  className="text-xl text-foreground mb-3 group-hover:text-primary transition-colors"
                  style={{
                    fontFamily: "'ThmanyahSans', system-ui, sans-serif",
                    fontWeight: 700,
                    lineHeight: 1.35,
                  }}
                >
                  {feature.title}
                </h3>
                <p
                  className="text-muted-foreground text-sm"
                  style={{
                    fontFamily: "'ThmanyahSans', system-ui, sans-serif",
                    fontWeight: 400,
                    lineHeight: 1.75,
                  }}
                >
                  {feature.description}
                </p>
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
