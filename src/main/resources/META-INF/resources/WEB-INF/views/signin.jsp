<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Sign In - Vishal World</title>
            <script src="https://cdn.tailwindcss.com"></script>
        </head>

        <body class="bg-slate-900 min-h-screen flex items-center justify-center p-4">

            <div class="bg-slate-800 border border-slate-700 shadow-2xl rounded-2xl p-8 w-full max-w-md">

                <div class="text-center mb-6">
                    <h1 class="text-3xl font-bold text-white tracking-wide">Sign In</h1>
                    <p class="text-slate-400 text-sm mt-1">Access Vishal World AI Dashboard</p>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div
                        class="mb-4 bg-rose-500/10 border border-rose-500/30 text-rose-400 text-sm p-3 rounded-lg flex items-center gap-2">
                        <span>${errorMessage}</span>
                    </div>
                </c:if>

                <c:if test="${not empty successMessage}">
                    <div
                        class="mb-4 bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-sm p-3 rounded-lg flex items-center gap-2">
                        <span>${successMessage}</span>
                    </div>
                </c:if>

                <form action="/signin" method="POST" class="space-y-4">
                    <div>
                        <label class="block text-sm font-medium text-slate-300 mb-1">Email Address</label>
                        <input type="email" name="email" required placeholder="name@example.com"
                            class="w-full px-4 py-2.5 bg-slate-950 border border-slate-700 rounded-lg text-white placeholder-slate-500 focus:outline-none focus:border-indigo-500 transition">
                    </div>

                    <div>
                        <label class="block text-sm font-medium text-slate-300 mb-1">Password</label>
                        <input type="password" name="password" required placeholder="Enter password"
                            class="w-full px-4 py-2.5 bg-slate-950 border border-slate-700 rounded-lg text-white placeholder-slate-500 focus:outline-none focus:border-indigo-500 transition">
                    </div>

                    <button type="submit"
                        class="w-full py-3 px-4 bg-indigo-600 hover:bg-indigo-500 text-white font-medium rounded-lg shadow-lg hover:shadow-indigo-500/25 transition duration-200">
                        Sign In
                    </button>
                </form>

                <div class="mt-6 text-center border-t border-slate-700/60 pt-4">
                    <p class="text-sm text-slate-400">
                        Naya account banana hai?
                        <a href="/signup" class="text-indigo-400 font-medium hover:text-indigo-300 hover:underline">Sign
                            Up karein</a>
                    </p>
                </div>

            </div>

        </body>

        </html>