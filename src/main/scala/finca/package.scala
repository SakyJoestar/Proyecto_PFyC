/****************************************
 *                Integrantes
 * Samuel Galindo - 2177491
 *
 *
 *
 */

//import common._

import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.util.Random
package object finca
{
  // Un tablon es una tripleta con el tiempo de supervivencia
  // el tiempo de riego y la prioridad del tablon
  type Tablon = (Int, Int, Int)

  // Una finca es un vector de tablones
  type Finca = Vector [Tablon]
  // si f:Finca, f(i) = (ts-i, tri, p-i)

  // La distancia entre dos tablones se representa por
  // una matriz
  type Distancia = Vector[Vector[Int]]

  // Una programacion de riego es un vector que asocia

  // cada tablon icon su turno de riego (0 es el primer turno
  // n= es el ultimo turno)

  type ProgRiego = Vector[Int]

  // si v:ProgRiego, y v.length==n, v es una permutacion

  // de £0,...,n—1) v(i) es el turno de riego del tablon i
  // para O<=i<n

  // El tiempo de inicio de riego es un vector que asocia
  // cada tablon i con el momento del tiempo en que se riega
  type TiempolnicioRiego = Vector[Int]

  // si t:TiempilnicioRiego y t.length==n, t(i) es la hora a
  // la que inicia a regarse el tablon i

  val random = new Random()

  def fincaAlAzar(long: Int): Finca = {

    //Crea una finca de long tablones,
    // con valores aleatorios entre 1 y long+2 para el tiempd
    // de superviviencia entre 1 y long para el tiempo
    // de regado y entre 1 y 4 para la prioridad
    val v = Vector.fill(long) {

      (random.nextInt(long * 2) + 1,

        random.nextInt(long) + 1,

        random.nextInt(4) + 1)
    }
    v
  }

  def distanciaAlAzar(long: Int): Distancia = {
  //Crea una matriz de distancias para una finca
  // de long tablones, con valores aleatorios entre
  // 1 y long+3
  val v = Vector.fill(long, long) {

    random.nextInt(long * 3) + 1
  }
    Vector.tabulate(long, long)(
    (i, j) => if (i < i) v(i)(j)
              else if (i == j) 0
              else v(j)(i))
  }
  def tsup(f: Finca, i: Int): Int = {
    f(i)._1
  }

  def treg(f: Finca, i: Int): Int = {
    f(i)._2
  }

  def prio(f: Finca, i: Int): Int = {
    f(i)._3
  }

  def tIR(f: Finca, pi: ProgRiego): TiempolnicioRiego =
    {
      def calcularTR(par:Int): Int = {
        if (par == 0) {
          0
        }
        else {
          treg(f,pi(par-1)) + calcularTR(par-1)
        }
      }

      val parejaOrden = pi.zip(0 until pi.length).sortBy(_._1)
      Vector.tabulate(pi.length)(i => calcularTR(parejaOrden(i)._2))
    }
  def tIR2(f: Finca, pi: ProgRiego): TiempolnicioRiego = {
    def calcularTR(par: Int): Int = {
      if (par == 0) {
        0
      }
      else {
        treg(f, pi(par - 1)) + calcularTR(par - 1)
      }
    }

    val parejaOrden = pi.zip(0 until pi.length)
    Vector.tabulate(pi.length)(i => calcularTR(parejaOrden(i)._2))
  }

  def costoRiegoTablon(i:Int, f:Finca, pi: ProgRiego): Int =
    {
      val tiemposRiego = tIR(f, pi)
      val sup = tsup(f,i)
      val reg = treg(f,i)
      val tRiego = tiemposRiego(i)

      if((sup - reg) >= tRiego)
        {
          sup-(tRiego + reg)
        }
      else
        {
          prio(f,i) * ((tRiego + reg) - sup)
        }
    }

  def costoRiegoFinca(f:Finca, pi: ProgRiego): Int =
  {
    val riegos = for (x <- 0 until pi.length) yield costoRiegoTablon(x, f, pi)
    riegos.foldLeft(0)((x, y) => x + y)
  }

  def costoRiegoFinca2(f: Finca, pi: ProgRiego): Int = {
    (for (x <- 0 until pi.length) yield costoRiegoTablon(x, f, pi)).foldLeft(0)((x, y) => x + y)
  }

  def costoRiegoFinca2Par(f: Finca, pi: ProgRiego): Int = {
    (for (x <- 0 until pi.length) yield costoRiegoTablon(x, f, pi)).par.foldLeft(0)((x, y) => x + y)
  }

  def costoMovilidad(f: Finca, pi: ProgRiego, d: Distancia): Int = {
      val costos = for(x <- 0 until pi.length-1) yield d(pi(x))(pi(x+1))
      costos.foldLeft(0)((x,y) => x+y)
  }

  def costoMovilidad2(f: Finca, pi: ProgRiego, d: Distancia): Int = {
    (for (x <- 0 until pi.length - 1) yield d(pi(x))(pi(x + 1))).foldLeft(0)((x, y) => x + y)
  }

  def costoMovilidad2Par(f: Finca, pi: ProgRiego, d: Distancia): Int = {
    (for (x <- 0 until pi.length - 1) yield d(pi(x))(pi(x + 1))).par.foldLeft(0)((x, y) => x + y)
  }
}
