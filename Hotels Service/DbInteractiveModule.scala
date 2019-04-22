package com.example

trait DbInteractiveModule {
  def addHotel(hotel: Hotel): Unit
  def deleteHotel(hotelId: Int): Unit
  def getHotels: Hotels
  def getHotel: Hotel
}