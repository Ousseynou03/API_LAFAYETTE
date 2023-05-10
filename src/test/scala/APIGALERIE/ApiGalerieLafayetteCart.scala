package APIGALERIE


import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._


import scala.language.postfixOps

class ApiGalerieLafayetteCart extends Simulation{

  private val host: String = System.getProperty("urlCible", " https://sapapi-int.galerieslafayette.com/occ/v2")
  private val VersionAppli: String = System.getProperty("VersionApp", "Vxx.xx.xx")
  private val TpsMonteEnCharge: Int = System.getProperty("tpsMonte", "15").toInt
  private val TpsPalier: Int = System.getProperty("tpsPalier", (2 * TpsMonteEnCharge).toString).toInt
  private val TpsPause: Int = System.getProperty("tpsPause", "60").toInt
  private val DureeMax: Int = System.getProperty("dureeMax", "1").toInt + 5 * (TpsMonteEnCharge + TpsPalier)

  private val tpsPaceDefault: Int = System.getProperty("tpsPace", "1000").toInt
  private val tpsPacingProducts: Int = System.getProperty("tpsPaceProducts", tpsPaceDefault.toString).toInt

  private val LeCoeff: Int = System.getProperty("coeff", "10").toInt
  private val  nbVu : Int =  LeCoeff * 1






  val httpProtocol =   http
    .baseUrl(host)
    .acceptHeader("application/json")


  before {

    println("----------------------------------------------" )
    println("host :"+ host   )
    println("VersionAppli :"+ VersionAppli   )
    println("TpsPause : " + TpsPause  )
    println("LeCoeff : " + LeCoeff  )
    println("nbVu : " + nbVu  )
    println("tpsMonte : " + TpsMonteEnCharge )
    println("----------------------------------------------" )
  }

  after  {
    println("----------------------------------------------" )
    println("--------     Rappel - Rappel - Rappel    -----" )
    println("VersionAppli :"+ VersionAppli   )
    println("host :"+ host   )
    println("TpsPause : " + TpsPause  )
    println("LeCoeff : " + LeCoeff  )
    println("nbVu : " + nbVu  )
    println("DureeMax : " + DureeMax )
    println("tpsMonte : " + TpsMonteEnCharge )
    println("--------     Rappel - Rappel - Rappel    -----" )
    println("----------------------------------------------" )
    println(" " )
  }

  val scnGetCart = scenario("Obtenir le panier").exec(ObjectCart.scnGetCart)
  val scnAddToCartVide = scenario("Ajouter Panier").exec(ObjectCart.scnGetCart)
  val scnAddToCart = scenario("Nouveau panier").exec(ObjectCart.scnAddToCart)
  val scnPanierExistant = scenario("Panier Existant").exec(ObjectCart.scnPanierExistant)

  setUp(
    scnGetCart.inject(rampUsers(nbVu * 10) during (TpsMonteEnCharge minutes), nothingFor(TpsPalier minutes)),
    scnAddToCartVide.inject(rampUsers(nbVu * 10) during (TpsMonteEnCharge minutes), nothingFor(TpsPalier minutes)),
    scnAddToCart.inject(rampUsers(nbVu * 10) during (TpsMonteEnCharge minutes), nothingFor(TpsPalier minutes)),
    scnPanierExistant.inject(rampUsers(nbVu * 10) during (TpsMonteEnCharge minutes), nothingFor(TpsPalier minutes))
  ).protocols(httpProtocol)
    .maxDuration(DureeMax minutes)

}
