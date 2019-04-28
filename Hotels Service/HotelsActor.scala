package com.example

import akka.actor.{Actor, ActorLogging, Props}

final case class Review(id: Int, hotelId: Int, date: String, author: String, content: String)
final case class PhotoUrl(id: Int, hotelId: Int, url: String)
final case class HotelDetail(id: Int, hotelId: Int, description: String) //something else
final case class Hotel(name: String, city: String, price: Double, stars: Double, breakfast: Boolean, seaNearby: Boolean, details: HotelDetail, photoUrls: Seq[PhotoUrl], reviews: Seq[Review]) //reviews: Review*
final case class Hotels(hotels: Seq[Hotel])
final case class ShortInfAboutHotel(id: Int, name: String, city: String, price: Double, stars: Double, mainPhotoUrl: String, breakfast: Boolean, seaNearby: Boolean)
final case class SearchingParams(date: String, city: String, stars: Double, breakfast: Boolean, seaNearby: Boolean)
final case class ShortInfAboutHotels(shortInfAboutHotel: Seq[ShortInfAboutHotel])
final case class AverageMinCosts(average: Option[Double], min: Option[Double])
final case class BookingDetails(personId: Int, hotelId: Int, dateArrive: String, dateDeparture: String, countOfPersons: Int)
final case class BookingResult(id: Option[Int], status: String, fullPrice: Double)
final case class BuyoutDetails(bookingId: Int)
final case class BuyoutResult(status: String)

object HotelsActor {
  final case class GetAllHotelsByCity(city: String)
  final case class GetAvailableHotels(date: String, cityName: String, stars: Double) //date: DateTime
  final case class GetHotel(id: Int)
  final case class AddHotel(hotelId: Int, hotel: Hotel)
  final case class DeleteHotel(hotelId: Int)
  final case class GetAverageMinCosts(day: String, cityName: String, stars: Double) //day: DateTime
  final case class GetCheapestHotel(searchingParams: SearchingParams)
  final case class BookingHotel(bookingDetails: BookingDetails)
  final case class BuyoutBooking(buyoutDetails: BuyoutDetails)
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
    case GetCheapestHotel(searchingParams) =>
      sender() ! SqliteDb.getCheapestHotel(searchingParams)
    case BookingHotel(bookingDetails) =>
      sender() ! SqliteDb.bookingHotel(bookingDetails)
    case BuyoutBooking(buyoutDetails) =>
      sender() ! SqliteDb.buyOut(buyoutDetails)
  }
}