package org.isen.news.model.data

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestCountry {
    @Before
    fun init() {
        FuelManager.instance.basePath = "https://www.restcountries.eu/rest/v2"
    }

    @Test
    fun getCountryDataForFrance() {
        val (request, response, result) = "/alpha?codes=fr"
                .httpGet().responseObject(Country.Deserializer())
        assertTrue(response.isSuccessful)

        result.component1().also {
            assertNotNull(it).also { println(it) }
        }?.let {
            it.let { req ->
                assertTrue(req.isNotEmpty())
                req.first().let { c ->
                    assertEquals("France", c.name)
                    assertEquals("FR", c.alpha2Code)
                    assertEquals("fr", c.alpha2Code.toLowerCase())
                    assertEquals("https://restcountries.eu/data/fra.svg",
                            c.flag
                    )
                }
            }
        }
    }

    @Test
    fun getCountryDataMultiple() {
        val (request, response, result) = "/alpha?codes=fr;gb;us"
                .httpGet().responseObject(Country.Deserializer())
        assertTrue(response.isSuccessful)

        result.component1().also {
            assertNotNull(it).also { req ->
                req.forEach { c ->
                    println(c)
                }
            }
        }?.let {
            it.let { req ->
                assertEquals(3, req.size)
                req.first().let { c ->
                    assertEquals("France", c.name)
                    assertEquals("FR", c.alpha2Code)
                    assertEquals("fr", c.alpha2Code.toLowerCase())
                    assertEquals("https://restcountries.eu/data/fra.svg",
                            c.flag
                    )
                }
                req[1].let { c ->
                    assertEquals("United Kingdom of Great Britain and Northern Ireland", c.name)
                    assertEquals("GB", c.alpha2Code)
                    assertEquals("gb", c.alpha2Code.toLowerCase())
                    assertEquals("https://restcountries.eu/data/gbr.svg",
                            c.flag
                    )
                }
                req[2].let { c ->
                    assertEquals("United States of America", c.name)
                    assertEquals("US", c.alpha2Code)
                    assertEquals("us", c.alpha2Code.toLowerCase())
                    assertEquals("https://restcountries.eu/data/usa.svg",
                            c.flag
                    )
                }
            }
        }
    }

    @Test
    fun getDataForUnknownCode() {
        val (request, response, result) = "/alpha?codes=uk"
                .httpGet().responseObject(Country.Deserializer())
        assertTrue(response.isSuccessful)

        result.component1().also {
            assertNotNull(it).also {
                it.forEach { c ->
                    println(c)
                }
            }
        }?.let {
            assertNull(it.first())
        }
    }

    @Test
    fun getMultipleDataWithUnknownCode() {
        val (request, response, result) = "/alpha?codes=fr;uk;us"
                .httpGet().responseObject(Country.Deserializer())
        assertTrue(response.isSuccessful)

        result.component1().also {
            assertNotNull(it).also { req ->
                req.forEach { c ->
                    println(c)
                }
            }
        }?.let {
            it.let { req ->
                assertEquals(3, req.size)
                req.first().let { c ->
                    assertEquals("France", c.name)
                    assertEquals("FR", c.alpha2Code)
                    assertEquals("fr", c.alpha2Code.toLowerCase())
                    assertEquals("https://restcountries.eu/data/fra.svg",
                            c.flag
                    )
                }
                assertNull(req[1])
                req[2].let { c ->
                    assertEquals("United States of America", c.name)
                    assertEquals("US", c.alpha2Code)
                    assertEquals("us", c.alpha2Code.toLowerCase())
                    assertEquals("https://restcountries.eu/data/usa.svg",
                            c.flag
                    )
                }
            }
        }
    }
}