package com.example

object TestingData {
  SqliteDb.createSchemas

  SqliteDb.initBooking

  SqliteDb.deleteHotel(0)
  SqliteDb.deleteHotel(1)
  SqliteDb.deleteHotel(2)
  SqliteDb.deleteHotel(3)
  SqliteDb.deleteHotel(4)
  SqliteDb.deleteHotel(5)
  SqliteDb.deleteHotel(6)
  SqliteDb.deleteHotel(7)
  SqliteDb.deleteHotel(8)
  SqliteDb.deleteHotel(9)
  SqliteDb.deleteHotel(10)
  SqliteDb.deleteHotel(11)


  SqliteDb.addHotel(0, Hotel("Coral Beach Resort Tiran", 1, 66600, 4.1, false, false, HotelsDetail(0, 0, "Worst hotel"), Array(PhotoUrl(0, 0,
    "https://s-ec.bstatic.com/images/hotel/max1024x768/147/147997361.jpg")), Seq(Review(0, 0, "2018-01-03", "Pharaoh", "sometext"))))
  SqliteDb.addHotel(1, Hotel("Try Me Beach", 1, 11100, 3.25, true, true, HotelsDetail(1, 1, "Death metal"), Array(PhotoUrl(1, 1,
    "http://premium.tophotels.ru/icache/reviewimage/9b01060a8dbf1572d024fbf83d230815/220_465x310.jpg")), Seq(Review(1, 1, "2099-11-14", "Rick Sunchez", "Wubbalubbadubdub!"))))
  SqliteDb.addHotel(2, Hotel("Hilton", 2, 22200, 4.5, false, false, HotelsDetail(2, 2, "Hotel hotel hotel hotel hotel hotel hotel hotel hotel"), Array(PhotoUrl(2, 2,
    "https://www.anapabest.ru/wp-content/uploads/2015/11/VityazevoMegaluxbasseinnochju.jpg")), Seq(Review(2, 2, "1876-04-05", "Scorpion", "Get over here!!!"))))
  SqliteDb.addHotel(3, Hotel("Sheraton", 2, 33300, 4.1, true, true, HotelsDetail(3, 3, "Hotel hotel hotel hotel hotel hotel hotel hotel hotel"), Array(PhotoUrl(3, 3,
    "https://img.hotels24.ua/photos/partner_hotel/hotel_main/93/9335/933567/Gostinica-Bistrica-Lyuks-Ivano-Frankovsk-snjat-933567z600.jpg")), Seq(Review(3, 3, "1876-04-05", "Scorpion", "Get over here!!!"))))
  SqliteDb.addHotel(4, Hotel("Westin", 3, 44400, 4.5, false, true, HotelsDetail(4, 4, "Hotel hotel hotel hotel hotel hotel hotel hotel hotel"), Array(PhotoUrl(4, 4,
    "https://www.santour.ru/Malta/images/hotels/CORINTHIA_SAN_GORG_HOTEL/CORINTHIA_SAN_GORG_HOTEL1.jpg")), Seq(Review(4, 4, "1876-04-05", "Scorpion", "Get over here!!!"))))
  SqliteDb.addHotel(5, Hotel("Four Seasons", 3, 55500, 4.2, true, true, HotelsDetail(5, 5, "Hotel hotel hotel hotel hotel hotel hotel hotel hotel"), Array(PhotoUrl(5, 5,
    "https://vestikavkaza.ru/upload/2017-07-24/15008909565975c74c488922.37288464.jpg")), Seq(Review(5, 5, "1876-04-05", "Scorpion", "Get over here!!!"))))
  SqliteDb.addHotel(6, Hotel("Ritz-Carlton", 4, 66600, 5, false, false, HotelsDetail(6, 6, "Hotel hotel hotel hotel hotel hotel hotel hotel hotel"), Array(PhotoUrl(6, 6,
    "https://www.santour.ru/fiji/images/hotels/FIJI_HIDEAWAY_RESORT_AND_SPA_HOTEL/FIJI_HIDEAWAY_RESORT_AND_SPA_HOTEL1.jpg")), Seq(Review(6, 6, "1876-04-05", "Scorpion", "Get over here!!!"))))
  SqliteDb.addHotel(7, Hotel("Hyatt", 4, 77700, 4.3, true, false, HotelsDetail(7, 7, "Hotel hotel hotel hotel hotel hotel hotel hotel hotel"), Array(PhotoUrl(7, 7,
    "https://t-ec.bstatic.com/images/hotel/max1024x768/388/38880764.jpg")), Seq(Review(7, 7, "1876-04-05", "Scorpion", "Get over here!!!"))))
  SqliteDb.addHotel(8, Hotel("Renaissance", 5, 88800, 3.5, false, true, HotelsDetail(8, 8, "Hotel hotel hotel hotel hotel hotel hotel hotel hotel"), Array(PhotoUrl(8, 8,
    "https://www.svoiludi.ru/images/tb/5649/pattaya-hotels-1270145977_w687h357.jpg")), Seq(Review(8, 8, "1876-04-05", "Scorpion", "Get over here!!!"))))
  SqliteDb.addHotel(9, Hotel("Embassy Suites", 5, 99900, 4, true, true, HotelsDetail(9, 9, "Hotel hotel hotel hotel hotel hotel hotel hotel hotel"), Array(PhotoUrl(9, 9,
    "https://t-ec.bstatic.com/images/hotel/max1024x768/218/21857479.jpg")), Seq(Review(9, 9, "1876-04-05", "Scorpion", "Get over here!!!"))))
  SqliteDb.addHotel(10, Hotel("InterContinental", 6, 100000, 4.5, false, false, HotelsDetail(10, 10, "Hotel hotel hotel hotel hotel hotel hotel hotel hotel"), Array(PhotoUrl(10, 10,
    "https://tophotels.ru/icache/hotel_photos/1/881/19748/1315958_740x550.jpg")), Seq(Review(10, 10, "1876-04-05", "Scorpion", "Get over here!!!"))))
  SqliteDb.addHotel(11, Hotel("Hyatt Regency", 6, 110000, 5, true, true, HotelsDetail(11, 11, "Hotel hotel hotel hotel hotel hotel hotel hotel hotel"), Array(PhotoUrl(11, 11,
    "http://www.hotelsempati.com/images/hotel-view.jpg")), Seq(Review(12, 12, "1876-04-05", "Scorpion", "Get over here!!!"))))

}
