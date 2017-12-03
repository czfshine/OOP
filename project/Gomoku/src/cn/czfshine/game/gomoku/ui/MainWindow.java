package cn.czfshine.game.gomoku.ui;

import cn.czfshine.game.gomoku.ai.K_TreeModel;

import java.awt.BorderLayout;

import javax.swing.JFrame;


/**
 * 主框架
 */
public class MainWindow extends JFrame {
    public MainWindow() {

        /**
         * 普通的预测模型
         */

        K_TreeModel board = new K_TreeModel(15);

        /**
         * 面板
         */
        QiPan panel = new QiPan(board);

        getContentPane().add(panel, BorderLayout.CENTER);
        setSize(panel.getWidth(), 10 + panel.getHeight());

        /**
         * 调整
         */
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}