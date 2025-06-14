package com.group16.view.ui;

import com.group16.controller.config.GameConfig;
import com.group16.model.Subject;
import com.group16.model.TitleLogic;
import com.group16.view.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * TitlePanel displays the main title screen and map selection menu.
 * It observes the {@link TitleLogic} to render the current state and selected option.
 */
public class TitlePanel extends JPanel implements Observer {

    private TitleLogic.MenuState menuState;
    private int selectedOption = 0;

    private BufferedImage bgImage;
    private BufferedImage titleImage;
    private BufferedImage startButtonImage;
    private BufferedImage buttonBg;
    private BufferedImage subTitleImage;

    /**
     * Constructs the title panel and loads all static images.
     */
    public TitlePanel() {
        try {
            bgImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/Starting_Screen/background_start.png")));
            titleImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/Starting_Screen/titlegame.png")));
            startButtonImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/Starting_Screen/startgame_button.png")));
            buttonBg = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/Starting_Screen/empty_block.png")));
            subTitleImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/Starting_Screen/subTitleGame.png")));
        } catch (Exception ignored) {}
    }

    /**
     * Renders the title screen or map selection screen depending on the current menu state.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // 1) Background
        g2.drawImage(bgImage, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, null);

        // 2) Main title image
        int scaledWidth = titleImage.getWidth() / 2;
        int scaledHeight = (int) (titleImage.getHeight() / 2.5);
        int tx = (GameConfig.SCREEN_WIDTH - scaledWidth) / 2;
        int ty = 0;
        g2.drawImage(titleImage, tx, ty - 50, scaledWidth, scaledHeight, null);

        // 3) Subtitle
        scaledWidth = subTitleImage.getWidth() / 2;
        scaledHeight = (int) (subTitleImage.getHeight() / 2.5);
        tx = (GameConfig.SCREEN_WIDTH - scaledWidth) / 2;
        g2.drawImage(subTitleImage, tx - 10, ty + 15, scaledWidth, scaledHeight, null);

        g2.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Color.WHITE);

        // 4) Menu rendering based on state
        if (menuState == TitleLogic.MenuState.START) {
            drawStartButton(g2);
        } else if (menuState == TitleLogic.MenuState.MAP_SELECT) {
            drawMapSelection(g2, fm);
        }
    }

    /**
     * Draws the "Start Game" button in the START menu.
     */
    private void drawStartButton(Graphics2D g2) {
        int buttonWidth = startButtonImage.getWidth();
        int buttonHeight = startButtonImage.getHeight();
        int bx = (GameConfig.SCREEN_WIDTH - buttonWidth) / 2;
        int by = GameConfig.SCREEN_HEIGHT / 2;
        g2.drawImage(startButtonImage, bx, by, buttonWidth, buttonHeight, null);
    }

    /**
     * Draws the map selection screen with highlighting and navigation arrow.
     */
    private void drawMapSelection(Graphics2D g2, FontMetrics fm) {
        String[] options = {"Map1", "Map2", "Back"};
        int startY = GameConfig.SCREEN_HEIGHT / 2 - fm.getHeight();
        int spacing = 30;

        for (int i = 0; i < options.length; i++) {
            String opt = options[i];
            int textW = fm.stringWidth(opt);
            int textH = fm.getHeight();

            double scaleFactor = 1.5;
            int btnW = (int) ((textW + 20) * scaleFactor);
            int btnH = (int) ((textH + 20) * scaleFactor);
            int bx = (GameConfig.SCREEN_WIDTH - btnW) / 2;
            int by = startY + i * (btnH + spacing);

            // Highlight the selected option
            if (i == selectedOption) {
                g2.setColor(new Color(255, 255, 0, 150)); // translucent yellow
                g2.fillRoundRect(bx, by, btnW, btnH, 16, 16);

                // Draw left-pointing yellow arrow
                int arrowX = bx - 30;
                int arrowY = by + btnH / 2;
                Polygon arrow = new Polygon();
                arrow.addPoint(arrowX + 30, arrowY);
                arrow.addPoint(arrowX + 10, arrowY - 10);
                arrow.addPoint(arrowX + 10, arrowY + 10);
                g2.setColor(Color.YELLOW);
                g2.fillPolygon(arrow);
            }

            // Button background
            g2.drawImage(buttonBg, bx, by, btnW, btnH, null);

            // Button text
            int tx2 = bx + (btnW - textW) / 2;
            int ty2 = by + ((btnH - textH) / 2) + fm.getAscent();
            g2.setColor(Color.BLACK);
            g2.drawString(opt, tx2, ty2);
        }
    }

    /**
     * Updates the panel state based on the model.
     * @param subject must be an instance of {@link TitleLogic}
     */
    @Override
    public void update(Subject subject) {
        if (!(subject instanceof TitleLogic model)) return;
        this.selectedOption = model.getSelectedOption();
        this.menuState = model.getMenuState();
    }
}
