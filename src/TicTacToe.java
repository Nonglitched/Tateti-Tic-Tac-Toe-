import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame {
    private JButton[][] botones;
    private boolean turnoX;
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private int movimientos;
    private String jugadorX, jugadorO;
    private int puntajeX = 0, puntajeO = 0;
    private int partidasNecesarias = 1;
    private int partidasJugadas = 0;

    public TicTacToe() {
        configurarJuego();
        setTitle("Tres en Raya - " + jugadorX + " vs " + jugadorO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        turnoX = true;
        movimientos = 0;
        botones = new JButton[3][3];
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        JPanel panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(3, 3, 5, 5));
        panelTablero.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel panelEstado = new JPanel();
        panelEstado.setLayout(new GridLayout(2, 1));
        statusLabel = new JLabel("Turno de " + jugadorX, SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel = new JLabel(jugadorX + " (X): " + puntajeX + " - " + 
                              jugadorO + " (O): " + puntajeO, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panelEstado.add(statusLabel);
        panelEstado.add(scoreLabel);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j] = new JButton("");
                botones[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                botones[i][j].setFocusPainted(false);
                final int fila = i;
                final int columna = j;
                botones[i][j].addActionListener(e -> realizarJugada(fila, columna));
                panelTablero.add(botones[i][j]);
            }
        }
        JPanel panelControl = new JPanel();
        JButton botonReinicio = new JButton("Reiniciar Partida");
        JButton botonNuevoJuego = new JButton("Nuevo Juego");
        botonReinicio.addActionListener(e -> reiniciarPartida());
        botonNuevoJuego.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this, 
                "¿Deseas comenzar un nuevo juego?", 
                "Nuevo Juego", 
                JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                dispose();
                new TicTacToe();
            }
        });
        panelControl.add(botonReinicio);
        panelControl.add(botonNuevoJuego);
        panelPrincipal.add(panelEstado, BorderLayout.NORTH);
        panelPrincipal.add(panelTablero, BorderLayout.CENTER);
        panelPrincipal.add(panelControl, BorderLayout.SOUTH);
        add(panelPrincipal);
        setVisible(true);
    }
    private void configurarJuego() {
        jugadorX = JOptionPane.showInputDialog(this, "Ingrese nombre del Jugador X:", "Jugador 1");
        if (jugadorX == null || jugadorX.trim().isEmpty()) jugadorX = "Jugador X";
        jugadorO = JOptionPane.showInputDialog(this, "Ingrese nombre del Jugador O:", "Jugador 2");
        if (jugadorO == null || jugadorO.trim().isEmpty()) jugadorO = "Jugador O";
        String[] opciones = {"Partida Única", "Mejor de 3", "Mejor de 5"};
        int seleccion = JOptionPane.showOptionDialog(this,
            "Seleccione el modo de juego:",
            "Modo de Juego",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        switch (seleccion) {
            case 1:
                partidasNecesarias = 2;
                break;
            case 2:
                partidasNecesarias = 3;
                break;
            default:
                partidasNecesarias = 1;
        }
    }
    private void realizarJugada(int fila, int columna) {
        if (botones[fila][columna].getText().equals("")) {
            botones[fila][columna].setText(turnoX ? "X" : "O");
            movimientos++;
            if (verificarGanador(fila, columna)) {
                if (turnoX) {
                    puntajeX++;
                } else {
                    puntajeO++;
                }
                partidasJugadas++;
                actualizarPuntajes();
                if (puntajeX >= partidasNecesarias || puntajeO >= partidasNecesarias) {
                    mostrarGanadorFinal();
                } else {
                    String ganador = turnoX ? jugadorX : jugadorO;
                    JOptionPane.showMessageDialog(this, 
                        "¡" + ganador + " ha ganado esta ronda!");
                    reiniciarPartida();
                }
                return;
            }
            if (movimientos == 9) {
                partidasJugadas++;
                JOptionPane.showMessageDialog(this, "¡Empate!");
                reiniciarPartida();
                return;
            }
            turnoX = !turnoX;
            statusLabel.setText("Turno de " + (turnoX ? jugadorX : jugadorO));
        }
    }

    private boolean verificarGanador(int fila, int columna) {
        String simbolo = turnoX ? "X" : "O";
        if (botones[fila][0].getText().equals(simbolo) &&
            botones[fila][1].getText().equals(simbolo) &&
            botones[fila][2].getText().equals(simbolo)) {
            return true;
        }
        if (botones[0][columna].getText().equals(simbolo) &&
            botones[1][columna].getText().equals(simbolo) &&
            botones[2][columna].getText().equals(simbolo)) {
            return true;
        }
        if (botones[0][0].getText().equals(simbolo) &&
            botones[1][1].getText().equals(simbolo) &&
            botones[2][2].getText().equals(simbolo)) {
            return true;
        }

        if (botones[0][2].getText().equals(simbolo) &&
            botones[1][1].getText().equals(simbolo) &&
            botones[2][0].getText().equals(simbolo)) {
            return true;
        }

        return false;
    }
    private void actualizarPuntajes() {
        scoreLabel.setText(jugadorX + " (X): " + puntajeX + " - " + 
                         jugadorO + " (O): " + puntajeO);
    }
    private void mostrarGanadorFinal() {
        String ganadorFinal = puntajeX > puntajeO ? jugadorX : jugadorO;
        JOptionPane.showMessageDialog(this, 
            "¡" + ganadorFinal + " ha ganado el juego!\n" +
            "Puntaje final:\n" +
            jugadorX + ": " + puntajeX + "\n" +
            jugadorO + ": " + puntajeO);
        
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Desean jugar otra vez?",
            "Nuevo Juego",
            JOptionPane.YES_NO_OPTION);
            
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            new TicTacToe();
        } else {
            dispose();
        }
    }
    private void reiniciarPartida() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setText("");
                botones[i][j].setEnabled(true);
            }
        }
        turnoX = true;
        movimientos = 0;
        statusLabel.setText("Turno de " + jugadorX);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }
}