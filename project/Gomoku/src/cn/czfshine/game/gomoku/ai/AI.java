package cn.czfshine.game.gomoku.ai;

/**
 * AI 电脑选手
 */
public class AI {

    /**
     * 两个模型
     */
    private NNModel nn=new NNModel();
    private K_TreeModel board;
    private int chessman;


    public AI(K_TreeModel board, int chessman) {
        this.board = board;
        this.chessman = chessman;
    }


    /**
     * 得到原始的数据（深复制）
     * @return
     */
    private int[][] scanner() {
        int[][] b;
        b = new int[board.getDimension()][];
        for (int i = 0; i < b.length; ++i) {
            b[i] = new int[board.getDimension()];
            for (int j = 0; j < b[i].length; ++j)
                b[i][j] = board.getElementInBoard(i, j);
        }
        return b;
    }

    public int[][] show() {
        int[][] b;
        b = new int[board.getDimension()][];
        for (int i = 0; i < b.length; ++i) {
            b[i] = new int[board.getDimension()];
            for (int j = 0; j < b[i].length; ++j)
                System.out.print(board.getElementInBoard(i, j));
            System.out.println();
        }
        return b;
    }
    /**
     * 得到发送的数据（深复制）
     * @return
     */
    private int[][] todata() {
        int[][] b;
        b = new int[board.getDimension()][];
        for (int i = 0; i < b.length; ++i) {
            b[i] = new int[board.getDimension()];
            for (int j = 0; j < b[i].length; ++j)
                b[i][j] = board.getElementInBoard(i, j)>0?1:0;
        }
        return b;
    }

    /**
     * 下棋
     * @param x
     * @param y
     * @return
     */
    private boolean set(int x, int y) {
        try {
            return board.play(x, y, chessman);
        } catch (Exception e) {
            //impossible to get here
            return false;
        }
    }

    /**
     * 四子连珠
     * @param n
     * @return
     */
    private boolean isOppoFour(int n) {

        int oppo = 3 - chessman;

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                if (board.try_do(i, j, oppo)) {
                    set(i, j);
                    return true;
                }
        return false;
    }

