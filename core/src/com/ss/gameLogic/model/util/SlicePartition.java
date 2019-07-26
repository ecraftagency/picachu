package com.ss.gameLogic.model.util;

import com.badlogic.gdx.utils.Array;
import com.ss.gameLogic.data.Level;
import com.ss.gameLogic.model.AniModel;

import java.util.ArrayList;

public class SlicePartition {
  public static Array<SlicePartition> instances = new Array<>();

  public static void initPartitions(int level, AniModel[][] array) {
    Level lv = Level.getLevelData(level);
    if (level == 0)
      return;
    instances = new Array<SlicePartition>();

    if (lv.nPartition == 1) {
      SlicePartition p1 = new SlicePartition(array, lv.sliceDirection, lv.sliceAnchor, 0, array.length - 1);
      instances.add(p1);
    }
    else if (lv.nPartition == 2) {
      if (array.length < 4 || array[0].length < 4)
        return;
      if (lv.sliceDirection == 0) { // vertical slice
        int anchor1 = lv.sliceAnchor;
        int anchor2 = (anchor1 == 0) ? 1 : 0;
        int e = (array.length % 2 == 0) ? array.length/2 - 1 : array.length/2;
        int s = (array.length % 2 == 0) ? array.length/2 : array.length/2 + 1;
        SlicePartition p1 = new SlicePartition(array, lv.sliceDirection, anchor1, 0, e);
        SlicePartition p2 = new SlicePartition(array, lv.sliceDirection, anchor2, s, array.length - 1);
        instances.add(p1);
        instances.add(p2);
      }
      else if (lv.sliceDirection == 1) { //horizon slice
        int anchor1 = lv.sliceAnchor;
        int anchor2 = (anchor1 == 0) ? 1 : 0;
        int e = (array[0].length % 2 == 0) ? array[0].length/2 - 1 : array[0].length/2;
        int s = (array[0].length % 2 == 0) ? array[0].length/2 : array[0].length/2 + 1;
        SlicePartition p1 = new SlicePartition(array, lv.sliceDirection, anchor1, 0, e);
        SlicePartition p2 = new SlicePartition(array, lv.sliceDirection, anchor2, s, array[0].length - 1);
        instances.add(p1);
        instances.add(p2);
      }
    }
  }

  private int sliceAnchor;
  private int sliceDirection;

  private int start;
  public int end;
  private AniModel[][] anis;

  private SlicePartition(AniModel[][] anis, int sliceDirection, int sliceAnchor, int start, int end) {
    this.sliceDirection = sliceDirection;
    this.sliceAnchor = sliceAnchor;
    this.start = start;
    this.end = end;
    this.anis = anis;
  }

  private AniModel[][] buildPartition() {
    int d = end - start + 1;
    if (d == anis.length || d == anis[0].length)
      return anis;

    if (sliceDirection == 0) {
      AniModel[][] partition = new AniModel[d][anis[0].length];
      for (int i = 0; i < d; i++) {
        for (int j = 0; j < anis[0].length; j++)
          partition[i][j] = anis[start + i][j];
      }
      return partition;
    }
    else {
      AniModel[][] partition = new AniModel[anis.length][d];
      for (int i = 0; i < anis.length; i++) {
        for (int j = 0; j < d; j++)
          partition[i][j] = anis[i][start + j];
      }
      return partition;
    }
  }

  public ArrayList<Tuple<AniModel, D>> calcSlices(AniModel ani) {
    AniModel[][] partition = this.buildPartition();
    ArrayList<Tuple<AniModel, D>> slices = new ArrayList<Tuple<AniModel, D>>();

    if (sliceDirection == 0) {
      Utils.nullSliceV(partition, slices, this.sliceAnchor, ani.getCol());
    }
    else if (sliceDirection == 1){
      Utils.nullSliceH(partition, slices, this.sliceAnchor, ani.getRow());
    }
    return slices;
  }
}
