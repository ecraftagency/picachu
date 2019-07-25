package com.ss.gameLogic.model.util;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Utils {
  public static <T> int nullSliceV(T[][] input, ArrayList<Tuple<T, D>> output, int anchor, int col) {
    int sp = (anchor == 0) ? input.length - 1 : 0;
    int ep = (anchor == 0) ? 0 : input.length - 1;
    int d = (sp < ep) ? 1 : -1;

    for (int i = sp; i != ep + d; i += d) {
      if (input[i][col] != null) {
        int dr = 0;
        for (int j = i + d; j != ep + d; j += d)
          if (input[j][col] == null)
            dr += d;
        if (dr != 0)
          output.add(new Tuple<T, D>(input[i][col], new D(dr, 0)));
      }
    }

    return output.size();
  }

  public static <T> int nullSliceH(T[][] input, ArrayList<Tuple<T, D>> output, int anchor, int row) {
    int sp = (anchor == 0) ? input[0].length - 1 : 0;
    int ep = (anchor == 0) ? 0 : input[0].length - 1;
    int d = (sp < ep) ? 1 : -1;

    for (int i = sp; i != ep + d; i += d) {
      if (input[row][i] != null) {
        int dc = 0;
        for (int j = i + d; j != ep + d; j += d)
          if (input[row][j] == null)
            dc += d;
        if (dc != 0)
          output.add(new Tuple<T, D>(input[row][i], new D(0, dc)));
      }
    }

    return output.size();
  }

  public static <T> int nullSlice(T[][] input, ArrayList<Tuple<T, D>> output, int anchor, int range, Vector2 direction) {
    if (direction.x == 1)
      return nullSliceH(input, output, anchor, range);
    else
      return nullSliceV(input, output, anchor, range);
  }
}

