package com.example

import slick.jdbc.SQLiteProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class DBHotels(tag: Tag) extends Table[(Int, String, String, Double, Double, String, Boolean, Boolean)](tag, "Hotels") {
  def id = column[Int]("hotelId", O.PrimaryKey)
  def name = column[String]("name")
  def city = column[String]("city")
  def price = column[Double]("price")
  def stars = column[Double]("stars")
  def mainPhotoUrl = column[String]("mainPhotoUrl")
  def breakfast = column[Boolean]("breakfast")                    // added for searching function
  def seaIsNear = column[Boolean]("seaIsNear") // < 100 meters :D // added for searching function
  def * = (id, name, city, price, stars, mainPhotoUrl, breakfast, seaIsNear)
}

class DBHotelDetail(tag: Tag) extends Table[(Int, Int, String)](tag, "HotelsDetail") {
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


class DBBooking(tag: Tag) extends Table[(Int, Int, Int, Int, String, String, String)](tag, "Booking") {
  def id = column[Int]("bookingId", O.PrimaryKey)
  def personId = column[Int]("personId")
  def hotelId = column[Int]("hotelId")
  def apartamentId = column[Int]("apartamentId")
  def dateDeparture = column[String]("dateDeparture") //need mapper from DateTime
  def dateArrive = column[String]("dateArrive") //need mapper from DateTime
  def status = column[String]("status")   // "expectation" to "success" or "canceled" or "error"
  def * = (id, personId, hotelId, apartamentId, dateDeparture, dateArrive, status)
}


object SqliteDb extends DbInteractiveModule {
  val dbHotels = TableQuery[DBHotels]
  val dBHotelDetail = TableQuery[DBHotelDetail]
  val dbRevievs = TableQuery[DBRevievs]
  val dbPhotoUrls = TableQuery[DBPhotoUrls]
  val dbBooking = TableQuery[DBBooking]
  val dbHotelsDetail = TableQuery[DBHotelDetail]

  val db = Database.forConfig("hotels")

  db.run(DBIO.seq(dbHotels.schema.create))
  db.run(DBIO.seq(dBHotelDetail.schema.create))
  db.run(DBIO.seq(dbRevievs.schema.create))
  db.run(DBIO.seq(dbPhotoUrls.schema.create))
  db.run(DBIO.seq(dbBooking.schema.create))
  db.run(DBIO.seq(dbHotelsDetail.schema.create))

//  deleteHotel(0)
//  deleteHotel(1)
//  deleteHotel(2)
//  <for testing>
//  addHotel(0, new Hotel("Coral Beach Resort Tiran", "Cairo", 66600, 0.1, false,false, HotelDetail(0, 0, "Worst hotel"), Array(new PhotoUrl(0, 0,
//    "https://s-ec.bstatic.com/images/hotel/max1024x768/147/147997361.jpg")), Seq(new Review(0, 0, "", "", "")))) // it's bad
//  addHotel(1, new Hotel("11111111", "Cairo", 11111, 4.5, false, false, new HotelDetail(1, 1, "11111111"), Array(new PhotoUrl(1, 1,
//    "111111111111111111.jpg")), Seq(new Review(1, 1, "", "", "")))) // it's bad
//  addHotel(2, new Hotel("Rixwell Elefant Hotel", "Riga", 777000, 9.9, true, true, HotelDetail(2, 2, "Best hotel"), Array(new PhotoUrl(2, 2,
//    "https://momblogsociety.com/wp-content/uploads/2019/03/hotels.jpg")), Seq(new Review(2, 2, "", "", "")))) // it's bad

  def addHotel(hotelId: Int, hotel: Hotel): Unit = {
    db.run(DBIO.seq(dbHotels forceInsertExpr (hotelId, hotel.name, hotel.city.toLowerCase, hotel.price, hotel.stars, hotel.photoUrls(0).url, hotel.breakfast, hotel.seaIsNear)))
      //  .onComplete( _ => db.stream((for (hotel <- dbHotels) yield hotel.name).result).foreach(println))

    hotel.photoUrls.foreach( photoUrl => {db.run(DBIO.seq(dbPhotoUrls += (photoUrl.id, hotelId, photoUrl.url)))})
    hotel.reviews.foreach( review => {db.run(DBIO.seq(dbRevievs += (review.id, hotelId, review.date.toString, review.author, review.content)))})
    db.run(DBIO.seq(dbHotelsDetail += (hotel.details.id, hotel.details.hotelId, hotel.details.description)))
  }

  def deleteHotel(hotelId: Int): Unit = db.run(dbHotels.filter(_.id === hotelId).delete)

  def getShortInfAboutHotels(city: String): ShortInfAboutHotels = ShortInfAboutHotels(Await.result(db.run(dbHotels.filter(_.city === city).result)
    .map(_.map(ShortInfAboutHotel tupled _)), Duration.Inf))

  def getHotel(hotelId: Int): Hotel = {
    val shortInf = Await.result(db.run(dbHotels.filter(_.id === hotelId).result.head), Duration.Inf)
    val photoUrls = Await.result(db.run(dbPhotoUrls.filter(_.hotelId === hotelId).result).map(_.map(PhotoUrl tupled _)), Duration.Inf)
    val reviews = Await.result(db.run(dbRevievs.filter(_.hotelId === hotelId).result).map(_.map(Review tupled _)), Duration.Inf)
    val hotelDetail = Await.result(db.run(dbHotelsDetail.filter(_.hotelId === hotelId).result.head).map(HotelDetail tupled _), Duration.Inf)

    Hotel(shortInf._2, shortInf._3, shortInf._4, shortInf._5, shortInf._7, shortInf._8, hotelDetail, photoUrls, reviews)
  }

  def getAvailableHotels(date: String, city: String, stars: Double): ShortInfAboutHotels = ShortInfAboutHotels(Await.result(db.run(dbHotels.filter(_.city === city)
    .filter(_.stars >= stars).result).map(_.map(ShortInfAboutHotel tupled _)), Duration.Inf))

  def getAverageMinCosts(date: String, city: String, stars: Double): AverageMinCosts = {
    AverageMinCosts(Await.result(db.run(dbHotels.filter(_.city === city).filter(_.stars >= stars).map(_.price).min.result), Duration.Inf),
      Await.result(db.run(dbHotels.filter(_.city === city).filter(_.stars >= stars).map(_.price).avg.result), Duration.Inf))
  }

}