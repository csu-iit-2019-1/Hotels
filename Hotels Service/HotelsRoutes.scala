package com.example

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging

import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.delete
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.directives.PathDirectives.path

import scala.concurrent.Future
import com.example.HotelsActor._
import akka.pattern.ask
import akka.util.Timeout

trait HotelsRoutes extends JsonSupport {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[HotelsRoutes])

  def hotelsActor: ActorRef

  implicit lazy val timeout = Timeout(10.seconds)

  lazy val hotelsRoutes: Route =
    pathPrefix("hotels") {
      concat (
        pathPrefix("cities") {        //GET /hotels/cities/{cityName}
          concat(
            path(Segment) { cityName =>
              concat(
                get {
                  val hotels: Future[ShortInfAboutHotels] =
                    (hotelsActor ? GetAllHotelsByCity(cityName.toLowerCase)).mapTo[ShortInfAboutHotels]
                  complete(hotels)
                }
              )
            }
          )
        },
        pathPrefix(Segment) { date =>       //GET /hotels/{date}/{cityName}/{stars}
          concat(
            pathPrefix(Segment) { cityName =>
              concat(
                path(Segment) { stars =>
                  concat(
                    get {
                      val hotels: Future[ShortInfAboutHotels] =
                        (hotelsActor ? GetAvailableHotels(date, cityName, stars.toDouble)).mapTo[ShortInfAboutHotels]
                      complete(hotels)
                    }
                  )
                }
              )
            }
          )
        },
        path(Segment) { hotelId =>
          concat(
            get {                             //GET /hotels/{hotelId}
              val hotel: Future[Hotel] =
                (hotelsActor ? GetHotel(hotelId.toInt)).mapTo[Hotel]
              complete(hotel)
            },
            put {                             //PUT /hotels/{hotelId}
              entity(as[Hotel]) { hotel =>
                val valueCreated: Future[ActionPerformed] = (hotelsActor ? AddHotel(hotelId.toInt, hotel)).mapTo[ActionPerformed]
                onSuccess(valueCreated) { performed =>
                  log.info("Putted hotel [{}]: {}", hotelId, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            },
            delete {                           //DELETE /hotels/{hotelId}
              val hotelDeleted: Future[ActionPerformed] = (hotelsActor ? DeleteHotel(hotelId.toInt)).mapTo[ActionPerformed]
              onSuccess(hotelDeleted) { performed =>
                log.info("Deleted hotel [{}]: {}", hotelId, performed.description)
                complete((StatusCodes.OK, performed))
              }
            }
          )
        },
        pathPrefix("avgmincosts") {       //GET /hotels/averagemincost/{day}/{cityName}/{stars}
          concat(
            pathPrefix(Segment) { day =>
              concat(
                pathPrefix(Segment) { cityName =>
                  concat(
                    path(Segment) { stars =>
                      concat(
                        get {
                          val avgCost: Future[AverageMinCosts] =
                            (hotelsActor ? GetAverageMinCosts(day, cityName, stars.toDouble)).mapTo[AverageMinCosts]
                          complete(avgCost)
                        }
                      )
                    }
                  )
                }
              )
            }
          )
        },
        pathPrefix("booking") {       //POST /hotel/booking/{hotelId}
          concat(
            path(Segment) { hotelId =>
              concat(
                post {
                  entity(as[BookingDetails]) { bookingDetails =>
                    val createdBooking: Future[BookingResult] = (hotelsActor ? BookingHotel(hotelId.toInt, bookingDetails)).mapTo[BookingResult]
                    onSuccess(createdBooking) { createdBooking =>
                      log.info("Booked [{}]: {}", createdBooking.id, createdBooking.status)
                      complete(createdBooking)  //return bookingId and status
                    }
                  }
                }
              )
            }
          )
        },
        pathPrefix("buyout") {        //PUT /hotels/buyout
          concat(
            put {
              entity(as[Buyout]) { buyout =>
                val buyOutStatus: Future[ActionPerformed] = (hotelsActor ? BuyoutBooking(buyout.bookingId)).mapTo[ActionPerformed]
                onSuccess(buyOutStatus) { performed =>
                  log.info("Booking [{}]: {}", buyout.bookingId, performed.description)  //may be need to detail status
                  complete((StatusCodes.Created, performed))
                }
              }
            }
          )
        }
      )
    }

}
