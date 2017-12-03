package cn.czfshine.game.gomoku.ai;

/**
 * 基于决策树的AI模型
 */
public class K_TreeModel {

    /**
     * 常量
     */
    public static final int EMPTY = 0;//未下
    public static final int BLACK = 1;//黑色
    public static final int WHITE = 2;//白色

    /**
     * 棋局数组
     */
    private int[][] board;
    private int lastPutRow, lastPutCol;


    public K_TreeModel(int dimension) {
        board = new int[dimension][dimension];
        clear();
    }

    /**
     * 获取原始数据
     * @param x
     * @param y
     * @return
     */
    public int getElementInBoard(int x, int y) {
        return board[x][y];
    }

    /**
     * 维度，默认15
     * @return
     */
    public int getDimension() {
        return board.length;
    }

    /**
     * 当前位置的状态
     * @param row
     * @param col
     * @return
     */
    public int getStatus(int row, int col) {
        return board[row][col];
    }

    /**
     * 该位置能否下棋
     * @param row
     * @param col
     * @return
     */
    public boolean isEmpty(int row, int col) {
        return EMPTY == getStatus(row, col);
    }

    /**
     * 棋盘是否下满
     * @return
     */
    public boolean isFull() {
        for (int k = 0; k < board.length; ++k)
            for (int j = 0; j < board.length; ++j)
                if (isEmpty(k, j))
                    return false;
        return true;
    }

    /**
     * 清空，重来
     */
    public void clear() {
        for (int k = 0; k < board.length; ++k)
            for (int j = 0; j < board.length; ++j)
                board[k][j] = EMPTY;
        lastPutRow = lastPutCol = -1;
    }


    /**
     * 得到最后一次落子
     * @return
     */
    public int[] getLastPut() {
        return new int[]{lastPutRow, lastPutCol};
    }

    /**
     * 尝试接下来的一步
     * @param row
     * @param col
     * @param chessman
     * @return
     */
    public boolean try_do(int row, int col, int chessman) {
        if (!isEmpty(row, col))
            return false;
        if (chessman != BLACK && chessman != WHITE)
            return false;
        board[row][col] = chessman;
        //lastPutRow=row;
        //lastPutCol=col;
        boolean win = checkWin(row, col);
        board[row][col] = EMPTY;
        return win;
    }

    /**
     * 尝试接下来的两步
     * @param row1
     * @param col1
     * @param row2
     * @param col2
     * @param chessman
     * @return
     */
    public boolean try_do(int row1, int col1, int row2, int col2, int chessman) {
        if (!isEmpty(row1, col1) || !isEmpty(row2, col2))
            return false;
        if (chessman != BLACK && chessman != WHITE)
            return false;
        board[row1][col1] = chessman;
        board[row2][col2] = chessman;
        //lastPutRow=row;
        //lastPutCol=col;
        boolean win = checkWin(row1, col1) || checkWin(row2, col2);
        board[row1][col1] = EMPTY;
        board[row2][col2] = EMPTY;
        return win;
    }

    /**
     * 尝试接下来的三步
     * @param row1
     * @param col1
     * @param row2
     * @param col2
     * @param row3
     * @param col3
     * @param chessman
     * @return
     */
    public boolean try_do(int row1, int col1, int row2, int col2, int row3, int col3, int chessman) {
        if (!isEmpty(row1, col1) || !isEmpty(row2, col2) || !isEmpty(row3, col3))
            return false;
        if (chessman != BLACK && chessman != WHITE)
            return false;
        board[row1][col1] = chessman;
        board[row2][col2] = chessman;
        board[row3][col3] = chessman;

        boolean win = checkWin(row1, col1) || checkWin(row2, col2) || checkWin(row3, col3);
        board[row1][col1] = EMPTY;
        board[row2][col2] = EMPTY;
        board[row3][col3] = EMPTY;
        return win;
    }

    /**
     * 下一步棋
     * @param row
     * @param col
     * @param chessman
     * @return
     * @throws Exception
     */
    public boolean play(int row, int col, int chessman) throws Exception {
        if (!isEmpty(row, col))
            throw new Exception("Non-empty position!");
        if (chessman != BLACK && chessman != WHITE)
            throw new Exception("Invalid chessman");
        board[row][col] = chessman;
        lastPutRow = row;
        lastPutCol = col;
        return checkWin(row, col);
    }

    /**
     * 判断是否胜利
     * @param row
     * @param col
     * @return
     */
    private boolean checkWin(int row, int col) {

        /**
         * 从左到右
         */
        int chessman = getStatus(row, col);
        int maxnumofpoint = 1;
        for (int left = col - 1;
             left >= 0 && getStatus(row, left) == chessman;
             --left, ++maxnumofpoint)
            ;
        for (int right = col + 1;
             right < getDimension() && getStatus(row, right) == chessman;
             ++right, ++maxnumofpoint)
            ;
        if (maxnumofpoint >= 5)
            return true;


        /**
         * 从上到下
         */
        maxnumofpoint = 1;
        for (int up = row - 1;
             up >= 0 && getStatus(up, col) == chessman;
             --up, ++maxnumofpoint)
            ;
        for (int down = row + 1;
             down < getDimension() && getStatus(down, col) == chessman;
             ++down, ++maxnumofpoint)
            ;
        if (maxnumofpoint >= 5)
            return true;

        /**
         * 左上到右下
         */
        maxnumofpoint = 1;
        for (int left = col - 1, up = row - 1;
             left >= 0 && up >= 0 && getStatus(up, left) == chessman;
             --left, --up, ++maxnumofpoint)
            ;
        for (int right = col + 1, down = row + 1;
             right < getDimension() && down < getDimension() && getStatus(down, right) == chessman;
             ++right, ++down, ++maxnumofpoint)
            ;
        if (maxnumofpoint >= 5)
            return true;

        /**
         * 右上到左下
         */
        maxnumofpoint = 1;
        for (int left = col - 1, down = row + 1;
             left >= 0 && down < getDimension() && getStatus(down, left) == chessman;
             --left, ++down, ++maxnumofpoint)
            ;
        for (int right = col + 1, up = row - 1;
             right < getDimension() && up >= 0 && getStatus(up, right) == chessman;
             ++right, --up, ++maxnumofpoint)
            ;
        if (maxnumofpoint >= 5)
            return true;
        return false;
    }
}