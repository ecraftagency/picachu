package com.ss.gameLogic.model.util;

import com.ss.gameLogic.model.AniModel;

import java.util.ArrayList;

public class SlicePartition {
  public int sliceAnchor; // 0 - slice ve 0 | 1 slice ve cuoi

  // 0 - slice ngang | 1 - slice dung
  // 0 -> partition row | 1 -> partition col
  // 0 -> nullSliceH    | 1 -> nullsliceV
  public int sliceDirection;

  public int start;
  public int end;
  AniModel[][] anis;

  public SlicePartition(AniModel[][] anis, int sliceDirection, int sliceAnchor, int start, int end) {
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
