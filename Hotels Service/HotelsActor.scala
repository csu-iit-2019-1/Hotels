package com.example

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.DateTime

final case class Apartament(number: Int, hotelId: Int, occupied: Boolean)
final case class Review(id: Int, hotelId: Int, date: String, author: String, content: String) //date: DateTime
final case class PhotoUrl(id: Int, hotelId: Int, url: String)
//final case class Hotel(id: Int, name: String, city: String, apartaments: Array[Apartament], price: Double, description: String, raiting: Double, photoUrls: Array[PhotoUrl], reviews: Review*)
final case class Hotel(name: String, city: String, price: Double, description: String, raiting: Double, photoUrls: Array[PhotoUrl], reviews: Seq[Review]) //reviews: Review*
final case class Hotels(hotels: Seq[Hotel])
final case class ShortInfAboutHotel(id: Int, name: String, city: String, price: Double, raiting: Double, photoUrl: String)
final case class ShortInfAboutHotels(shortInfAboutHotel: Seq[ShortInfAboutHotel])
final case class AverageMinCosts(average: Double, min: Double)
final case class BookingDetails(hotelId: Int, personId: Int, dateDeparture: String, dateArrive: String, countOfPersons: Int) //dateDeparture: DateTime, dateArrive: DateTime
final case class BookingResult(id: Int, status: String)
final case class Buyout(bookingId: Int)

object HotelsActor {
  final case object GetAllHotels
  final case class GetAvailableHotels(date: String, cityId: Int, stars: Int) //date: DateTime
  final case class GetHotel(id: Int)
  final case class PutHotel(hotelId: Int, hotel: Hotel)
  final case class DeleteHotel(hotelId: Int)
  final case class GetAverageMinCosts(day: String, cityId: Int, stars: Int) //day: DateTime
  final case class BookingHotel(hotelId: Int, bookingDetails: BookingDetails)
  final case class BuyoutBooking(bookingId: Int)
  final case class ActionPerformed(description: String)

  def props: Props = Props[HotelsActor]
}

class HotelsActor extends Actor with ActorLogging {
  import HotelsActor._


  def receive: Receive = {
    case GetAllHotels =>
      sender() ! SqliteDb.getShortInfAboutHotels()
//    case GetAvailableHotels(date, cityId, stars) =>
//      sender() ! //
    case GetHotel(id) =>
      sender() ! SqliteDb.getHotel(id)
    case PutHotel(hotelId, hotel) =>
      sender() ! SqliteDb.addHotel(hotelId, hotel)
    case DeleteHotel(hotelId) =>
      sender() ! SqliteDb.deleteHotel(hotelId)
//    case GetAverageMinCosts(day, cityId, stars) =>
//      sender() ! //
//    case BookingHotel(hotelId, bookingDetails) =>
//      sender() ! //
//    case BuyoutBooking(bookingId) =>
//      sender() ! //
  }
}