package APIGALERIE

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.language.postfixOps

object ObjectPageCategorie {

  private val tpsPaceDefault: Int = System.getProperty("tpsPace", "1000").toInt
  private val tpsPacingProducts: Int = System.getProperty("tpsPaceProducts", tpsPaceDefault.toString).toInt


  private val TpsPause: Int = System.getProperty("tpsPause", "10").toInt
  private val TempoMillisecond: Int = System.getProperty("TempoMillisecond", "10").toInt

  //private val NbreIter : Int = System.getProperty("nbIter", "5000").toInt
  private val NbreIterDefault: Int = System.getProperty("nbIter", "10").toInt
  private val NbreIter: Int = System.getProperty("nbIterProduct", NbreIterDefault.toString).toInt

  private val groupBy: String = System.getProperty("groupBy", "PageCategorie")

  // private val FichierPath: String = System.getProperty("dataDir", "./src/test/resources/data/")
  private val FichierPath: String = System.getProperty("dataDir", "data/")
  private val FichierDataCategorieId: String = "JddApiCategorie.csv"

  val jddDataCategorieId = csv(FichierPath + FichierDataCategorieId).circular

  ///////////////////////////////////////////////////
  ///     scenario :     Definition de la page            ///
  ///////////////////////////////////////////////////

  val scnDefinePage = scenario("Page Definition")

    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PageCategorie") {
      exec { session => session.set("LeGroup", "PageCategorie") }
    } {
      exec { session => session.set("LeGroup", "PageCategorie") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "DefinitionPage") }

        .feed(jddDataCategorieId)
        .exec { session =>
          println(session("CategorieId").as[String])
          session
        }

        .group("${LeGroup}") {
          exec(http("${L_API}")
            .get("/gl-fr/cms/pages?pageType=CategoryPage&code=${CategorieId}&lang=fr&curr=EUR")
            .header("Content-Type", "application/json")
            .check(status.is(200))
          )
            .exec { session => println(session); session }

        } // Fin group("${LeGroup}")
    } // Fin forever

  ///////////////////////////////////////////////////
  ///     scenario :       Appel Fredhopper	             ///
  ///////////////////////////////////////////////////

  val scnFredhopper = scenario("API_FredHopper")


    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PageCategorie") {
      exec { session => session.set("LeGroup", "PageCategorie") }
    } {
      exec { session => session.set("LeGroup", "PageCategorie") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "API_FredHopper") }


        .group("${LeGroup}") {
          exec(http("${L_API}")
            .get("/gl-fr/cms/pages?pageType=CategoryPage&code=${CategorieId}&lang=fr&curr=EUR")
            .header("Content-Type", "application/json")
            .check(status.is(200))
          )

        } // Fin group("${LeGroup}")
    } // Fin forever

  ///////////////////////////////////////////////////
  ///     scenario :       Composants CMS	             ///
  ///////////////////////////////////////////////////

  val scnComposantCMS = scenario("Composant CMS")


    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PageCategorie") {
      exec { session => session.set("LeGroup", "PageCategorie") }
    } {
      exec { session => session.set("LeGroup", "PageCategorie") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "Composant_CMS") }


        .group("${LeGroup}") {
          exec(http("${L_API}")
            .get("/gl-fr/cms/components?fields=DEFAULT&categoryCode=${CategorieId}&currentPage=0&pageSize=37&componentIds=reinsuranceElement01,reinsuranceElement02,reinsuranceElement03,reinsuranceElement04,glFbBanner,glInstaBanner,glPinterestBanner,glTiktokBanner,GLInspiration,banner1,banner2,banner3,banner4,GlSeoLink1,GlSeoLink2,GlSeoLink3,GlSeoLink4,GlSeoLink5,GlSeoLink6,GlSeoLink7,GlSeoLink8,GlSeoLink9,GlSeoLink10,GlSeoLink11,GlSeoLink12,GlSeoLink13,GlSeoLink14,GlSeoLink15,GlSeoLink16,GlSeoLink17,GlSeoLink18,GlSeoLink19,GlSeoLink20,GlSeoLink21,GlSeoLink22,GlSeoLink23,GlSeoLink24&lang=fr&curr=EUR")
            .header("Content-Type", "application/json")
            .check(status.is(200))
          )

        } // Fin group("${LeGroup}")
    } // Fin forever


  ///////////////////////////////////////////////////
  ///     scenario :       Données Categories            ///
  ///////////////////////////////////////////////////

  val scnDataCategorie = scenario("Données Categories ")


    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PageCategorie") {
      exec { session => session.set("LeGroup", "PageCategorie") }
    } {
      exec { session => session.set("LeGroup", "PageCategorie") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "Données_Categories ") }


        .group("${LeGroup}") {
          exec(http("${L_API}")
            .get("/gl-fr/catalogs/gl-frProductCatalog/Main/categories/${CategorieId}?fields=DEFAULT&lang=fr&curr=EUR")
            .header("Content-Type", "application/json")
            .check(status.is(200))
          )

        } // Fin group("${LeGroup}")
    } // Fin forever


}
