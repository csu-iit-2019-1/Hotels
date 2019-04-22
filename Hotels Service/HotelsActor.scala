package com.example

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.DateTime

final case class Apartament(number: Int, hotelId: Int, occupied: Boolean)
final case class Review(id: Int, hotelId: Int, date: DateTime, author: String, content: String)
final case class PhotoUrl(id: Int, hotelId: Int, url: String)
final case class Hotel(id: Int, name: String, city: String, apartaments: Array[Apartament], price: Double, description: String, raiting: Double, photoUrls: Array[PhotoUrl], reviews: Review*)
final case class Hotels(hotels: Hotel*)
final case class AverageMinCosts(average: Double, min: Double)
final case class BookingDetails(hotelId: Int, personId: Int, dateDeparture: DateTime, dateArrive: DateTime, countOfPersons: Int)
final case class BookingResult(id: Int, status: String)

object HotelsActor {
  final case object GetAllHotels
  final case class GetAvailableHotels(date: DateTime, cityId: Int, stars: Int)
  final case class GetHotel(id: Int)
  final case class PutHotel(hotelId: Int, hotel: Hotel)
  final case class DeleteHotel(hotelId: Int)
  final case class GetAverageMinCosts(day: DateTime, cityId: Int, stars: Int)
  final case class BookingHotel(hotelId: Int, bookingDetails: BookingDetails)
  final case class BuyoutBooking(bookingId: Int)
  final case class ActionPerformed(description: String)

  def props: Props = Props[HotelsActor]
}

class HotelsActor extends Actor with ActorLogging {
  import HotelsActor._

  var users = Set.empty[Hotel]

  def receive: Receive = {
    case GetAllHotels =>
      sender() ! //
    case GetAvailableHotels(date, cityId, stars) =>
      sender() ! //
    case GetHotel(id) =>
      sender() ! //
    case PutHotel(hotelId, hotel) =>
      sender() ! //
    case DeleteHotel(hotelId) =>
      sender() ! //
    case GetAverageMinCosts(day, cityId, stars) =>
      sender() ! //
    case BookingHotel(hotelId, bookingDetails) =>
      sender() ! //
    case BuyoutBooking(bookingId) =>
      sender() ! //
  }
}