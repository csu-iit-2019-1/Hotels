package com.example

import slick.jdbc.SQLiteProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class DBHotels(tag: Tag) extends Table[(Int, String, Int, Double, Double, String, Boolean, Boolean)](tag, "Hotels") {
  def id = column[Int]("hotelId", O.PrimaryKey)
  def name = column[String]("name")
  def cityId = column[Int]("cityId")
  def price = column[Double]("price")
  def stars = column[Double]("stars")
  def mainPhotoUrl = column[String]("mainPhotoUrl")
  def breakfast = column[Boolean]("breakfast")                    // added for searching function
  def seaNearby = column[Boolean]("seaNearby") // < 100 meters :D // added for searching function
  def * = (id, name, cityId, price, stars, mainPhotoUrl, breakfast, seaNearby)
}

class DBHotelsDetail(tag: Tag) extends Table[(Int, Int, String)](tag, "HotelsDetail") {
  def id = column[Int]("detailsId", O.PrimaryKey)
  def hotelId = column[Int]("hotelId")
  def description = column[String]("desription")
  def * = (id, hotelId, description)
  // something else
}

class DBRevievs(tag: Tag) extends Table[(Int, Int, String, String, String)](tag, "Reviews") {
  def id = column[Int]("reviewId", O.PrimaryKey)
  def hotelId = column[Int]("hotelId")
  def date = column[String]("date") //need mapper from DateTime
  def author = column[String]("author")
  def content = column[String]("content")
  def * = (id, hotelId, date, author, content)
}

class DBPhotoUrls(tag: Tag) extends Table[(Int, Int, String)](tag, "PhotoUrls") {
  def id = column[Int]("photoId", O.PrimaryKey)
  def hotelId = column[Int]("hotelId")
  def url = column[String]("url")
  def * = (id, hotelId, url)
}


class DBBooking(tag: Tag) extends Table[(Int, Int, Int, String, String, Int, Double, String)](tag, "Booking") {
  def id = column[Int]("bookingId", O.PrimaryKey)
  def personId = column[Int]("personId")
  def hotelId = column[Int]("hotelId")
  def dateDeparture = column[String]("dateDeparture") //need mapper from DateTime
  def dateArrive = column[String]("dateArrive") //need mapper from DateTime
  def countOfPersons = column[Int]("countOfPersons")
  def fullPrice = column[Double]("fullPrice")
  def status = column[String]("status")   // "expectation" to "success" or "canceled" or "error"
  def * = (id, personId, hotelId, dateDeparture, dateArrive, countOfPersons, fullPrice, status)
}


object SqliteDb extends DbInteractiveModule {
  val dbHotels = TableQuery[DBHotels]
  val dbRevievs = TableQuery[DBRevievs]
  val dbPhotoUrls = TableQuery[DBPhotoUrls]
  val dbBooking = TableQuery[DBBooking]
  val dbHotelsDetail = TableQuery[DBHotelsDetail]

  val db = Database.forConfig("hotels")

  db.run(DBIO.seq(dbHotels.schema.create))
  db.run(DBIO.seq(dbHotelsDetail.schema.create))
  db.run(DBIO.seq(dbRevievs.schema.create))
  db.run(DBIO.seq(dbPhotoUrls.schema.create))
  db.run(DBIO.seq(dbBooking.schema.create))

//  deleteHotel(0)
//  deleteHotel(1)
//  deleteHotel(2)
//  <for testing>
//  addHotel(0, Hotel("Coral Beach Resort Tiran", 1, 55500, 0.1, false, false, HotelsDetail(0, 0, "Worst hotel"), Array(PhotoUrl(0, 0,
//    "https://s-ec.bstatic.com/images/hotel/max1024x768/147/147997361.jpg")), Seq(Review(0, 0, "2018-01-03", "Pharaoh", "sometext")))) // it's bad
//  addHotel(1, Hotel("Try Me Beach", 1, 66600, 4.5, false, false, HotelsDetail(1, 1, "Death metal"), Array(PhotoUrl(1, 1,
//    "https://fakepicturehotel1423.jpg")), Seq(Review(1, 1, "2099-11-14", "Rick Sunchez", "Wubbalubbadubdub!")))) // it's bad
//  addHotel(2, Hotel("Rixwell Elefant Hotel", 2, 777000, 5, true, true, HotelsDetail(2, 2, "Best hotel"), Array(PhotoUrl(2, 2,
//    "https://momblogsociety.com/wp-content/uploads/2019/03/hotels.jpg")), Seq(Review(2, 2, "1876-04-05", "Scorpion", "Get over here!!!")))) // it's bad
//  db.run(DBIO.seq(dbBooking forceInsertExpr (0, 0, 0, "", "", 0, 0.0, "")))

