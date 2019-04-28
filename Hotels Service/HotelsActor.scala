package com.example

import akka.actor.{Actor, ActorLogging, Props}

final case class Review(id: Int, hotelId: Int, date: String, author: String, content: String)
final case class PhotoUrl(id: Int, hotelId: Int, url: String)
final case class HotelDetail(id: Int, hotelId: Int, description: String) //something else
final case class Hotel(name: String, city: String, price: Double, stars: Double, breakfast: Boolean, seaIsNear: Boolean, details: HotelDetail, photoUrls: Seq[PhotoUrl], reviews: Seq[Review]) //reviews: Review*
final case class Hotels(hotels: Seq[Hotel])
final case class ShortInfAboutHotel(id: Int, name: String, city: String, price: Double, stars: Double, mainPhotoUrl: String, breakfast: Boolean, seaIsNear: Boolean)
final case class ShortInfAboutHotels(shortInfAboutHotel: Seq[ShortInfAboutHotel])
final case class AverageMinCosts(average: Option[Double], min: Option[Double])
final case class BookingDetails(hotelId: Int, personId: Int, dateDeparture: String, dateArrive: String, countOfPersons: Int)
final case class BookingResult(id: Int, status: String)
final case class Buyout(bookingId: Int)

object HotelsActor {
  //final case object GetAllHotels
  final case class GetAllHotelsByCity(city: String)
  final case class GetAvailableHotels(date: String, cityName: String, stars: Double) //date: DateTime
  final case class GetHotel(id: Int)
  final case class AddHotel(hotelId: Int, hotel: Hotel)
  final case class DeleteHotel(hotelId: Int)
  final case class GetAverageMinCosts(day: String, cityName: String, stars: Double) //day: DateTime
  final case class BookingHotel(hotelId: Int, bookingDetails: BookingDetails)
  final case class BuyoutBooking(bookingId: Int)
  final case class ActionPerformed(description: String)

  def props: Props = Props[HotelsActor]
}

class HotelsActor extends Actor with ActorLogging {
  import HotelsActor._


  def receive: Receive = {
    case GetAllHotelsByCity(city) =>
      sender() ! SqliteDb.getShortInfAboutHotels(city)
    case GetAvailableHotels(date, cityName, stars) => //do not check the date
      sender() ! SqliteDb.getAvailableHotels(date, cityName, stars)
    case GetHotel(id) =>
      sender() ! SqliteDb.getHotel(id)
    case AddHotel(hotelId, hotel) =>
      sender() ! SqliteDb.addHotel(hotelId, hotel)
    case DeleteHotel(hotelId) =>
      sender() ! SqliteDb.deleteHotel(hotelId)
    case GetAverageMinCosts(date, cityName, stars) =>
      sender() ! SqliteDb.getAverageMinCosts(date, cityName, stars)
//    case BookingHotel(hotelId, bookingDetails) =>
//      sender() ! //
//    case BuyoutBooking(bookingId) =>
//      sender() ! //
  }
}