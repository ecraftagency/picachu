package com.ss.gameLogic.model.util;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class PathFinder<T> {
  private int row = 0;
  private int col = 0;
  public ArrayList<Path> path;
  private int[][] arr;

  public void printPath() {
    for (int i = 0; i < path.size(); i++)
      Gdx.app.log("line 1: ", path.get(i).toString());
  }

  public PathFinder(T[][] a, int _r, int _c) {
    this.row = _r + 2;
    this.col = _c + 2;
    path = new ArrayList<Path>();

    this.arr = new int[row][col];
    for (int r = 0; r < row; r++)
      for (int c = 0; c < col; c++) {
        if (r >= 1 && r < row - 1 && c >= 1 && c < col - 1)
          arr[r][c] = (a[r - 1][c - 1] == null) ? 0 : 1;
        else
          arr[r][c] = 0;
      }
  }

  public boolean findPath(Point p1, Point p2) {
    Point tp1 = new Point(p1.r + 1, p1.c + 1);
    Point tp2 = new Point(p2.r + 1, p2.c + 1);

    arr[tp1.r][tp1.c] = 0;
    arr[tp2.r][tp2.c] = 0;

    if (tp1.r == tp2.r)
      if (checkLineRow(tp1.c, tp2.c, tp1.r))
        return true;
    if (tp1.c == tp2.c)
      if (checkLineCol(tp1.r, tp2.r, tp1.c))
        return true;
    if (checkRectRow(tp1, tp2))
      return true;
    if (checkRectCol(tp1,tp2))
      return true;
    if (checkMoreLineRow(tp1, tp2, 1))
      return true;
    if (checkMoreLineRow(tp1, tp2, -1))
      return true;
    if (checkMoreLineCol(tp1, tp2, 1))
      return true;
    if (checkMoreLineCol(tp1, tp2, -1))
      return true;

    return false;
  }

  private boolean checkLineRow(int c1, int c2, int r) {
    int cmin = (c1 > c2) ? c2 : c1;
    int cmax = (c1 > c2) ? c1 : c2;
    for (int i = cmin; i <= cmax; i++)
      if (arr[r][i] == 1)
        return false;

    path.add(new Path(new Point(r-1, cmin-1 ), new Point(0, Math.abs(cmax-cmin))));
    return true;
  }

  private boolean checkLineCol(int r1, int r2, int c) {
    int rmin = (r1 > r2) ? r2 : r1;
    int rmax = (r1 > r2) ? r1 : r2;

    for (int i = rmin; i <= rmax; i++)
      if (arr[i][c] == 1)
        return false;

    path.add(new Path(new Point(rmin-1,c-1), new Point(Math.abs(rmax-rmin), 0)));
    return true;
  }

  private boolean checkRectRow(Point p1, Point p2) {
    Point pmin = p1; Point pmax = p2;
    if (p1.r > p2.r) {pmin = p2;pmax = p1;}
    for (int r = pmin.r + 1; r < pmax.r; r++)
      if (checkLineCol(pmin.r, r, pmin.c) && checkLineRow(pmin.c, pmax.c, r) && checkLineCol(r, pmax.r, pmax.c))
        return true;
      else
        path.clear();
    return false;
  }

  private boolean checkRectCol(Point p1, Point p2) {
    Point pmin = p1; Point pmax = p2;
    if (p1.c > p2.c) {pmin = p2; pmax = p1;}
    for (int c = pmin.c + 1; c < pmax.c; c++)
      if (checkLineRow(pmin.c, c, pmin.r) && checkLineCol(pmin.r, pmax.r, c) && checkLineRow(c, pmax.c, pmax.r))
        return true;
      else
        path.clear();
    return false;
  }

  private boolean checkMoreLineRow(Point p1, Point p2, int direction) {
    Point pmin = p1; Point pmax = p2;
    if (p1.c > p2.c) {pmin = p2; pmax = p1;}

    int c = pmax.c; int r = pmin.r;
    if (direction == -1) {c = pmin.c; r = pmax.r;}

    if (checkLineRow(pmin.c, pmax.c, r))
      while (c < arr[0].length && arr[pmin.r][c] != 1 && arr[pmax.r][c] != 1){
        if (this.checkLineCol(pmin.r, pmax.r, c)) {
          Path d = path.get(path.size() - 1);
          if (d != null){
            Path pt1 = new Path(new Point(p1.r-1,p1.c-1), new Point(0,direction*Math.abs(p1.c-1 - d.sp.c)));
            Path pt2 = new Path(new Point(p2.r - 1, p2.c-1), new Point(0,direction*Math.abs(p2.c-1 - d.sp.c)));
            path.add(pt1); path.add(pt2); path.add(d);
            return true;
          }
        }
        c += direction;
      }
    return false;
  }

  private boolean checkMoreLineCol(Point p1, Point p2, int direction) {
    Point pmin = p1; Point pmax = p2;
    if (p1.r > p2.r) {pmin = p2; pmax = p1;}

    int r = pmax.r; int c = pmin.c;
    if (direction == -1) { r = pmin.r; c = pmax.c;}

    if (checkLineCol(pmin.r, pmax.r, c))
      while (r < arr.length && arr[r][pmin.c] != 1 && arr[r][pmax.c] != 1) {
        if (checkLineRow(pmin.c, pmax.c, r)) {
          Path d = path.get(path.size() - 1);
          if (d != null) {
            Path pt1 = new Path(new Point(p1.r-1,p1.c-1), new Point(direction*Math.abs(p1.r-1 - d.sp.r), 0));
            Path pt2 = new Path(new Point(p2.r-1,p2.c-1), new Point(direction*Math.abs(p2.r-1 - d.sp.r), 0));
            path.add(pt1); path.add(pt2); path.add(d);
          }
          return true;
        }
        r += direction;
      }
    return false;
  }
}
