<?php

namespace Database\Seeders;

use App\Models\WelcomePhrase;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class WelcomePhraseSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $arrogantPhrases = [
            "Intuitive? We practically read your mind. (And your mouse movements.)",
            "Effortless. Elegant. Unquestionably superior.",
            "The best thing since sliced bread.",
            "We're not bragging, but... yeah, we kind of are. This frontend is amazing.",
            "Future-proof design, today. Because we're always ahead of the curve.",
            "Prepare for a user experience that's... well, better. Much better.",
        ];

        foreach ($arrogantPhrases as $phrase) {
            WelcomePhrase::create(['phrase' => $phrase]);
        }
    }
}
