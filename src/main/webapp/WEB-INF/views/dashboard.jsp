<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Dashboard - Vishal World</title>
            <!-- Tailwind CSS CDN -->
            <script src="https://cdn.tailwindcss.com"></script>
        </head>

        <body class="bg-slate-950 text-slate-100 min-h-screen flex flex-col">

            <!-- Top Navbar -->
            <header class="border-b border-slate-800 bg-slate-900/80 backdrop-blur sticky top-0 z-50">
                <div class="max-w-5xl mx-auto px-4 py-3 flex justify-between items-center">
                    <div class="flex items-center gap-2">
                        <div
                            class="w-8 h-8 rounded-lg bg-indigo-600 flex items-center justify-center font-bold text-white shadow-md shadow-indigo-500/20">
                            V
                        </div>
                        <span class="font-semibold text-lg text-white">Vishal World</span>
                    </div>

                    <div class="flex items-center gap-4">
                        <span class="text-sm text-slate-400 hidden sm:inline">Hello, <strong
                                class="text-indigo-400">${user.name}</strong></span>
                        <a href="/logout"
                            class="px-3.5 py-1.5 bg-slate-800 hover:bg-rose-600/80 border border-slate-700 hover:border-rose-500 text-xs font-medium text-slate-200 rounded-lg transition duration-200">
                            Sign Out
                        </a>
                    </div>
                </div>
            </header>

            <!-- Main Content Area -->
            <main class="flex-1 max-w-4xl w-full mx-auto p-4 sm:p-6 flex flex-col justify-between">

                <!-- Welcome Hero Section -->
                <div class="text-center my-6">
                    <h1
                        class="text-3xl sm:text-4xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400">
                        Welcome to vishal world
                    </h1>
                    <p class="text-slate-400 text-sm mt-2">Pucho kuch bhi, powered directly by Gemini AI</p>
                </div>

                <!-- Dynamic Temporary Chat Display -->
                <div class="space-y-6 my-4 flex-1">

                    <!-- User Prompt Box (If Asked) -->
                    <c:if test="${not empty userPrompt}">
                        <div class="flex justify-end">
                            <div
                                class="bg-indigo-600/90 text-white rounded-2xl rounded-tr-none px-5 py-3.5 max-w-[85%] shadow-lg">
                                <p class="text-xs text-indigo-200 font-semibold mb-1">Aapka Sawal:</p>
                                <p class="text-sm whitespace-pre-wrap leading-relaxed">${userPrompt}</p>
                            </div>
                        </div>
                    </c:if>

                    <!-- Gemini Response Box (If Answered) -->
                    <c:if test="${not empty aiResponse}">
                        <div class="flex justify-start">
                            <div
                                class="bg-slate-900 border border-slate-800 rounded-2xl rounded-tl-none p-5 max-w-[90%] shadow-xl">
                                <div class="flex items-center gap-2 mb-2 pb-2 border-b border-slate-800">
                                    <span class="w-2.5 h-2.5 rounded-full bg-emerald-400 animate-pulse"></span>
                                </div>
                                <div class="text-sm text-slate-200 whitespace-pre-wrap leading-relaxed font-normal">
                                    ${aiResponse}
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <!-- Error Display -->
                    <c:if test="${not empty errorMessage}">
                        <div class="bg-rose-500/10 border border-rose-500/30 text-rose-400 text-sm p-4 rounded-xl">
                            ${errorMessage}
                        </div>
                    </c:if>

                    <!-- Placeholder State when no chat yet -->
                    <c:if test="${empty userPrompt and empty aiResponse}">
                        <div
                            class="border border-dashed border-slate-800 rounded-2xl p-10 text-center text-slate-500 my-8">
                            <p class="text-sm">Abhi koi conversation active nahi hai.</p>
                            <p class="text-xs text-slate-600 mt-1">Niche diye search box me apna sawal type karein.</p>
                        </div>
                    </c:if>

                </div>

                <!-- Sticky Search / Ask Input Bar -->
                <div class="sticky bottom-4 pt-2">
                    <form action="/ask" method="POST" class="relative flex items-center shadow-2xl">
                        <input type="text" name="prompt" required
                            placeholder="Type your question here (e.g. Java Spring Boot roadmap, Docker kya hai?)..."
                            class="w-full pl-5 pr-28 py-4 bg-slate-900 border border-slate-700 rounded-2xl text-white placeholder-slate-500 focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 transition shadow-inner text-sm">

                        <button type="submit"
                            class="absolute right-2 px-5 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white font-medium text-sm rounded-xl shadow-md hover:shadow-indigo-500/30 transition duration-200 flex items-center gap-1.5">
                            <span>Ask AI</span>
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M14 5l7 7m0 0l-7 7m7-7H3" />
                            </svg>
                        </button>
                    </form>
                </div>

            </main>

        </body>

        </html>