    /**
     * 置零
     * @param a
     */
    private void setZero(int[][] a) {
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[i].length; j++)
                a[i][j] = 0;
    }

    /**
     * 暴力决策
     * @return
     */
    private boolean solve() {
        int oppo = 3 - chessman;
        int n = board.getDimension();
        int[][] a = scanner();

        /**
         * 情况1.已经四子了
         */
        if (isOppoFour(n))
            return true;

        /**
         * 加权值
         */
        int[][] sum = new int[n][];
        for (int i = 0; i < sum.length; ++i)
            sum[i] = new int[n];
        setZero(sum);

        /**
         * 尝试下两步，看那个位置最优
         */
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) {
                for (int x = 0; x < n; ++x)
                    for (int y = 0; y < n; ++y) {
                        if (board.try_do(i, j, x, y, chessman))
                            sum[i][j] += 1;
                    }
            }
        int mx = 0;
        int x = 0, y = 0;
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) {
                if (mx <= sum[i][j]) {
                    mx = sum[i][j];
                    x = i;
                    y = j;
                }
            }
        /**
         * 超过阈值直接下
         */
        if (mx >= 2) {
            set(x, y);
            return true;
        }


        /**
         * 八个方向尝试下几步，直到胜利
         */
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j)
                if (a[i][j] == 0) {
                    a[i][j] = chessman;
                    int s1 = -2, s2 = -2, s3 = -2, s4 = -2;
                    int X, Y;

                    X = i;
                    Y = j;
                    while (X >= 0 && X < n && Y >= 0 && Y < n && a[X][Y] == chessman) {
                        X += -1;
                        Y += -1;
                        ++s1;
                    }
                    X = i;
                    Y = j;
                    while (X >= 0 && X < n && Y >= 0 && Y < n && a[X][Y] == chessman) {
                        X -= -1;
                        Y -= -1;
                        ++s1;
                    }

                    X = i;
                    Y = j;
                    while (X >= 0 && X < n && Y >= 0 && Y < n && a[X][Y] == chessman) {
                        X += 0;
                        Y += -1;
                        ++s2;
                    }
                    X = i;
                    Y = j;
                    while (X >= 0 && X < n && Y >= 0 && Y < n && a[X][Y] == chessman) {
                        X -= 0;
                        Y -= -1;
                        ++s2;
                    }

                    X = i;
                    Y = j;
                    while (X >= 0 && X < n && Y >= 0 && Y < n && a[X][Y] == chessman) {
                        X += -1;
                        Y += 0;
                        ++s3;
                    }
                    X = i;
                    Y = j;
                    while (X >= 0 && X < n && Y >= 0 && Y < n && a[X][Y] == chessman) {
                        X -= -1;
                        Y -= 0;
                        ++s3;
                    }

                    X = i;
                    Y = j;
                    while (X >= 0 && X < n && Y >= 0 && Y < n && a[X][Y] == chessman) {
                        X += -1;
                        Y += +1;
                        ++s4;
                    }
                    X = i;
                    Y = j;
                    while (X >= 0 && X < n && Y >= 0 && Y < n && a[X][Y] == chessman) {
                        X -= -1;
                        Y -= +1;
                        ++s4;
                    }

                    if (s1 + s2 + s3 + s4 >= 4) {
                        set(i, j);
                        return true;
                    }

                    a[i][j] = 0;
                }
        }


        /**
         * 尝试下两步，不过阈值变小（强行找）
         */
        setZero(sum);
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) {
                for (int e = 0; e < n; ++e)
                    for (int r = 0; r < n; ++r) {
                        if (board.try_do(i, j, e, r, oppo))
                            sum[i][j] += 1;
                    }
            }
        mx = 0;
        x = 0;
        y = 0;
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) {
                if (mx <= sum[i][j]) {
                    mx = sum[i][j];
                    x = i;
                    y = j;
                }
            }
        if (mx >= 2) {
            set(x, y);
            return true;
        }


        /**
         * 都找不到，只能预测未来三步的情况，为每个坐标加权
         */
        setZero(sum);
        for (int q = 0; q < n; ++q)
            for (int w = 0; w < n; ++w)
                for (int i = 0; i < n; ++i)
                    for (int j = 0; j < n; ++j) {
                        for (int e = 0; e < n; ++e)
                            for (int r = 0; r < n; ++r) {
                                if (board.try_do(q, w, i, j, e, r, chessman))
                                    sum[i][j] += 1;
                            }
                    }
        mx = 0;
        x = y = 0;
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) {
                if (mx <= sum[i][j]) {
                    mx = sum[i][j];
                    x = i;
                    y = j;
                }
            }
        if (mx >= 3) {
            set(x, y);
            return true;
        }

        return false;
    }

    /**
     * 电脑选手下一步
     * @return
     */
    public boolean play() {
        int[] anotherPlayerPut = board.getLastPut();

        /**
         * 第一步下中间
         */
        if (anotherPlayerPut[0] < 0) {
            try {
                board.play(board.getDimension() / 2, board.getDimension() / 2, chessman);
            } catch (Exception e) {
            }
            return false;
        }

        int row = anotherPlayerPut[0], col = anotherPlayerPut[1];
        int nearRow = -1, nearCol = -1, dis = 2 * board.getDimension();


        /**
         * 先用神经网络模型预测
         */
        Point p;
        p=nn.nextStep(todata());

        /**
         * 是否成功
         */
        if(board.isEmpty(p.getRow(),p.getCol())){
            return  set(p.getRow(),p.getCol());
        }

        /**
         * 看下一步有没有能赢的
         */
        for (int k = 0; k < board.getDimension(); ++k)
            for (int j = 0; j < board.getDimension(); ++j)
                if (board.isEmpty(k, j)) {
                    if (Math.abs(k - row) + Math.abs(j - col) < dis) {
                        nearCol = j;
                        nearRow = k;
                        dis = Math.abs(k - row) + Math.abs(j - col);
                    }
                    if (board.try_do(k, j, chessman)) {
                        try {
                            return board.play(k, j, chessman);
                        } catch (Exception e) {
                            //impossible to get here
                            return false;
                        }
                    }
                }


        /**
         * 没有就暴力
         */
        if (solve()) {
            return false;
        }

        /**
         * 实在赢不了就判断有没有三子的
         */
        if (checkLeftToRight(row, col))
            return false;
        if (checkUpToDown(row, col))
            return false;
        if (checkLeftUpToRightDown(row, col))
            return false;
        if (checkLeftDownToRightUp(row, col))
            return false;
        try {
            /**
             * 最后实在不行就下在附件
             */
            board.play(nearRow, nearCol, chessman);
        } catch (Exception e) {
        }
        return false;
    }

    private boolean checkLeftToRight(int row, int col) {
        return check(row, col, 0, 1);
    }

    private boolean checkUpToDown(int row, int col) {
        return check(row, col, 1, 0);
    }

    private boolean checkLeftUpToRightDown(int row, int col) {
        return check(row, col, 1, 1);
    }

    private boolean checkLeftDownToRightUp(int row, int col) {
        return check(row, col, -1, 1);
    }

    /**
     * 三子连珠的
     * @param row
     * @param col
     * @param drow
     * @param dcol
     * @return
     */
    private boolean check(int row, int col, int drow, int dcol) {
        int startRow = row - drow, startCol = col - dcol,
                endRow = row + drow, endCol = col + dcol;
        int chessman = board.getStatus(row, col);
        int sameChessNum = 1;
        while (startRow >= 0 && startCol >= 0 &&
                startRow < board.getDimension() &&
                startCol < board.getDimension()
                && board.getStatus(startRow, startCol) == chessman) {
            startRow -= drow;
            startCol -= dcol;
            ++sameChessNum;
        }
        while (endRow >= 0 && endCol >= 0
                && endRow < board.getDimension()
                && endCol < board.getDimension()
                && board.getStatus(endRow, endCol) == chessman) {
            endRow += drow;
            endCol += dcol;
            ++sameChessNum;
        }
        if (sameChessNum >= 3) {//more than 3  chessman in a row
            if (startRow >= 0 && startCol >= 0 && board.isEmpty(startRow, startCol)) {
                try {
                    board.play(startRow, startCol, this.chessman);
                    return true;
                } catch (Exception e) {
                }
            } else if (endRow < board.getDimension()
                    && endCol < board.getDimension() && board.isEmpty(endRow, endCol)) {
                try {
                    board.play(endRow, endCol, this.chessman);
                    return true;
                } catch (Exception e) {
                }
            }
        }
        return false;
    }
}