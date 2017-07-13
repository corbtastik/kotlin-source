package io.corbs.howdy

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Output
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.GenericMessage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

fun main(args: Array<String>) {
    SpringApplication.run(HowdyAPI::class.java, *args)
}

@SpringBootApplication
@RestController
@EnableBinding(SourceChannels::class)
class HowdyAPI(@Autowired val channels: SourceChannels) {

    @PostMapping("/howdy")
    fun sayHowdy() {
        val message = "Howdy ${RandomThings.firstName}"
        println(message)
        channels.output().send(GenericMessage(message))
    }

    @GetMapping("ip")
    fun getIp(): Map<String, String> {
        val response = HashMap<String, String>()
        response.put("ip", AppUtils.ip.toString())
        return response
    }

    @GetMapping("datetime")
    fun getDateTime(): Map<String, String> {
        val response = HashMap<String, String>()
        response.put("datetime", AppUtils.now())
        return response
    }

    @GetMapping("vcap/application")
    fun getVcapApplication(): Map<String, Any> {
        return CfUtils.vcapApplication
    }

    @GetMapping("vcap/services")
    fun getVcapServices(): Map<String, Any> {
        return CfUtils.vcapServices
    }

}

interface SourceChannels {
    @Output fun output(): MessageChannel
}
