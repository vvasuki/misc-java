package javalib;

class Block {
  int WIDTH = 4;
  int HEIGHT = 1;
  int[][] block = new int[HEIGHT][WIDTH];
  int row = 0;
  int col = Board.mid - WIDTH / 2;
  boolean bAnchored = false;
  
  public void setSize(int r, int c) {
    WIDTH = c;
    HEIGHT = r;
    block = new int[HEIGHT][WIDTH];
  }
  
  boolean isCellOccupied(int r, int c) {
    if(r<row || r >= row + HEIGHT || c < col || c >=col + WIDTH )
      return false;
    return block[r-row][c-col] == 1;
  }

  public boolean drawable(int r, int c, Board board) {
//    System.out.println("drawable " + r);
//    System.out.println("drawable " + c);
//  
    for (int j = 0; j < HEIGHT; j++)
      for (int i = 0; i < WIDTH; i++) {
        if(!board.checkBounds(r+j, c+i))
          return false;
//        System.out.println("drawable  " + (r + j) + " " + (c + i));
//        System.out.println(board.isFree(r + j, c + i) );
//        System.out.println(isCellOccupied(r + j, c + i) );
        if ( block[j][i] == 1 && (!board.isFree(r + j, c + i) && (r< HEIGHT || !isCellOccupied(r+j, c+i) ))  ) {
//          System.out.println("drawable returning false");
          return false;
        }
      }
//    System.out.println("drawable returning true");
    return true;
  }
  
  public void setLocus(int r, int c) {
    row = r; col = c;
  }
  
  public boolean apparate(Board board) {
    return !checkNDraw(row, col, board);
      
  }
  
  public boolean checkNDraw(int r, int c, Board board) {
    if(!drawable(r, c, board))
      return false;
    eraseBlock(board);
    setLocus(r, c);
    drawBlock(board);
    board.print();
    return true;
  }
  
  public void moveDown(Board board) {
      if(!checkNDraw(row+1, col, board)) bAnchored = true;
  }
  
  public void horizMove(String input, Board board) {
    int colDiff = 0;
    if (input == TetrisUtils.LEFT)
      colDiff = -1;
    else if (input == TetrisUtils.RIGHT)
      colDiff = 1;
    if(!checkNDraw(row+1, col + colDiff, board))
      moveDown(board);
  }
  
  public void print(){
    for(int i=0; i<HEIGHT; i++) {
      for(int j=0;j<WIDTH; j++) {
        if(block[i][j] == 0)
        System.out.print(' ');
        else
          System.out.print('#');
      }
      System.out.print('\n');
    }
  }
  
//  public boolean isAnchored() {
//    
//  }
  

  public void drawBlock(Board board) {
//  System.out.println("draw " + row);
//  System.out.println("draw " + col);
    for (int j = 0; j < HEIGHT; j++)
      for (int i = 0; i < WIDTH; i++) {
        if (block[j][i] == 1)
          board.set(j+row, i + col, 1);
      }
  }

  public void eraseBlock(Board board) {
    for (int j = 0; j < HEIGHT; j++)
      for (int i = 0; i < WIDTH; i++) {
        if (block[j][i] == 1)
          board.set(j + row, i + col, 0);
      }
  }

  public boolean move(int newRow, int newCol, Board board) {
    if (!drawable(newRow, newCol, board))
      return false;
    eraseBlock(board);
    row = newRow;
    col = newCol;
    drawBlock(board);
    return true;
  }

  public void anchor(Board board, Block nextBlock) {
    while(!bAnchored) {
      System.out.println("Next block ");
      nextBlock.print();
      System.out.println("Score "+ board.score);
      String input = TetrisUtils.getInput();
      if (input == TetrisUtils.LEFT || input == TetrisUtils.RIGHT){
        horizMove(input, board);
      }
      else if(input == TetrisUtils.DOWN)
        moveDown(board);
      else
        System.out.println(input);
      System.out.println("\n====================================\n");
    }
    
    
  }

}


