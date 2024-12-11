package iesmm.pmdm.tres_en_raya;

import java.util.Random;

public class JuegoTresEnRaya {
    public static final int DIM_TABLERO = 9;
    public static final char JUGADOR = 'X';
    public static final char MAQUINA = 'O';
    public static final char BLANCO = ' ';

    private char[] tablero;
    private Random aleatorio;

    public JuegoTresEnRaya() {
        tablero = new char[DIM_TABLERO];
        aleatorio = new Random();
        limpiarTablero();
    }

    public void limpiarTablero() {
        for (int i = 0; i < DIM_TABLERO; i++) {
            tablero[i] = BLANCO;
        }
    }

    public boolean moverFicha(char ficha, int casilla) {
        if (casilla >= 0 && casilla < DIM_TABLERO && tablero[casilla] == BLANCO) {
            tablero[casilla] = ficha;
            return true;
        }
        return false;
    }

    public int comprobarGanador() {
        // Comprobar filas horizontales
        for (int i = 0; i <= 6; i += 3) {
            if (tablero[i] == JUGADOR && tablero[i + 1] == JUGADOR && tablero[i + 2] == JUGADOR)
                return 1;
            if (tablero[i] == MAQUINA && tablero[i + 1] == MAQUINA && tablero[i + 2] == MAQUINA)
                return 2;
        }

        // Comprobar columnas verticales
        for (int i = 0; i < 3; i++) {
            if (tablero[i] == JUGADOR && tablero[i + 3] == JUGADOR && tablero[i + 6] == JUGADOR)
                return 1;
            if (tablero[i] == MAQUINA && tablero[i + 3] == MAQUINA && tablero[i + 6] == MAQUINA)
                return 2;
        }

        // Comprobar diagonales
        if ((tablero[0] == JUGADOR && tablero[4] == JUGADOR && tablero[8] == JUGADOR) ||
                (tablero[2] == JUGADOR && tablero[4] == JUGADOR && tablero[6] == JUGADOR))
            return 1;

        if ((tablero[0] == MAQUINA && tablero[4] == MAQUINA && tablero[8] == MAQUINA) ||
                (tablero[2] == MAQUINA && tablero[4] == MAQUINA && tablero[6] == MAQUINA))
            return 2;

        // Comprobar si hay casillas vacías
        for (int i = 0; i < DIM_TABLERO; i++) {
            if (tablero[i] == BLANCO) return 0;
        }

        // Empate si nadie ha ganado
        return -1;
    }

    public int getMovimientoMaquina() {
        return getMovimientoMaquinaAleatorio();
    }

    private int getMovimientoMaquinaAleatorio() {
        int casilla;
        do {
            casilla = aleatorio.nextInt(DIM_TABLERO);
        } while (tablero[casilla] != BLANCO);
        return casilla;
    }
}
