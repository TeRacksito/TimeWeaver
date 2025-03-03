import { useEffect, useState } from "react";
import { getWelcomePhrase } from "./services/main";

export function HomePage() {
  const [phrase, setPhrase] = useState("");

  useEffect(() => {
    getWelcomePhrase().then((data) => setPhrase(data));
  }, []);

  return (
    <>
      <div className="max-w-3xl mx-auto text-center mb-16">
        <h1
          className="bg-gradient text-5xl md:text-6xl font-bold tracking-tight bg-clip-text text-transparent animate-fadeIn delay-300 mb-6"
          style={{ backgroundClip: "text" }}
        >
          Proyecto-Final - Frontend
        </h1>
        <p className="text-xl text-slate-300 animate-fadeIn delay-400 mb-8">
          <div className="flex items-center justify-center mb-4 animate-fadeIn delay-400">
            <svg
              className="w-5 h-5 text-indigo-400 mr-2"
              viewBox="0 0 24 24"
              fill="currentColor"
            >
              <path d="M14.017 21v-7.391c0-5.704 3.731-9.57 8.983-10.609l.995 2.151c-2.432.917-3.995 3.638-3.995 5.849h4v10h-9.983zm-14.017 0v-7.391c0-5.704 3.748-9.57 9-10.609l.996 2.151c-2.433.917-3.996 3.638-3.996 5.849h3.983v10h-9.983z" />
            </svg>
            <span className="italic font-medium">{phrase}</span>
            <svg
              className="w-5 h-5 text-indigo-400 ml-2 transform rotate-180"
              viewBox="0 0 24 24"
              fill="currentColor"
            >
              <path d="M14.017 21v-7.391c0-5.704 3.731-9.57 8.983-10.609l.995 2.151c-2.432.917-3.995 3.638-3.995 5.849h4v10h-9.983zm-14.017 0v-7.391c0-5.704 3.748-9.57 9-10.609l.996 2.151c-2.433.917-3.996 3.638-3.996 5.849h3.983v10h-9.983z" />
            </svg>
          </div>
        </p>
        <div className="animate-fadeIn delay-500">
          <a
            href="https://github.com/IES-Las-Galletas-DAW-2025-Grupo-D/Proyecto-Final"
            className="px-6 py-3 border border-indigo-500/30 hover:bg-indigo-500/10 rounded-lg font-medium transition-all duration-200 inline-flex items-center"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5 mr-2"
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z" />
            </svg>
            GitHub Repo
          </a>
        </div>
      </div>
    </>
  );
}
