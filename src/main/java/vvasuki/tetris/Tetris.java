package javalib;

import java.util.Random;

class Cuboid extends Block {
  Cuboid(int w, int h) {
    setSize(w, h);
    for (int i = 0; i < HEIGHT; i++)
      for (int j = 0; j < WIDTH; j++)
        block[i][j] = 1;
  }
}

class Tetris {
  static boolean bEnd = false;
  static Random randomGenerator = new Random();
  static Block getRandomBlock() {
    //, new Cuboid(2, 2)
    Block[] blocks = {new Rod(1), new Rod(2), new Rod(3), new Rod(4)};
    int blockId = randomGenerator.nextInt(blocks.length);
//    System.out.println("Getting block " + blockId);
    return blocks[blockId];
  }
  
  public static void main(String[] args) {
    Board board = new Board();
    Block nextBlock = getRandomBlock();
    while(!bEnd) {
      Block block = nextBlock;
      nextBlock = getRandomBlock();
      bEnd = block.apparate(board);
      if(bEnd) break;
      System.out.println("\n====================================\n");
      block.anchor(board, nextBlock);
      board.updateScore();
      
      System.out.println("Score "+ board.score);
    }
    System.out.println("Game over. ");
  }
}

class Board {
  static int WIDTH = 10;
  static int HEIGHT = 10;
  static int mid = (int) (Math.floor(WIDTH / 2.0));
  // The board is represented as an array of arrays, with 10 rows and 10
  // columns.
  int[][] board = new int[HEIGHT][WIDTH];
  int score = 0;

  public int[][] getBoard() {
    return board;
  }

  public void set(int x, int y, int value) {
    board[x][y] = value;
  }
  
  public boolean checkBounds(int r, int c) {
    return r > -1 && r<HEIGHT && c>-1 && c< WIDTH;
  }
  
  public boolean isFree(int r, int c) {
    return board[r][c] == 0;
  }
  
  void updateScore() {
    int completeLines = 0;
    for(int i=0;i<HEIGHT; i++) {
      boolean bIsComplete = true;
      for(int j = 0; j< WIDTH; j++) {
        if(isFree(i, j))
          bIsComplete = false;
      }
      if(bIsComplete){
        for(int j = i; j >0 ; j--) {
          board[j] = board[j-1];
        }
        board[0] = new int[WIDTH];
        completeLines = completeLines + 1;
      }
    }
    score =  score + completeLines;
  }

  /*
   * Prints the contents of the board and draws a border around it.
   */
  public void print() {
    for (int col = 0; col < WIDTH + 2; col++)
      System.out.print("*");
    System.out.println();

    for (int row = 0; row < HEIGHT; row++) {
      System.out.print("|");
      for (int col = 0; col < WIDTH; col++) {
        int value = board[row][col];
        System.out.print(value == 0 ? " " : "#");
      }
      System.out.println("|");
    }

    for (int col = 0; col < WIDTH + 2; col++)
      System.out.print("*");
    System.out.println();
  }
}
