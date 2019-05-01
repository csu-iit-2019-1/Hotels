package com.example

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer

object QuickstartServer extends App with HotelsRoutes {

  implicit val system: ActorSystem = ActorSystem("hotelsAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val hotelsActor: ActorRef = system.actorOf(HotelsActor.props, "hotelsActor")

  lazy val routes: Route = hotelsRoutes

  val port: Int = sys.env.getOrElse("PORT", "8080").toInt
  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "0.0.0.0", port)

  SqliteDb
  //TestingData

  serverBinding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  Await.result(system.whenTerminated, Duration.Inf)
}