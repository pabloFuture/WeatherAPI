package com.example.weatherapi;

public class WindTurbinePowerCalculator {

    // Stałe związane z charakterystyką wiatraka
    private static final double MAX_POWER_OUTPUT = 1000; // Maksymalna moc wiatraka w kW
    private static final double STARTUP_WIND_SPEED = 3.0; // Prędkość wiatru potrzebna do rozpoczęcia pracy wiatraka w m/s
    private static final double CUT_OUT_WIND_SPEED = 25.0; // Prędkość wiatru, powyżej której wiatrak jest wyłączany w m/s

    // Metoda obliczająca produkcję prądu przez wiatrak na podstawie prędkości wiatru
    public static double calculatePowerOutput(double windSpeed) {
        if (windSpeed < STARTUP_WIND_SPEED || windSpeed >= CUT_OUT_WIND_SPEED) {
            return 0; // Wiatrak nie pracuje poniżej prędkości startowej lub powyżej prędkości maksymalnej
        } else {
            // Prosta interpolacja liniowa dla mocy w zależności od prędkości wiatru
            return (windSpeed - STARTUP_WIND_SPEED) / (CUT_OUT_WIND_SPEED - STARTUP_WIND_SPEED) * MAX_POWER_OUTPUT;
        }
    }
}

