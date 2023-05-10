package APIGALERIE

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.language.postfixOps

object ObjectCart {


  private val tpsPaceDefault: Int = System.getProperty("tpsPace", "1000").toInt
  private val tpsPacingProducts: Int = System.getProperty("tpsPaceProducts", tpsPaceDefault.toString).toInt


  private val NbreIterDefault: Int = System.getProperty("nbIter", "10").toInt
  private val NbreIter: Int = System.getProperty("nbIterProduct", NbreIterDefault.toString).toInt

  private val groupBy: String = System.getProperty("groupBy", "PageCart")
  private val FichierPath: String = System.getProperty("dataDir", "data/")
  private val FichierDataCodeId: String = "JddCode.csv"

  val jddDataCodeId = csv(FichierPath + FichierDataCodeId).circular



  ///////////////////////////////////////////////////
  ///     scenario :     Obtenir le panier            ///
  ///////////////////////////////////////////////////

  val scnGetCart = scenario("Obtenir le panier")

    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PageCart") {
      exec { session => session.set("LeGroup", "PageCart") }
    } {
      exec { session => session.set("LeGroup", "PageCart") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "Get_Cart") }

        .group("${LeGroup}") {
          exec(http("${L_API}")
            .get("/occ/v2/gl-fr/users/current/carts?fields=carts(DEFAULT,potentialProductPromotions,appliedProductPromotions(FULL),potentialOrderPromotions,appliedOrderPromotions,entries(totalPrice(formattedValue),product(images(FULL),stock(FULL)),basePrice(formattedValue,value),updateable),totalPrice(formattedValue),totalItems,totalPriceWithTax(formattedValue),totalDiscounts(value,formattedValue),subTotal(formattedValue),deliveryItemsQuantity,deliveryCost(formattedValue),totalTax(formattedValue, value),pickupItemsQuantity,net,appliedVouchers,productDiscounts(formattedValue),user,saveTime,name,description)&lang=fr&curr=EUR")
            .header("Content-Type", "application/json")
            .check(status.is(200))
          )
            .exec { session => println(session); session }

        } // Fin group("${LeGroup}")
    } // Fin forever

  ///////////////////////////////////////////////////
  ///     scenario :     Ajouter au panier  vide         ///
  ///////////////////////////////////////////////////

  val scnAddToCartVide = scenario("Add To Cart")

    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PageCart") {
      exec { session => session.set("LeGroup", "PageCart") }
    } {
      exec { session => session.set("LeGroup", "PageCart") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "Add_To_Cart_vide") }
        .group("${LeGroup}") {
          exec(http("${L_API}")
            .post("/gl-fr/users/current/carts?fields=DEFAULT%2CpotentialProductPromotions%2CappliedProductPromotions(FULL)%2CpotentialOrderPromotions%2CappliedOrderPromotions%2Centries(totalPrice(formattedValue)%2Cproduct(images(FULL)%2Cstock(FULL))%2CbasePrice(formattedValue%2Cvalue)%2Cupdateable)%2CtotalPrice(formattedValue)%2CtotalItems%2CtotalPriceWithTax(formattedValue)%2CtotalDiscounts(value%2CformattedValue)%2CsubTotal(formattedValue)%2CdeliveryItemsQuantity%2CdeliveryCost(formattedValue)%2CtotalTax(formattedValue%2C value)%2CpickupItemsQuantity%2Cnet%2CappliedVouchers%2CproductDiscounts(formattedValue)%2Cuser&lang=fr&curr=EUR ")
            .body(StringBody("""{}""")).asJson
            .header("Content-Type", "application/json")
            .check(status.is(200))
          )
            .exec { session => println(session); session }

        } // Fin group("${LeGroup}")
    } // Fin forever

  ///////////////////////////////////////////////////
  ///     scenario :     Nouveau Panier            ///
  ///////////////////////////////////////////////////

  val scnAddToCart = scenario("Nouveau Panier")

    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PageCart") {
      exec { session => session.set("LeGroup", "PageCart") }
    } {
      exec { session => session.set("LeGroup", "PageCart") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "Nouveau Page") }
        .feed(jddDataCodeId)
        .exec { session =>
          println(session("CodeId").as[String])
          session
        }
        .group("${LeGroup}") {
          exec(http("${L_API}")
            .post("/gl-fr/users/current/carts/00005077/entries?lang=fr&curr=EUR")
            .body(StringBody("""{"quantity":1,"product":{"code":"${code}"}}""")).asJson
            .header("Content-Type", "application/json")
            .check(status.is(200))
          )
            .exec { session => println(session); session }

        } // Fin group("${LeGroup}")
    } // Fin forever

  ///////////////////////////////////////////////////
  ///     scenario :     Panier Existant            ///
  ///////////////////////////////////////////////////

  val scnPanierExistant = scenario("Nouveau")

    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PageCart") {
      exec { session => session.set("LeGroup", "PageCart") }
    } {
      exec { session => session.set("LeGroup", "PageCart") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "Panier_Existant") }
        .group("${LeGroup}") {
          exec(http("${L_API}")
            .get("/gl-fr/users/current/carts/00005077?fields=DEFAULT,potentialProductPromotions,appliedProductPromotions,potentialOrderPromotions(FULL),appliedOrderPromotions(FULL),entries(totalPrice(formattedValue),product(logisticFamily,images(FULL),stock(FULL),price(formattedValue,regularRetailPrice)),basePrice(formattedValue,value),updateable),totalPrice(formattedValue),totalItems,totalPriceWithTax(formattedValue),totalDiscounts(value,formattedValue),subTotal(formattedValue),deliveryItemsQuantity,deliveryCost(formattedValue),totalTax(formattedValue, value),pickupItemsQuantity,net,appliedVouchers,productDiscounts(formattedValue),user,saveTime,name,description,deliveryMode,deliveryOrderGroups(pointOfDelivery(FULL))&lang=fr&curr=EUR")
            .header("Content-Type", "application/json")
            .check(status.is(200))
          )
            .exec { session => println(session); session }

        } // Fin group("${LeGroup}")
    } // Fin forever




}
