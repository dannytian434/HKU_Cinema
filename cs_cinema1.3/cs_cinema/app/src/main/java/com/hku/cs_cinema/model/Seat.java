package com.hku.cs_cinema.model;


public class Seat
{

  private String n = null;

  private String damagedFlg = null;

  private String loveInd = null;

  public void setN(String paramString)
  {
    this.n = paramString;
  }

  public boolean a()
  {
    return ("1".equals(this.loveInd)) || ("2".equals(this.loveInd));
  }

  public String getN()
  {
    return this.n;
  }

  public void setDamagedFlg(String paramString)
  {
    this.damagedFlg = paramString;
  }

  public String getDamagedFlg()
  {
    return this.damagedFlg;
  }

  public void setLoveInd(String paramString)
  {
    this.loveInd = paramString;
  }

  public String getLoveInd()
  {
    return this.loveInd;
  }
}