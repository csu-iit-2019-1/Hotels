package com.example

import com.example.HotelsActor.ActionPerformed
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
//import akka.http.scaladsl.model.DateTime
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

//  implicit val apartamentJsonFormat = jsonFormat3(Apartament)
  implicit val reviewJsonFormat = jsonFormat5(Review)
  implicit val photoUrlJsonFormat = jsonFormat3(PhotoUrl)
  implicit val hotelDetailJsonFormat = jsonFormat3(HotelDetail)
  implicit val hotelJsonFormat = jsonFormat9(Hotel)
  implicit val hotelsJsonFormat = jsonFormat1(Hotels)
  implicit val shortInfAboutHotelJsonFormat = jsonFormat8(ShortInfAboutHotel)
  implicit val shortInfAboutHotelsJsonFormat = jsonFormat1(ShortInfAboutHotels)
  implicit val averageMinCostsJsonFormat = jsonFormat2(AverageMinCosts)
  implicit val bookingDetailsJsonFormat = jsonFormat5(BookingDetails)
  implicit val bookingResultJsonFormat = jsonFormat2(BookingResult)
  implicit val buyoutJsonFormat = jsonFormat1(Buyout)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
