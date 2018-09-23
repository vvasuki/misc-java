package javalib;

public class Rod extends Block {
  Rod(int size) {
    setSize(1, size);
    for (int i = 0; i < WIDTH; i++)
      block[0][i] = 1;
  }
}

