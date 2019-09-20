package com.pax.order.adapter;


import com.pax.order.FinancialApplication;
import com.pax.dal.IMag;
import com.pax.dal.entity.TrackData;

public class MagReader {
  private static MagReader magTester;

  private IMag iMag;

  private MagReader() {
    iMag = FinancialApplication.getDal().getMag();
  }

  public static MagReader getInstance() {
    if (magTester == null) {
      magTester = new MagReader();
    }
    return magTester;
  }

  public void open() {
    try {
      iMag.open();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void close() {
    try {
      iMag.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  // Reset magnetic stripe card reader, and clear buffer of magnetic stripe card.
  public void reset() {
    try {
      iMag.reset();
    } catch (Exception e) {
      System.out.println(e.getMessage() == null ? "Unexpected exception":e.getMessage());
    }
  }

  // Check whether a card is swiped
  public boolean isSwiped() {
    boolean b = false;
    try {
      b = iMag.isSwiped();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return b;
  }

  public TrackData read() {
    try {
      return iMag.read();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

}