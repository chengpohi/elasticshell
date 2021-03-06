package com.github.chengpohi.dsl.eql

import com.github.chengpohi.dsl.converter.ResponseConverter
import com.github.chengpohi.dsl.serializer.ResponseSerializer
import org.elasticsearch.action.{
  ActionFuture,
  ActionListener,
  ListenableActionFuture
}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}

trait FutureOps extends ResponseSerializer with ResponseConverter {
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  implicit def buildFuture[A](f: ActionFuture[A]): Future[A] = Future {
    f.actionGet()
  }

  implicit def buildFuture[A](f: ListenableActionFuture[A]): Future[A] = {
    val p = Promise[A]()
    f.addListener(new ActionListener[A] {
      def onFailure(e: Exception): Unit = p.tryFailure(e)

      def onResponse(resp: A): Unit = p.trySuccess(resp)
    })
    p.future
  }

  trait FutureAwaitOps[A] {
    val value: Future[A]
    def await: A = Await.result(value, Duration.Inf)
  }

  implicit def convertToFutureAwaitOps[A](v: Future[A]): FutureAwaitOps[A] =
    new FutureAwaitOps[A] {
      override val value: Future[A] = v
    }

  trait FutureJsonOps[A] {
    val F0: JSONSerializer[A]
    val value: Future[A]

    def toJson: String = {
      val result = Await.result(value, Duration.Inf)
      F0.json(result)
    }
  }

  trait FutureConverterOps[A] {
    val F0: Converter[A]
    val value: Future[A]
    def as[T](implicit mf: Manifest[T]): Future[Stream[T]] =
      value.map(a => {
        F0.as(a)
      })
  }

  implicit def futureToJsonOps[A: JSONSerializer](a: Future[A])(
      implicit F: JSONSerializer[A]): FutureJsonOps[A] =
    new FutureJsonOps[A] {
      override val F0: JSONSerializer[A] = F
      override val value: Future[A] = a
    }

  implicit def futureToConverterOps[A: Converter](a: Future[A])(
      implicit F: Converter[A]): FutureConverterOps[A] =
    new FutureConverterOps[A] {
      override val F0: Converter[A] = F
      override val value: Future[A] = a
    }
}
