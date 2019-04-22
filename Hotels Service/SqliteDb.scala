package com.example


import com.example.SqliteDb.hotels
import slick.jdbc.SQLiteProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global


class DBHotels(tag: Tag) extends Table[(Int, String, String, Double, String, Double)](tag, "Hotels") {
  def id = column[Int]("hotelId", O.PrimaryKey)
  def name = column[String]("name")
  def city = column[String]("city")
  def price = column[Double]("price")
  def description = column[String]("description")
  def raiting = column[Double]("raiting")
  def * = (id, name, city, price, description, raiting)
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

class DBApartaments(tag: Tag) extends Table[(Int, Int, Boolean)](tag, "Apartaments") {  // may be added some fields
  def id = column[Int]("apartamentId", O.PrimaryKey)
  def hotelId = column[Int]("hotelId")
  def occupied = column[Boolean]("occupied")
  def * = (id, hotelId, occupied)
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
  val hotels = TableQuery[DBHotels]
  val apartaments = TableQuery[DBApartaments]
  val revievs = TableQuery[DBRevievs]
  val photoUrls = TableQuery[DBPhotoUrls]
  val booking = TableQuery[DBBooking]

  val db = Database.forConfig("hotels")

  try {
    val setup = DBIO.seq(
      (hotels.schema).create
    )

    val setupFuture = db.run(setup)
    println("Done")

    val insert = DBIO.seq(
      hotels += (0, "Coral Beach Resort Tiran", "Egypt", 66600, "Worst hotel", 0.1)
    )
  }

  def addHotel(hotel: Hotel): Unit = {
    try {
      val insertHotel = DBIO.seq(
        hotels += (hotel.id, hotel.name, hotel.city, hotel.price, hotel.description, hotel.raiting)
      )
      db.run(insertHotel)

      for (apartament <- hotel.apartaments) {
        val insertApartament = DBIO.seq(
          apartaments += (apartament.number, apartament.hotelId, apartament.occupied)
        )
        db.run(insertHotel)
      }

      for (photoUrl <- hotel.photoUrls) {
        val insertPhotoUrls = DBIO.seq(
          photoUrls += (photoUrl.id, photoUrl.hotelId, photoUrl.url)
        )
        db.run(insertPhotoUrls)
      }

      for (review <- hotel.reviews) {
        val insertPhotoUrls = DBIO.seq(
          revievs += (review.id, review.hotelId, review.date.toString, review.author, review.content)
        )
        db.run(insertPhotoUrls)
      }

      //val insertFuture = db.run(insert)
      db.run(insertHotel)
    } finally db.close()

  }

  def deleteHotel(hotelId: Int): Unit = {
    val queryDelete = hotels.filter(_.id === hotelId)
    val action = queryDelete.delete
    db.run(action)
  }

  def getHotels(): Hotels = {
    db.run(hotels.result) // need to marhal
  }

  def getHotel(hotelId: Int): Hotel = {
    db.run(hotels.filter(_.id === hotelId).result.headOption) // need to marhal
  }

}