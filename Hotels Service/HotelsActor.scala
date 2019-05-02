package com.example

import akka.actor.{Actor, ActorLogging, Props}

final case class Review(id: Int, hotelId: Int, date: String, author: String, content: String)
final case class PhotoUrl(id: Int, hotelId: Int, url: String)
final case class HotelsDetail(id: Int, hotelId: Int, description: String) //something else
final case class Hotel(name: String, cityId: Int, price: Double, stars: Double, breakfast: Boolean, seaNearby: Boolean, details: HotelsDetail, photoUrls: Seq[PhotoUrl], reviews: Seq[Review]) //reviews: Review*
final case class Hotels(hotels: Seq[Hotel])
final case class ShortInfAboutHotel(id: Int, name: String, cityId: Int, price: Double, stars: Double, mainPhotoUrl: String, breakfast: Boolean, seaNearby: Boolean)
final case class SearchingParams(date: String, cityId: Int, stars: Double, breakfast: Boolean, seaNearby: Boolean)
final case class ShortInfAboutHotels(shortInfAboutHotel: Seq[ShortInfAboutHotel])
final case class AverageMinCosts(average: Option[Double], min: Option[Double])
final case class BookingDetails(personId: Int, hotelId: Int, dateArrive: String, dateDeparture: String, countOfPersons: Int)
//final case class BookingResult(id: Option[Int], status: String, fullPrice: Double)
final case class BookingResult(BookingId: Option[Int])
final case class BuyoutDetails(BookingId: Int)
final case class BuyoutResult(status: Boolean)
//final case class BuyoutResult(status: Boolean)

object HotelsActor {
  final case class GetAllHotelsByCityId(cityId: Int)
  final case class GetAvailableHotels(date: String, cityId: Int, stars: Double) //date: DateTime
  final case class GetHotel(id: Int)
  final case class AddHotel(hotelId: Int, hotel: Hotel)
  final case class DeleteHotel(hotelId: Int)
  final case class GetAverageMinCosts(searchingParams: SearchingParams) //day: DateTime
  final case class GetCheapestHotel(searchingParams: SearchingParams)
  final case class BookingHotel(bookingDetails: BookingDetails)
  final case class BuyoutBooking(buyoutDetails: BuyoutDetails)
  final case class ActionPerformed(description: String)

  def props: Props = Props[HotelsActor]
}

class HotelsActor extends Actor with ActorLogging {
  import HotelsActor._


  def receive: Receive = {
    case GetAllHotelsByCityId(cityId) =>
      sender() ! SqliteDb.getShortInfAboutHotels(cityId)
    case GetAvailableHotels(date, cityId, stars) => //do not check the date
      sender() ! SqliteDb.getAvailableHotels(date, cityId, stars)
    case GetHotel(id) =>
      sender() ! SqliteDb.getHotel(id)
    case AddHotel(hotelId, hotel) =>
      sender() ! SqliteDb.addHotel(hotelId, hotel)
    case DeleteHotel(hotelId) =>
      sender() ! SqliteDb.deleteHotel(hotelId)
    case GetAverageMinCosts(searchingParams) =>
      sender() ! SqliteDb.getAverageMinCosts(searchingParams)
    case GetCheapestHotel(searchingParams) =>
      sender() ! SqliteDb.getCheapestHotel(searchingParams)
    case BookingHotel(bookingDetails) =>
      sender() ! SqliteDb.bookingHotel(bookingDetails)
    case BuyoutBooking(buyoutDetails) =>
      sender() ! SqliteDb.buyOut(buyoutDetails)
  }
}