  def addHotel(hotelId: Int, hotel: Hotel): Unit = {
    db.run(DBIO.seq(dbHotels forceInsertExpr (hotelId, hotel.name, hotel.cityId, hotel.price, hotel.stars, hotel.photoUrls.head.url, hotel.breakfast, hotel.seaNearby)))
    //    .onComplete( _ => db.stream((for (hotel <- dbHotels) yield hotel.name).result).foreach(println))

    hotel.photoUrls.foreach( photoUrl => {db.run(DBIO.seq(dbPhotoUrls += (photoUrl.id, hotelId, photoUrl.url)))})
    hotel.reviews.foreach( review => {db.run(DBIO.seq(dbRevievs += (review.id, hotelId, review.date.toString, review.author, review.content)))})
    Await.result(db.run(DBIO.seq(dbHotelsDetail += (hotel.details.id, hotel.details.hotelId, hotel.details.description))), Duration.Inf)
  }

  def deleteHotel(hotelId: Int): Unit = db.run(dbHotels.filter(_.id === hotelId).delete)

  def getShortInfAboutHotels(cityId: Int): ShortInfAboutHotels = ShortInfAboutHotels(Await.result(db.run(dbHotels.filter(_.cityId === cityId).result)
    .map(_.map(ShortInfAboutHotel tupled _)), Duration.Inf))

  def getHotel(hotelId: Int): Hotel = {
    val shortInf = Await.result(db.run(dbHotels.filter(_.id === hotelId).result.head), Duration.Inf)
    val photoUrls = Await.result(db.run(dbPhotoUrls.filter(_.hotelId === hotelId).result).map(_.map(PhotoUrl tupled _)), Duration.Inf)
    val reviews = Await.result(db.run(dbRevievs.filter(_.hotelId === hotelId).result).map(_.map(Review tupled _)), Duration.Inf)
    val hotelDetail = Await.result(db.run(dbHotelsDetail.filter(_.hotelId === hotelId).result.head).map(HotelsDetail tupled _), Duration.Inf)

    Hotel(shortInf._2, shortInf._3, shortInf._4, shortInf._5, shortInf._7, shortInf._8, hotelDetail, photoUrls, reviews)
  }

  def getAvailableHotels(date: String, cityId: Int, stars: Double): ShortInfAboutHotels = ShortInfAboutHotels(Await.result(db.run(dbHotels.filter(_.cityId === cityId)
    .filter(_.stars >= stars).result).map(_.map(ShortInfAboutHotel tupled _)), Duration.Inf))

  def getAverageMinCosts(date: String, cityId: Int, stars: Double): AverageMinCosts = {
    AverageMinCosts(Await.result(db.run(dbHotels.filter(_.cityId === cityId).filter(_.stars >= stars).map(_.price).avg.result), Duration.Inf),
      Await.result(db.run(dbHotels.filter(_.cityId === cityId).filter(_.stars >= stars).map(_.price).min.result), Duration.Inf))
  }

  def getCheapestHotel(searchingParams: SearchingParams): ShortInfAboutHotels = {
    ShortInfAboutHotels(Await.result(db.run(dbHotels.filter(_.cityId === searchingParams.cityId).filter(_.stars >= searchingParams.stars)
      .filter(_.breakfast === searchingParams.breakfast)
      .filter(_.seaNearby === searchingParams.seaNearby)
      .filter(_.price === getAverageMinCosts(searchingParams.date, searchingParams.cityId, searchingParams.stars).min).result)
      .map(_.map(ShortInfAboutHotel tupled _)), Duration.Inf))
  }

  def bookingHotel(bookingDetails: BookingDetails): BookingResult = {  //returned bookingId, status and fullPrice to buyout
    val fullPrice = Await.result(db.run(dbHotels.filter(_.id === bookingDetails.hotelId).map(_.price).result.head), Duration.Inf) * bookingDetails.countOfPersons
    Await.result(db.run(DBIO.seq(dbBooking forceInsertExpr (
      Await.result(db.run(dbBooking.map(_.id).max.result), Duration.Inf).get + 1, bookingDetails.personId, bookingDetails.hotelId,
        bookingDetails.dateArrive, bookingDetails.dateDeparture, bookingDetails.countOfPersons, fullPrice, "Booked")))
      , Duration.Inf)

    BookingResult(Await.result(db.run(dbBooking.map(_.id).max.result), Duration.Inf), "Booked! Take your booking id", fullPrice)
  }

  def buyOut(buyoutDetails: BuyoutDetails): BuyoutResult = {
    Await.result(db.run(dbBooking.filter(_.id === buyoutDetails.bookingId).map(_.status).update("Buyouted")), Duration.Inf) //without check money
    BuyoutResult("Buyouted")
  }

}