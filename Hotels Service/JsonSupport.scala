package com.example

import com.example.HotelsActor.ActionPerformed

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val hotelJsonFormat = jsonFormat9(Hotel)
  implicit val hotelsJsonFormat = jsonFormat1(Hotels)
  implicit val avgMinCostsJsonFormat = jsonFormat2(AverageMinCosts)
  implicit val bookingResultJsonFormat = jsonFormat2(BookingResult)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
