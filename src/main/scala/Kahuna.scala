import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.Future

/**
  * Created by jarin on 27/06/16.
  */

class SmurfConsumer {
  import akka.http.scaladsl.model._
  import akka.http.scaladsl.model.HttpMethods._
  import akka.http.scaladsl.Http
  implicit val actorsystem = ActorSystem()
  implicit val materializer =  ActorMaterializer()
  implicit val ec =actorsystem.dispatcher
  val serverSource = Http().bind(interface= "localhost",port=8080)

  val requestHandler: HttpRequest => HttpResponse =  {
    case HttpRequest(GET,Uri.Path("/"),_,_,_) =>
      HttpResponse(entity = HttpEntity (
        ContentTypes.`text/html(UTF-8)`,"<html><body>Yo!</body></html>"))

    case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
      HttpResponse(entity = "PONG!")

    case HttpRequest(GET, Uri.Path("/crash"), _, _, _) =>
      sys.error("BOOM!")

    case _: HttpRequest =>
      HttpResponse(404, entity = "Unknown resource!")
  }

  val bindingFuture: Future[Http.ServerBinding] =
    serverSource.to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)

      connection handleWithSyncHandler requestHandler
      // this is equivalent to
      // connection handleWith { Flow[HttpRequest] map requestHandler }
    }).run()
}

class PingActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case "pong" => log.info("Got Pong!")
  }
}

class PongActor extends Actor with ActorLogging {

  override def receive:Receive = {
  case "ping" => log.info("Got Ping!")
    context.actorSelection("../ping") ! "pong"
  case _ => log.info("WTF?")
}

}


object Kahuna extends App {
//  val system = ActorSystem.create("smurfActors")
//  val pingProps = Props[PingActor]
//  val pongProps = Props[PongActor]
//  val pinger = system.actorOf(pingProps, "ping")
//  val ponger = system.actorOf(pongProps, "pong")
//  ponger ! "ping"
  println("Smurf:!")
  new SmurfConsumer()


}
