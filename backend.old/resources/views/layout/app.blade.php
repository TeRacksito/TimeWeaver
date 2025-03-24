<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>API Development Portal</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    @keyframes fadeIn {
      from {
        opacity: 0;
        transform: translateY(20px);
      }

      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    @keyframes pulse {

      0%,
      100% {
        transform: scale(1);
      }

      50% {
        transform: scale(1.05);
      }
    }

    @keyframes float {

      0%,
      100% {
        transform: translateY(0);
      }

      50% {
        transform: translateY(-10px);
      }
    }

    .animate-fadeIn {
      opacity: 0;
      animation: fadeIn 0.8s ease-out forwards;
    }

    .animate-pulse-slow {
      animation: pulse 3s ease-in-out infinite;
    }

    .animate-float {
      animation: float 6s ease-in-out infinite;
    }

    .delay-100 {
      animation-delay: 0.1s;
    }

    .delay-200 {
      animation-delay: 0.2s;
    }

    .delay-300 {
      animation-delay: 0.3s;
    }

    .delay-400 {
      animation-delay: 0.4s;
    }

    .delay-500 {
      animation-delay: 0.5s;
    }

    .delay-600 {
      animation-delay: 0.6s;
    }

    .bg-gradient {
      background: linear-gradient(135deg, #4f46e5 0%, #9333ea 50%, #ec4899 100%);
    }
  </style>
</head>

<body class="bg-slate-900 text-white min-h-screen">
  <div class="relative overflow-hidden bg-gradient-to-b from-slate-900 to-slate-800">

    <div class="absolute inset-0 overflow-hidden">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-purple-700/20 rounded-full blur-3xl"></div>
      <div class="absolute top-60 -left-40 w-80 h-80 bg-indigo-700/20 rounded-full blur-3xl animate-float"></div>
      <div class="absolute bottom-20 right-20 w-60 h-60 bg-pink-700/20 rounded-full blur-3xl animate-float delay-300"></div>
    </div>

    <div class="container mx-auto px-6 py-16 relative">

      @include('layout.header')

      @yield('content')

    </div>
  </div>

  @include('layout.footer')
</body>

</html>