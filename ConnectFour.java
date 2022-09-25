package four;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ConnectFour extends JFrame {
    static JButton[][] buttons = new JButton[6][7];
    static boolean gameOn = true;

    public ConnectFour() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 500);
        setVisible(true);
        setLayout(new BorderLayout());
        setTitle("Connect Four");
        Checker checker = new Checker();

        JPanel main = new JPanel();
        main.setLayout(new GridLayout(6, 7, 0, 0));
        add(main, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        add(bottom, BorderLayout.SOUTH);

        for (int i = buttons.length; i > 0; i--) {
            for (char c = 'A'; c <= 'G'; c++) {
                String name = String.format("Button%c%d", c, i);
                JButton btn = new JButton(" ");
                btn.setName(name);
                btn.addActionListener(checker);
                btn.setFocusPainted(false);
                btn.setBackground(Color.lightGray);
                btn.setOpaque(true);
                btn.setFont(new Font("Courier", Font.PLAIN, 24));
                buttons[i - 1][c - 'A'] = btn;
                main.add(buttons[i - 1][c - 'A']);
            }
        }

        JButton ButtonReset = new JButton("Reset");
        ButtonReset.setName("ButtonReset");
        ButtonReset.setFocusPainted(false);
        ButtonReset.addActionListener(e -> {
            ConfirmWindow confirm = new ConfirmWindow();
            confirm.setVisible(true);
            confirm.setLocationRelativeTo(this);
        });
        bottom.add(ButtonReset);
    }

    private class ConfirmWindow extends JFrame implements ActionListener {

        public ConfirmWindow() {
            setSize(250, 100);
            setLayout(new BorderLayout());

            JLabel confirmLabel = new JLabel("Are you sure you want to reset?", SwingConstants.CENTER);
            add(confirmLabel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            JButton exitButton = new JButton("Yes");
            exitButton.addActionListener(this);
            buttonPanel.add(exitButton);

            JButton cancelButton = new JButton("No");
            cancelButton.addActionListener(this);
            buttonPanel.add(cancelButton);

            add(buttonPanel, BorderLayout.SOUTH);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();

            if (action.equals("Yes")) {
                for (JButton[] button : buttons) {
                    for (JButton jButton : button) {
                        jButton.setText(" ");
                        jButton.setBackground(Color.lightGray);
                    }
                }
                ConnectFour.gameOn = true;
                Checker.player = "O";
                dispose();
            } else if (action.equals("No")) {
                dispose();
            }
        }
    }
}

class Checker implements ActionListener {
    static String player = "O";
    JButton[][] buttons = ConnectFour.buttons;

    /**
     * Handles button clicks on the 6 * 7 game board.
     *
     * @param e - ActionEvent object
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        /* col and row are the position for the clicked button -- they are acquired from button name ---
         button name was of the form "ButtonA1" (position [0][5]) */
        int col = ((((JButton) e.getSource()).getName().replaceAll("Button", "").charAt(0)) - '@') - 1;
        int row = Integer.parseInt(((JButton) e.getSource()).getName().replaceAll("\\D", "")) - 1;

        if (buttons[row][col].getActionCommand() == " " && ConnectFour.gameOn) {
            int freeRow = 0;
            boolean flag = true;   //checks if we have free cell to fill in the clicked column (col)
            for (int i = 0; i < buttons.length; i++) {
                if (!Objects.equals(buttons[i][col].getActionCommand(), " ")) { //if it is occupied go to freeRow above
                    if (i != 5) {
                        freeRow++;
                    } else {
                        flag = false;
                    }
                }
            }
            if (flag) {
                player = player.equals("X") ? "O" : "X";
                buttons[freeRow][col].setText(player);
            }
            checkWinner(player);
        }
    }

    void checkWinner(String player) {
        /* Since the game board has the same number of columns for every row, we can use any row to get column length.
         I used buttons[0] here. */

        // horizontal check
        for (int j = 0; j < buttons[0].length - 3; j++) {
            for (JButton[] button : buttons) {
                if (button[j].getActionCommand() == player && button[j + 1].getActionCommand() == player && button[j + 2].getActionCommand() == player && button[j + 3].getActionCommand() == player) {
                    button[j].setBackground(Color.red);
                    button[j + 1].setBackground(Color.red);
                    button[j + 2].setBackground(Color.red);
                    button[j + 3].setBackground(Color.red);
                    ConnectFour.gameOn = false;
                }
            }
        }

        //vertical check
        for (int i = 0; i < buttons.length - 3; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j].getActionCommand() == player && buttons[i + 1][j].getActionCommand() == player && buttons[i + 2][j].getActionCommand() == player && buttons[i + 3][j].getActionCommand() == player) {
                    buttons[i][j].setBackground(Color.red);
                    buttons[i + 1][j].setBackground(Color.red);
                    buttons[i + 2][j].setBackground(Color.red);
                    buttons[i + 3][j].setBackground(Color.red);
                    ConnectFour.gameOn = false;
                }
            }
        }

        //ascending diagonal check
        for (int i = 0; i < buttons.length - 3; i++) {
            for (int j = 0; j < buttons[i].length - 3; j++) {
                if (buttons[i][j].getActionCommand() == player && buttons[i + 1][j + 1].getActionCommand() == player && buttons[i + 2][j + 2].getActionCommand() == player && buttons[i + 3][j + 3].getActionCommand() == player) {
                    buttons[i][j].setBackground(Color.red);
                    buttons[i + 1][j + 1].setBackground(Color.red);
                    buttons[i + 2][j + 2].setBackground(Color.red);
                    buttons[i + 3][j + 3].setBackground(Color.red);
                    ConnectFour.gameOn = false;
                }
            }
        }

        //descending diagonal check
        for (int i = 3; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length - 3; j++) {
                if (buttons[i][j].getActionCommand() == player && buttons[i - 1][j + 1].getActionCommand() == player && buttons[i - 2][j + 2].getActionCommand() == player && buttons[i - 3][j + 3].getActionCommand() == player) {
                    buttons[i][j].setBackground(Color.red);
                    buttons[i - 1][j + 1].setBackground(Color.red);
                    buttons[i - 2][j + 2].setBackground(Color.red);
                    buttons[i - 3][j + 3].setBackground(Color.red);
                    ConnectFour.gameOn = false;
                }
            }
        }
    }
}