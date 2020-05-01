package com.hku.cs_cinema.model;


import java.util.ArrayList;

public class SeatInfo
{
  private String row = null;
  private String desc = null;
  private ArrayList<Seat> mSeatList = null;

  private String c(String paramString)
  {
    if (paramString == null)
      paramString = "";
    return paramString;
  }

  public Seat getSeat(int paramInt)
  {
    if ((paramInt > this.mSeatList.size()) || (paramInt < 0))
      return new Seat();
    return this.mSeatList.get(paramInt);
  }

  public String getDesc()
  {
    return c(this.desc);
  }

  public void addSeat(Seat paramSeat)
  {
    this.mSeatList.add(paramSeat);
  }

  public void setRow(String paramString)
  {
    this.row = paramString;
  }

  public ArrayList getSeatList()
  {
    return this.mSeatList;
  }

  public void setSeatList(ArrayList<Seat> seat_list){
	  this.mSeatList = seat_list;
  }
  
  
  public void setDesc(String paramString)
  {
    this.desc = paramString;
  }
}