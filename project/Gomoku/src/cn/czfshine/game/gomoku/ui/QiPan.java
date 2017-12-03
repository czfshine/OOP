package cn.czfshine.game.gomoku.ui;


/**
 * 程序主面板
 */

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;// Image 的子类，描述具有可访问图像数据缓冲区的 Image
import java.io.File;//文件流的输入输出
import java.io.IOException;//输入输出流异常

import javax.imageio.ImageIO;//javax.imageio 的所有实现都提供以下标准图像格式插件:JPEG,PNG,BMP,WBMP,GIF
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.czfshine.game.gomoku.ai.AI;
import cn.czfshine.game.gomoku.ai.K_TreeModel;

public class QiPan extends JPanel {

    /**
     * 默认属性
     */
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    private static int MARGIN = 20;

    /**
     * AI
     */
    private K_TreeModel board;
    private AI AI;


    /**
     * 用户下棋
     * @param row
     * @param col
     * @return
     * @throws Exception
     */
    private boolean userplay(int row,int col) throws Exception {
        /**
         * 选手默认是白色的，在该位置下一步，并判断是否胜利
         */
        boolean win = QiPan.this.board.play(row, col, K_TreeModel.WHITE);

        QiPan.this.drawChessman(row, col);
        if (win) {
            JOptionPane.showMessageDialog(QiPan.this, "Congratulation! You win!");
            QiPan.this.board.clear();
            QiPan.this.repaint();

            return false;
        } else if (QiPan.this.board.isFull()) {
            JOptionPane.showMessageDialog(QiPan.this, "No empty cell!");
            QiPan.this.board.clear();
            QiPan.this.repaint();
            return false;
        }
        return true;
    }

    /**
     * 电脑下棋
     * @param row
     * @param col
     */
    private void AIplay(int row,int col){
        boolean win = AI.play();

        /**
         * 得到最后一个下棋点
         */
        int[] last = QiPan.this.board.getLastPut();
        row = last[0];
        col = last[1];
        QiPan.this.drawChessman(row, col);

        if (win) {
            JOptionPane.showMessageDialog(QiPan.this, "Computer win. Try again!");
            QiPan.this.board.clear();
            QiPan.this.repaint();
        } else if (QiPan.this.board.isFull()) {
            JOptionPane.showMessageDialog(QiPan.this, "No empty cell!");
            QiPan.this.board.clear();
            QiPan.this.repaint();
        }
    }
    public QiPan(K_TreeModel board) {
        this.board = board;
        AI = new AI(board, K_TreeModel.BLACK);

        this.setBackground(Color.WHITE);

        /**
         * 监听器
         */
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                Point p = e.getPoint();
                int row, col;
                int dim = QiPan.this.board.getDimension();
                int cellWidth = (getWidth() - 2 * MARGIN) / dim;

                /**
                 * 是否在面板有效的内部
                 */
                if (p.y >= MARGIN && p.y < MARGIN + dim * cellWidth && p.x >= MARGIN
                        && p.x < MARGIN + dim * cellWidth) {

                    /**
                     * 计算出下棋的行和列
                     */
                    row = (p.y - MARGIN) / cellWidth;
                    col = (p.x - MARGIN) / cellWidth;

                    /**
                     * 已经被下过了，不响应事件
                     */
                    if (!QiPan.this.board.isEmpty(row, col))
                        return;
                    try {
                        if(userplay(row,col)){
                            /**
                             * 轮到电脑下棋
                             */
                            AIplay(row,col);
                        };
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        /**
         * 绘制背景图片
         */
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("./b.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, this);//边框
        g.drawImage(img, 10, 10, WIDTH - 25, HEIGHT - 20, this);


        /**
         * 绘制提示
         */
        // write tip
        g2d.setColor(Color.BLACK);
        g2d.drawString("Computer", MARGIN + 100, MARGIN * 2 / 3 + 10);
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds("Computer", g2d);
        int r = MARGIN - 2;
        g2d.drawOval(MARGIN + 100 + (int) rect.getWidth() + r, 10, r, r);
        g2d.fillOval(MARGIN + 100 + (int) rect.getWidth() + r, 10, r, r);

        g2d.setColor(Color.white);
        g2d.drawString("User", WIDTH / 2 + 30, MARGIN * 2 / 3 + 10);
        rect = fm.getStringBounds("User", g2d);
        g2d.drawOval(WIDTH / 2 + 30 + (int) rect.getWidth() + r, 10, r, r);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(WIDTH / 2 + 30 + (int) rect.getWidth()+ r, 10, r, r);

    }


    /**
     * 绘制棋子
     * @param row
     * @param col
     */
    private void drawChessman(int row, int col) {
        int chessman = board.getStatus(row, col);
        if (chessman != K_TreeModel.BLACK && chessman != K_TreeModel.WHITE)
            return;
        int x, y;
        int cellWidth = (getWidth() - 2 * MARGIN) / board.getDimension();
        x = MARGIN + col * cellWidth;
        y = MARGIN + row * cellWidth;
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        int pad = 2;
        Color color = chessman == K_TreeModel.BLACK ? Color.BLACK : Color.WHITE;
        g2d.setColor(color);
        g2d.fillOval(x + pad, y + pad, cellWidth - 2 * pad, cellWidth - 2 * pad);
    }
}