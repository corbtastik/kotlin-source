package io.corbs.howdy

import org.springframework.util.ObjectUtils
import java.net.InetAddress
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    val ip: InetAddress?
        get() {
            var ip: InetAddress? = null
            try {
                ip = InetAddress.getLocalHost()
                println("Your current IP address : " + ip!!)
            } catch (ex: UnknownHostException) {
                ex.printStackTrace()
            }

            return ip
        }

    @Throws(java.text.ParseException::class)
    fun parseDate(input: String): Date {
        var input = input

        // NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        // things a bit. Before we go on we have to repair this.
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz")

        // this is zero time so we need to add that TZ indicator for
        if (input.endsWith("Z")) {
            input = input.substring(0, input.length - 1) + "GMT-00:00"
        } else {
            val inset = 6
            val s0 = input.substring(0, input.length - inset)
            val s1 = input.substring(input.length - inset, input.length)
            input = s0 + "GMT" + s1
        }

        return df.parse(input)
    }

    fun parseDateSilently(input: String): Date? {
        try {
            return if (ObjectUtils.isEmpty(input)) null else parseDate(input)
        } catch (e: ParseException) {
            return null
        }

    }

    fun dateAsString(date: Date): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
        val tz = TimeZone.getTimeZone("UTC")
        df.timeZone = tz
        val output = df.format(date)
        return output.replace("UTC".toRegex(), "+00:00")
    }

    fun now(): String {
        return dateAsString(Date())
    }

}