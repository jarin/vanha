import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

/**
  * Created by jarin on 27/06/16.
  */


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
  val system = ActorSystem.create("smurfActors")
  val pingProps = Props[PingActor]
  val pongProps = Props[PongActor]
  val pinger = system.actorOf(pingProps, "ping")
  val ponger = system.actorOf(pongProps, "pong")
  ponger ! "ping"


}
