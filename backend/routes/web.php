<?php

use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    $arrogantPhrases = [
        "Yeah, we know our API is good. Deal with it.",
        "Other APIs wish they were this good. Seriously, we asked them.",
        "We coded this API while you were still debugging your 'Hello, World' program.",
        "This API is so good, it's almost unfair. (Almost.)",
        "We're not saying we're the best API... but have you tried the others?",
        "We set the curve. Everyone else is just trying to catch up."
    ];

    return view('welcome', ['phrase' => $arrogantPhrases[array_rand($arrogantPhrases)]]);
});

Route::get('/documentation', function () {
    return view('documentation');
});
